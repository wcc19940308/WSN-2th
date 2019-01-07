package ExperimentCode;

import com.google.common.collect.Range;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MyDegreeDistribution extends DegreeDistribution{
    double c; // ϵ��c
    double ��; // ϵ����
    double k; // ��֪����k
    double R; // ϵ��R
    double a; // ������ָ��ϵ������
    double b; // ³�����ӷֲ�ϵ������

    public MyDegreeDistribution(double c, double ��, double k, double a, double b) {
        this.c = c;
        this.�� = ��;
        this.k = k;
        this.R = c * Math.log(k / ��) * Math.sqrt(k);
        this.a = a;
        this.b = b;
    }

    // ��ö�����-³�����ӷֲ��ĸ��ʱ�
    public Map<Double, Double> getBinaryRobustSolitonDistribution() {
        // ��������һ������ĺ�
        double W1 = 0;
        double W2 = 0;
        double W = 0;
        double prob = 0;
        Map<Double, Double> probTable = new LinkedHashMap<Double, Double>();
        // ����������Ӧ���һ����
        for (double i=1; i<=k; i++) {
            if (i == 1) {
                W1 += 1 / k;
            } else {
                W1 += 1 / (i * (i - 1));
            }
        }
        // ����³�����Ӧ���һ����
        for (double i=1; i<=Math.ceil(k/R); i++) {
            if (i >= 1 && i <= k / R - 1) {
                W1 += R / (i * k);
            } else if (i == Math.ceil(k / R)) {
                W1 += (R * Math.log(R / ��)) / k;
            }
        }

        // ���������ָ������
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
