package DegreeDistributionOptimization;

import ExperimentCode.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * 鲁棒孤子的度分布图
 */
public class DegreeDistibutionGraph {
    public static void main(String[] args) throws IOException {
        Experiment LTExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 30);
        DegreeDistribution LTdegreeDistribution = new LTDegreeDistribution(0.2,0.05,5000);
        Map<Double, Double> robustSolitonDistribution = LTdegreeDistribution.getRobustSolitonDistribution();
        File file = new File("G:/lab/LabData/度分布情况.txt");
        file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        for (Double index : robustSolitonDistribution.keySet()) {
            double value = robustSolitonDistribution.get(index);
            out.write(index.intValue() + " " + value + "\r\n");
        }
        out.flush();
        out.close();
    }
}
