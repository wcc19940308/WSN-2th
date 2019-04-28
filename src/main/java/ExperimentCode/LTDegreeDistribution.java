package ExperimentCode;

import com.google.common.collect.Range;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class LTDegreeDistribution extends ExperimentCode.DegreeDistribution {
    double c; // 系数c
    double δ; // 系数δ
    double k; // 感知数量k
    double R; // 系数R
    double peak; // 尖峰节点值

    public LTDegreeDistribution(double c, double δ, double k) {
        this.c = c;
        this.δ = δ;
        this.k = Math.ceil(k);
        this.R = c * Math.log(k / δ) * Math.sqrt(k);
        this.peak = Math.ceil(k / R);
        System.out.println("集中式LT需要的节点数量：" + (k + c * Math.sqrt(k) * Math.log(k / δ) * Math.log(k / δ)));
        System.out.println("尖峰值为："+peak);
    }

    // 产生哈希表用于索引，哈希表中记录了各个不同的度所对应的概率
    public Map<Double, Double> getRobustSolitonDistribution() {
        // double R = c * Math.log(k / δ) * Math.sqrt(k);
        // 理想孤子和鲁棒孤子总的概率和，用于做归一化处理,这里的1是先计算了理想孤子的和
        double β = 0;
        double prob = 0;
        Map<Double, Double> probTable = new LinkedHashMap<Double, Double>();
        // 计算理想孤子ρ那一部分
        for (double i=1; i<=k; i++) {
            if (i == 1) {
                β += 1 / k;
            } else {
                β += 1 / (i * (i - 1));
            }
        }
        // 计算鲁棒孤子τ那一部分
        // Math.ceil用于修正k/R的尖峰部分，如果不向上取整则遍历i的过程中可能取不到k/R！
        for (double i=1; i<=Math.ceil(k/R); i++) {
            if (i >= 1 && i <= k / R - 1) {
                β += R / (i * k);
            } else if (i == Math.ceil(k / R)) {
                β += (R * Math.log(R / δ)) / k;
            }
        }
        //System.out.println(β);
        for (double i=1; i<=k; i++) {
            if (i == 1) {
                prob = ((1 / k) + R / (i * k)) / β;
                // 度为i的概率是prob
                probTable.put(i, prob);
            } else if (i > 1 && i <= k / R - 1) {
                prob = (1 / (i * (i - 1)) + R / (i * k)) / β;
                probTable.put(i, prob);
            } else if (i == Math.ceil(k / R)) {
                prob = (1 / (i * (i - 1)) + (R * Math.log(R / δ)) / k) / β;
                probTable.put(i, prob);
            } else {
                prob = (1 / (i * (i - 1)))/β;
                probTable.put(i, prob);
            }
        }
        return probTable;
    }

    // 根据得到的概率分布计算出相应的概率区间
    public Map<Double, Range<Double>> getProbInterval() {
        Map<Double, Double> degreeDistribution = getRobustSolitonDistribution();
        double start = 0;
        double end = 0;
        Map<Double, Range<Double>> probInterval = new LinkedHashMap<Double, Range<Double>>();
        for (double index : degreeDistribution.keySet()) {
            double prob = degreeDistribution.get(index);
            // 因为度分布函数表和概率区间表长度一直，而且概率区间表是由度分布函数表推出的，所以这里使用度分布函数的长度
            // 主要是为了概率区间能够覆盖整个0-100区域，防止在为节点分配编码度的时候没有对应的概率区间而导致度为0的情况出现
            if (index == degreeDistribution.size()-1)
                end = 100;
            else{
                // 将区间放大100倍，便于将各个度区分开来
                end = prob * 100 + start;
            }
            probInterval.put(index, Range.closedOpen(start, end));
            start = end;
        }
        //System.out.println(probInterval.size());
        return  probInterval;
    }

    // 获得尖峰值的概率，用于计算尖峰值节点的数量
    public double getPeakProb() {
        Map<Double, Double> robustSolitonDistribution = getRobustSolitonDistribution();
        return robustSolitonDistribution.get(Math.ceil(peak));
    }
}
