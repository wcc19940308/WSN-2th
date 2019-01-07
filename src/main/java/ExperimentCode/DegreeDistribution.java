package ExperimentCode;

import com.google.common.collect.Range;

import java.util.Map;

public abstract class DegreeDistribution {
    // ��ø��ʷֲ�����ĺ���
    public abstract Map<Double, Range<Double>> getProbInterval();

    public abstract Map<Double, Double> getRobustSolitonDistribution();

    public abstract double getPeak();

    public abstract double getPeakProb();
}
