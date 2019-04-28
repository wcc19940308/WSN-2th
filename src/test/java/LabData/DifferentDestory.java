package LabData;

import ExperimentCode.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;

// 不同的节点破坏比例对解码率的影响,每个破坏率执行50次，求平均值
public class DifferentDestory {

    public static void main(String[] args) throws IOException {
        File file = new File("G:/lab/LabData/半径为50破坏率从0.01到0.1的解码情况.txt");
        file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        Config.DESTORY_RATIO = 0;
        for (int i = 1; i <= 10; i++) {
            Config.DESTORY_RATIO += 0.01;
            for (int j=0; j<=50; j++)
                init(Config.DESTORY_RATIO, out);
        }
        out.flush();
        out.close();
    }

    // 稠密网络受到破坏的影响会相对小一点，因此测试了半径为31和50两种情况下不同破坏率的影响
    public static void init(double destoryRatio, BufferedWriter out) throws IOException {
        Experiment LTExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 50);
        Experiment ELFCExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 50);
        Experiment OELFCExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 50);

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

        String LTResult = instance.format((float) LTSize / (float) codeNum);
        String ELFCResult = instance.format((float) ELFCSize / (float) codeNum);
        String OELFCResult = instance.format((float)OELFCSize / (float)codeNum);

        out.write(Config.DESTORY_RATIO + " " + LTResult + " " + ELFCResult + " " + OELFCResult + "\r\n");
        out.flush();
    }
}
