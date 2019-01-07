package ExperimentCode;

import com.google.common.collect.Range;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MyDegreeDistribution extends DegreeDistribution{
    double c; // 系数c
    double δ; // 系数δ
    double k; // 感知数量k
    double R; // 系数R
    double a; // 二进制指数系数部分
    double b; // 鲁棒孤子分布系数部分

    public MyDegreeDistribution(double c, double δ, double k, double a, double b) {
        this.c = c;
        this.δ = δ;
        this.k = k;
        this.R = c * Math.log(k / δ) * Math.sqrt(k);
        this.a = a;
        this.b = b;
    }

    // 获得二进制-鲁棒孤子分布的概率表
    public Map<Double, Double> getBinaryRobustSolitonDistribution() {
        // 用于做归一化处理的和
        double W1 = 0;
        double W2 = 0;
        double W = 0;
        double prob = 0;
        Map<Double, Double> probTable = new LinkedHashMap<Double, Double>();
        // 计算理想孤子ρ那一部分
        for (double i=1; i<=k; i++) {
            if (i == 1) {
                W1 += 1 / k;
            } else {
                W1 += 1 / (i * (i - 1));
            }
        }
        // 计算鲁棒孤子τ那一部分
        for (double i=1; i<=Math.ceil(k/R); i++) {
            if (i >= 1 && i <= k / R - 1) {
                W1 += R / (i * k);
            } else if (i == Math.ceil(k / R)) {
                W1 += (R * Math.log(R / δ)) / k;
            }
        }

        // 计算二进制指数部分
        for (double i=1; i<=k; i++) {
            if (i < k) {
                W2 += 1 / (Math.pow(2, i));
            } else if (i == k) {
                W2 += 1 / (Math.pow(2, i - 1));
            }
        }
        W = a * W2 + b * W1;

        return new HashMap<>();
    }

    public Map<Double, Range<Double>> getProbInterval() {
        return null;
    }

    public Map<Double, Double> getRobustSolitonDistribution() {
        return null;
    }

    public  double getPeak(){return 0;}

    public  double getPeakProb() {
        return 0;
    }
}
