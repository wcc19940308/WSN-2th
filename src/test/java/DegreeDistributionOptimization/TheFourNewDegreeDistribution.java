package DegreeDistributionOptimization;

import ExperimentCode.DegreeDistribution;
import ExperimentCode.Experiment;
import ExperimentCode.NewDegreeDistribution;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class TheFourNewDegreeDistribution {
    public static void main(String[] args) throws IOException {
        Experiment Experiment1 = new Experiment(1000, 1000, 0,
                5000, 10000, 30);
        Experiment Experiment2 = new Experiment(1000, 1000, 0,
                5000, 10000, 30);
        Experiment Experiment3 = new Experiment(1000, 1000, 0,
                5000, 10000, 30);
        Experiment Experiment4 = new Experiment(1000, 1000, 0,
                5000, 10000, 30);
        Experiment Experiment5 = new Experiment(1000, 1000, 0,
                5000, 10000, 30);

        double a1 = 0.38920795976855915, b1 = 0.4108600993677495;
        double a2 =0.784499337781702, b2 = 0.2603566604258498;
        double a3 = 1.7128947521680336, b3 = 0.011768445451988518;
        double a4 = 0.8013792412144722, b4 = 0.0657077185758227;
        double a5 = 0.3503100729837485, b5 = 0.04592954906681446;

        DegreeDistribution degreeDistribution1 = new NewDegreeDistribution(0.01, 0.05, 5000, a1, b1);
        DegreeDistribution degreeDistribution2 = new NewDegreeDistribution(0.01, 0.05, 5000, a2, b2);
        DegreeDistribution degreeDistribution3 = new NewDegreeDistribution(0.01, 0.05, 5000, a3, b3);
        DegreeDistribution degreeDistribution4 = new NewDegreeDistribution(0.01, 0.05, 5000, a4, b4);
        DegreeDistribution degreeDistribution5 = new NewDegreeDistribution(0.01, 0.05, 5000, a5, b5);

        Map<Double, Double> robustSolitonDistribution1 = degreeDistribution1.getRobustSolitonDistribution();
        Map<Double, Double> robustSolitonDistribution2 = degreeDistribution2.getRobustSolitonDistribution();
        Map<Double, Double> robustSolitonDistribution3 = degreeDistribution3.getRobustSolitonDistribution();
        Map<Double, Double> robustSolitonDistribution4 = degreeDistribution4.getRobustSolitonDistribution();
        Map<Double, Double> robustSolitonDistribution5 = degreeDistribution5.getRobustSolitonDistribution();

        File file = new File("G:/lab/LabData/NewDistribution/5种新的度分布函数度分布情况.txt");
        file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        for (Double index : robustSolitonDistribution1.keySet()) {
            Double p1 = robustSolitonDistribution1.get(index);
            Double p2 = robustSolitonDistribution2.get(index);
            Double p3 = robustSolitonDistribution3.get(index);
            Double p4 = robustSolitonDistribution4.get(index);
            Double p5 = robustSolitonDistribution5.get(index);
            out.write(index + " " + p1 + " " + p2 + " " + p3 + " " + p4 + " " + p5 + "\r\n");
            System.out.println("degree=" + index + " p1=" + p1 + " p2=" + p2 + " p3=" + p3 + " p4=" + p4 + " p5=" + p5 + "\r\n");
        }
        out.flush();
    }
}
