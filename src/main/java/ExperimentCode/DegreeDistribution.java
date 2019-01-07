package ExperimentCode;

import com.google.common.collect.Range;

import java.util.Map;

public abstract class DegreeDistribution {
    // 获得概率分布区间的函数
    public abstract Map<Double, Range<Double>> getProbInterval();

    public abstract Map<Double, Double> getRobustSolitonDistribution();

    public abstract double getPeak();

    public abstract double getPeakProb();
}
