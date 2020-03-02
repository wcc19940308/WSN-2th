package GraduationPaper.PartTwo;

import ExperimentCode.DegreeDistribution;
import ExperimentCode.Experiment;
import ExperimentCode.LTDegreeDistribution;
import ExperimentCode.NewDegreeDistribution;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

// 新的度分布函数图
public class NewDegreeDistrubition {
    public static void main(String[] args) throws IOException {
        Experiment LTExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 30);
        double x = 9.99989814e-01, y = 1.00000002e-05;
//        DegreeDistribution degreeDistribution = new NewDegreeDistribution(0.01, 0.05, 5000, x, y);
        DegreeDistribution degreeDistribution = new LTDegreeDistribution(0.2, 0.05, 5000);
        Map<Double, Double> robustSolitonDistribution = degreeDistribution.getRobustSolitonDistribution();
        System.out.println(robustSolitonDistribution.get(1.0));
        File file = new File("G:/lab/GraduationPaper/PartTwo/RSD度分布情况111.txt");
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
