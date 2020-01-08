package LabData;

import ExperimentCode.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;

/**
 * 网络总的随机游走开销
 */
public class AllWalkLengthSituation {
    public static void main(String[] args) throws IOException {
        File file = new File("G:/lab/LabData/总步长的增加情况.txt");
        file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                init(out);
            }
            Config.WALK_LENGTH += 50;
        }
        out.flush();
        out.close();
    }

    public static void init(BufferedWriter out) throws IOException {
        Experiment LTExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 30);
        Experiment ELFCExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 30);
        Experiment OELFCExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 30);

        DegreeDistribution LTdegreeDistribution = new LTDegreeDistribution(0.01,0.05,5000);
        DegreeDistribution ELFCdegreeDistribution = new LTDegreeDistribution(0.01,0.05,5000);
        DegreeDistribution OELFCdegreeDistribution = new LTDegreeDistribution(0.01, 0.05, 5000);

        LTSimulator LTSimulator = new LTSimulator(
                new SpaceHelper(LTExperiment, LTdegreeDistribution, NodeTypeEnum.LT));
        LTSimulator ELFCSimulator = new LTSimulator(
                new SpaceHelper(ELFCExperiment, ELFCdegreeDistribution, NodeTypeEnum.NORMAL_BY_LAYER_LT));
        LTSimulator OELFCSimulator = new LTSimulator(
                new SpaceHelper(OELFCExperiment, OELFCdegreeDistribution, NodeTypeEnum.LAYER_LT));

        // 一次步长
        double LTIntialWalkLength = LTSimulator.walkLength;
        double ELFCIntialWalkLength = ELFCSimulator.walkLength;
        double OELFCIntialWalkLength = OELFCSimulator.walkLength;


        // 二次步长
        double LTRedudancy = LTSimulator.REDUNDANCY_WALK_LENGTH;
        double ELFCRedudancy = ELFCSimulator.REDUNDANCY_WALK_LENGTH;
        double OELFCRedudancy = OELFCSimulator.REDUNDANCY_WALK_LENGTH;


        // 总步长
        double LTAll = LTIntialWalkLength + LTRedudancy;
        double ELFCAll = ELFCIntialWalkLength + ELFCRedudancy;
        double OELFCAll = OELFCIntialWalkLength + OELFCRedudancy;

        System.out.println("LT:" + LTAll + " ELFC:" + ELFCAll
                + " OELFC:" + OELFCAll);


        System.out.println("---开始写文件啦---");

        // LT ELFC OELFC的所需步长
        out.write(Config.WALK_LENGTH + " " + LTAll + " " + ELFCAll + " " + OELFCAll + "\r\n");
        out.flush();
    }
}
