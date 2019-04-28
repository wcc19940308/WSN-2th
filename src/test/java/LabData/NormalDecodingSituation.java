package LabData;

import ExperimentCode.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

// 初始条件下的解码率情况
public class NormalDecodingSituation {
    public static void main(String[] args) throws IOException {

        Experiment LTExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 40);
        Experiment ELFCExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 40);
        Experiment OELFCExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 40);

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

        Map<Integer, Integer> LTMap = LTSink.getDecodingRatio();
        Map<Integer, Integer> ELFCMap = ELFCSink.getDecodingRatio();
        Map<Integer, Integer> OELFCMap = OELFCSink.getDecodingRatio();
        File file = new File("G:/lab/LabData/半径40初始化条件解码1111.txt");
        file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        for (Integer index : LTMap.keySet()) {
            out.write(index + " " + LTMap.get(index) +" "+ELFCMap.get(index)+" "+OELFCMap.get(index)+ "\r\n");
        }
        out.flush();
        out.close();
    }
}
