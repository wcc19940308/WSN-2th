package ExperimentCode;

import com.google.common.collect.Range;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class LTDegreeDistribution extends ExperimentCode.DegreeDistribution {
    double c; // ϵ��c
    double ��; // ϵ����
    double k; // ��֪����k
    double R; // ϵ��R
    double peak; // ���ڵ�ֵ

    public LTDegreeDistribution(double c, double ��, double k) {
        this.c = c;
        this.�� = ��;
        this.k = Math.ceil(k);
        this.R = c * Math.log(k / ��) * Math.sqrt(k);
        this.peak = Math.ceil(k / R);
        System.out.println("����ʽLT��Ҫ�Ľڵ�������" + (k + c * Math.sqrt(k) * Math.log(k / ��) * Math.log(k / ��)));
        System.out.println("���ֵΪ��"+peak);
    }

    // ������ϣ��������������ϣ���м�¼�˸�����ͬ�Ķ�����Ӧ�ĸ���
    public Map<Double, Double> getRobustSolitonDistribution() {
        // double R = c * Math.log(k / ��) * Math.sqrt(k);
        // ������Ӻ�³�������ܵĸ��ʺͣ���������һ������,�����1���ȼ�����������ӵĺ�
        double �� = 0;
        double prob = 0;
        Map<Double, Double> probTable = new LinkedHashMap<Double, Double>();
        // ����������Ӧ���һ����
        for (double i=1; i<=k; i++) {
            if (i == 1) {
                �� += 1 / k;
            } else {
                �� += 1 / (i * (i - 1));
            }
        }
        // ����³�����Ӧ���һ����
        // Math.ceil��������k/R�ļ�岿�֣����������ȡ�������i�Ĺ����п���ȡ����k/R��
        for (double i=1; i<=Math.ceil(k/R); i++) {
            if (i >= 1 && i <= k / R - 1) {
                �� += R / (i * k);
            } else if (i == Math.ceil(k / R)) {
                �� += (R * Math.log(R / ��)) / k;
            }
        }
        //System.out.println(��);
        for (double i=1; i<=k; i++) {
            if (i == 1) {
                prob = ((1 / k) + R / (i * k)) / ��;
                // ��Ϊi�ĸ�����prob
                probTable.put(i, prob);
            } else if (i > 1 && i <= k / R - 1) {
                prob = (1 / (i * (i - 1)) + R / (i * k)) / ��;
                probTable.put(i, prob);
            } else if (i == Math.ceil(k / R)) {
                prob = (1 / (i * (i - 1)) + (R * Math.log(R / ��)) / k) / ��;
                probTable.put(i, prob);
            } else {
                prob = (1 / (i * (i - 1)))/��;
                probTable.put(i, prob);
            }
        }
        return probTable;
    }

    // ���ݵõ��ĸ��ʷֲ��������Ӧ�ĸ�������
    public Map<Double, Range<Double>> getProbInterval() {
        Map<Double, Double> degreeDistribution = getRobustSolitonDistribution();
        double start = 0;
        double end = 0;
        Map<Double, Range<Double>> probInterval = new LinkedHashMap<Double, Range<Double>>();
        for (double index : degreeDistribution.keySet()) {
            double prob = degreeDistribution.get(index);
            // ��Ϊ�ȷֲ�������͸����������һֱ�����Ҹ�����������ɶȷֲ��������Ƴ��ģ���������ʹ�öȷֲ������ĳ���
            // ��Ҫ��Ϊ�˸��������ܹ���������0-100���򣬷�ֹ��Ϊ�ڵ�������ȵ�ʱ��û�ж�Ӧ�ĸ�����������¶�Ϊ0���������
            if (index == degreeDistribution.size()-1)
                end = 100;
            else{
                // ������Ŵ�100�������ڽ����������ֿ���
                end = prob * 100 + start;
            }
            probInterval.put(index, Range.closedOpen(start, end));
            start = end;
        }
        //System.out.println(probInterval.size());
        return  probInterval;
    }

    // ��ü��ֵ�ĸ��ʣ����ڼ�����ֵ�ڵ������
    public double getPeakProb() {
        Map<Double, Double> robustSolitonDistribution = getRobustSolitonDistribution();
        return robustSolitonDistribution.get(Math.ceil(peak));
    }
}
