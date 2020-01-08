package LabData;

import ExperimentCode.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;

/**
 * 数据包到达可接收节点所消费的随机游走步长所占原始步长的比例
 * 对比初始步长为0 - 1000
 */
public class WalkLengthRatio {
    public static void main(String[] args) throws IOException {
        File file = new File("G:/lab/LabData/总步长占初始步长的比例.txt");
        file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 30; j++) {
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

        double LTIntialWalkLength = LTSimulator.walkLength;
        double ELFCIntialWalkLength = ELFCSimulator.walkLength;
        double OELFCIntialWalkLength = OELFCSimulator.walkLength;

        double LTRedudancy = LTSimulator.REDUNDANCY_WALK_LENGTH;
        double ELFCRedudancy = ELFCSimulator.REDUNDANCY_WALK_LENGTH;
        double OELFCRedudancy = OELFCSimulator.REDUNDANCY_WALK_LENGTH;

        double LTAll = LTIntialWalkLength + LTRedudancy;
        double ELFCAll = ELFCIntialWalkLength + ELFCRedudancy;
        double OELFCAll = OELFCIntialWalkLength + OELFCRedudancy;

        System.out.println("LT:" + LTAll / LTIntialWalkLength + " ELFC:" + ELFCAll / ELFCIntialWalkLength
                + " OELFC:" + OELFCAll / OELFCIntialWalkLength);


        System.out.println("---开始写文件啦---");

        int codeNum = 5000;

        NumberFormat instance = NumberFormat.getInstance();
        instance.setMaximumFractionDigits(2);

        String LTResult = instance.format((float) LTAll / (float) LTIntialWalkLength);

        String ELFCResult = instance.format((float) ELFCAll / (float) ELFCIntialWalkLength);

        String OELFCResult = instance.format((float) OELFCAll / (float) OELFCIntialWalkLength );

        // LT ELFC OELFC的所需步长
        out.write(Config.WALK_LENGTH + " " + LTResult + " " + ELFCResult + " " + OELFCResult + "\r\n");
        out.flush();
    }
}
