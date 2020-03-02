package GraduationPaper.PartTwo;

import ExperimentCode.DegreeDistribution;
import ExperimentCode.LTDegreeDistribution;
import ExperimentCode.NewDegreeDistribution;

import java.util.Map;

public class Demo {
    public static void main(String[] args) {
        DegreeDistribution degreeDistribution = new LTDegreeDistribution(0.01, 0.05, 5000);
        Map<Double, Double> robustSolitonDistribution =
                degreeDistribution.getRobustSolitonDistribution();
        Double res = robustSolitonDistribution.get(1.0);
        System.out.println(res);
    }
}
