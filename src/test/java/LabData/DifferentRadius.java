package LabData;

import ExperimentCode.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Map;

// 不同网络半径对网络解码情况的影响
public class DifferentRadius {
    public static void main(String[] args) throws IOException {
        File file = new File("G:/lab/LabData/半径从30到50的解码情况.txt");
        file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        int neighborRadius = 20;
        for (int i = 0; i < 20; i++) {
            init(neighborRadius, out);
            neighborRadius++;
        }
        out.flush();
        out.close();
    }

    public static void init(int neighborRadius, BufferedWriter out) throws IOException {
        Experiment LTExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, neighborRadius);
        Experiment ELFCExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, neighborRadius);
        Experiment OELFCExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, neighborRadius);

        DegreeDistribution LTdegreeDistribution = new LTDegreeDistribution(0.01,0.05,5000);
        DegreeDistribution ELFCdegreeDistribution = new LTDegreeDistribution(0.01,0.05,5000);
        DegreeDistribution OELFCdegreeDistribution = new LTDegreeDistribution(0.01, 0.05, 5000);

        LTSimulator LTSimulator = new LTSimulator(
                new SpaceHelper(LTExperiment, LTdegreeDistribution, NodeTypeEnum.LT));
        LTSimulator ELFCSimulator = new LTSimulator(
                new SpaceHelper(ELFCExperiment, ELFCdegreeDistribution, NodeTypeEnum.NORMAL_BY_LAYER_LT));
        LTSimulator OELFCSimulator = new LTSimulator(
                new SpaceHelper(OELFCExperiment, OELFCdegreeDistribution, NodeTypeEnum.LAYER_LT));

        Sink LTSink = new Sink(LTSimulator);
        Sink ELFCSink = new Sink(ELFCSimulator);
        Sink OELFCSink = new Sink(OELFCSimulator);

        boolean LT = LTSink.collectPackage(NodeTypeEnum.LT);
        boolean ELFC = ELFCSink.collectPackage(NodeTypeEnum.NORMAL_BY_LAYER_LT);
        boolean OELFC = OELFCSink.collectPackage(NodeTypeEnum.LAYER_LT);
        System.out.println("LT:" + LT + " ELFC:" + ELFC + " OELFC:" + OELFC);


        System.out.println("---开始写文件啦---");

        int codeNum = 5000;

        int LTSize = LTSink.getOneDegreeData().size();
        int ELFCSize = ELFCSink.getOneDegreeData().size();
        int OELFCSize = OELFCSink.getOneDegreeData().size();

        NumberFormat instance = NumberFormat.getInstance();
        instance.setMaximumFractionDigits(2);

        String LTResult = instance.format((float) codeNum / (float) LTSize);
        String ELFCResult = instance.format((float) codeNum / (float) ELFCSize);
        String OELFCResult = instance.format((float)codeNum / (float)OELFCSize);

        out.write(neighborRadius + " " + LTResult + " " + ELFCResult + " " + OELFCResult + "\r\n");
        out.flush();
    }
}
