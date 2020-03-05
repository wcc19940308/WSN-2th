package GraduationPaper.PartTwo;

import ExperimentCode.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;

// 主动收集方式的随机游走总步长
public class RandomWalkLength {
    public static void activeCollection(BufferedWriter out) throws IOException {
        double x = 9.99989814e-01, y = 1.00000002e-05;
        Experiment LTExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 30);
        Experiment ERSDExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 30);
        DegreeDistribution LTdegreeDistribution = new LTDegreeDistribution(0.01,0.05,5000);
        DegreeDistribution ERSDdegreeDistribution = new NewDegreeDistribution(0.01, 0.05, 5000, x, y);


        LTSimulator LTSimulator = new LTSimulator(
                new SpaceHelper(LTExperiment, LTdegreeDistribution, NodeTypeEnum.MRF_LT));
        LTSimulator ERSDSimulator = new LTSimulator(
                new SpaceHelper(LTExperiment, ERSDdegreeDistribution, NodeTypeEnum.MRF_LT));

        double LTIntialWalkLength = LTSimulator.walkLength;
        double ERSDIntialWalkLength = ERSDSimulator.walkLength;

        double LTRedudancy = LTSimulator.REDUNDANCY_WALK_LENGTH;
        double ERSDRedudancy = ERSDSimulator.REDUNDANCY_WALK_LENGTH;

        double LTAll = LTIntialWalkLength + LTRedudancy;
        double ERSDAll = ERSDIntialWalkLength + ERSDRedudancy;

        System.err.println("RSD:" + LTAll );
        System.err.println("ERSD:" + ERSDAll );

        System.out.println("---开始写文件啦---");


        // LT ELFC OELFC的所需步长
        out.write(Config.WALK_LENGTH + " " + LTAll + " " + ERSDAll + "\r\n");
        out.flush();
    }


    // 50到1000的初始步长，每个实验20次  2-29
    public static void main(String[] args) throws IOException {
        File file = new File("/Users/wangchenchao/Desktop/" +
                "毕业论文/figures/RSD和ERSD随机游走.txt");
        file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        Config.WALK_LENGTH = 50;
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 5; j++) {
                activeCollection(out);
            }
            Config.WALK_LENGTH += 50;
        }
        out.flush();
        out.close();
    }
}
