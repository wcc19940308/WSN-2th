package LabData;

import ExperimentCode.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;

/**
 * 初试步长50 - 1000对3种码的解码影响
 * 每种步长重复实验30次
 */
public class IntialStepFactor {
    public static void main(String[] args) throws IOException {
        File file = new File("G:/lab/LabData/初始步长的影响-每个重复实验30次.txt");
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

        String OELFCResult = instance.format((float) OELFCSize / (float) codeNum );

        out.write(Config.WALK_LENGTH + " " + LTResult + " " + ELFCResult + " " + OELFCResult + "\r\n");
        out.flush();
    }
}
