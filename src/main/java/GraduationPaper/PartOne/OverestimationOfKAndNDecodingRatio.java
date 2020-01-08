package GraduationPaper.PartOne;

import ExperimentCode.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;

// 高估K和N在3种收集方式下的的解码率具体情况,每个重复20次
public class OverestimationOfKAndNDecodingRatio {
    public static void main(String[] args) throws IOException {
        File file = new File("G:/lab/GraduationPaper/MRFRW策略高估N在3种收集方式下的解码率.txt");
        file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        for (int i = 0; i < 21; i++) {
            for (int j = 0; j < 20; j++) {
                overestimationN(out);
            }
            Config.N += 100;
        }
        out.flush();
        out.close();

        Config.N = 10000;
        File file2 = new File("G:/lab/GraduationPaper/MRFRW策略高估K在3种收集方式下的解码率.txt");
        file2.createNewFile();
        BufferedWriter out2 = new BufferedWriter(new FileWriter(file2));
        for (int i = 0; i < 21; i++) {
            for (int j = 0; j < 20; j++) {
                overestimationK(out2);
            }
            Config.K += 100;
        }
        out2.flush();
        out2.close();
    }

    // 高估K
    public static void overestimationK(BufferedWriter out) throws IOException{
        Experiment MRF_ELFCExp = new Experiment(1000, 1000, 0,
                5000, 10000, 30);
        Experiment MRF_OELFCExp = new Experiment(1000, 1000, 0,
                5000, 10000, 30);

        Experiment MRF_LTExp = new Experiment(1000, 1000, 0,
                5000, 10000, 30);

        DegreeDistribution MRF_ELFCdegreeDistribution = new LTDegreeDistribution(0.01, 0.05, 5000);
        DegreeDistribution MRF_OELFCdegreeDistribution = new LTDegreeDistribution(0.01, 0.05, 5000);
        DegreeDistribution MRF_LTdegreeDistribution = new LTDegreeDistribution(0.01, 0.05, 5000);


        LTSimulator MRF_ELFCSimulator = new LTSimulator(
                new SpaceHelper(MRF_ELFCExp, MRF_ELFCdegreeDistribution, NodeTypeEnum.MRFELFC));
        LTSimulator MRF_OELFCSimulator = new LTSimulator(
                new SpaceHelper(MRF_OELFCExp, MRF_OELFCdegreeDistribution, NodeTypeEnum.MRFOELFC));
        LTSimulator MRF_LTSimulator = new LTSimulator(
                new SpaceHelper(MRF_LTExp, MRF_LTdegreeDistribution, NodeTypeEnum.MRF_LT));


        Sink MRF_ELFCSink = new Sink(MRF_ELFCSimulator);
        Sink MRF_OELFCSink = new Sink(MRF_OELFCSimulator);
        Sink MRF_LTSink = new Sink(MRF_LTSimulator);

        boolean MRF_ELFC = MRF_ELFCSink.collectPackage(NodeTypeEnum.MRFELFC);
        boolean MRF_OELFC = MRF_OELFCSink.collectPackage(NodeTypeEnum.MRFOELFC);
        boolean MRF_LT = MRF_LTSink.collectPackage(NodeTypeEnum.MRF_LT);


        System.err.println(" MRF_LT:" + MRF_LT + " MRFELFC:" + MRF_ELFC + "MRFOELFC" + MRF_OELFC);


        System.out.println("---开始写文件啦---");

        int codeNum = 5000;

        int MRF_LTSize = MRF_LTSink.getOneDegreeData().size();
        int MRF_ELFCSize = MRF_ELFCSink.getOneDegreeData().size();
        int MRF_OELFCSize = MRF_OELFCSink.getOneDegreeData().size();

        NumberFormat instance = NumberFormat.getInstance();
        instance.setMaximumFractionDigits(2);

        String MRF_LTResult = instance.format((float) MRF_LTSize / (float) codeNum);

        String MRF_ELFCResult = instance.format((float) MRF_ELFCSize / (float) codeNum );

        String MRF_OELFCResult = instance.format((float) MRF_OELFCSize / (float) codeNum );

        out.write(Config.K + " " + MRF_LTResult + " " + MRF_ELFCResult + " " + MRF_OELFCResult + "\r\n");
        out.flush();
    }

    // 高估N
    public static void overestimationN(BufferedWriter out) throws IOException{
        Experiment MRF_ELFCExp = new Experiment(1000, 1000, 0,
                5000, 10000, 30);
        Experiment MRF_OELFCExp = new Experiment(1000, 1000, 0,
                5000, 10000, 30);

        Experiment MRF_LTExp = new Experiment(1000, 1000, 0,
                5000, 10000, 30);

        DegreeDistribution MRF_ELFCdegreeDistribution = new LTDegreeDistribution(0.01, 0.05, 5000);
        DegreeDistribution MRF_OELFCdegreeDistribution = new LTDegreeDistribution(0.01, 0.05, 5000);
        DegreeDistribution MRF_LTdegreeDistribution = new LTDegreeDistribution(0.01, 0.05, 5000);


        LTSimulator MRF_ELFCSimulator = new LTSimulator(
                new SpaceHelper(MRF_ELFCExp, MRF_ELFCdegreeDistribution, NodeTypeEnum.MRFELFC));
        LTSimulator MRF_OELFCSimulator = new LTSimulator(
                new SpaceHelper(MRF_OELFCExp, MRF_OELFCdegreeDistribution, NodeTypeEnum.MRFOELFC));
        LTSimulator MRF_LTSimulator = new LTSimulator(
                new SpaceHelper(MRF_LTExp, MRF_LTdegreeDistribution, NodeTypeEnum.MRF_LT));


        Sink MRF_ELFCSink = new Sink(MRF_ELFCSimulator);
        Sink MRF_OELFCSink = new Sink(MRF_OELFCSimulator);
        Sink MRF_LTSink = new Sink(MRF_LTSimulator);

        boolean MRF_ELFC = MRF_ELFCSink.collectPackage(NodeTypeEnum.MRFELFC);
        boolean MRF_OELFC = MRF_OELFCSink.collectPackage(NodeTypeEnum.MRFOELFC);
        boolean MRF_LT = MRF_LTSink.collectPackage(NodeTypeEnum.MRF_LT);


        System.err.println(" MRF_LT:" + MRF_LT + " MRFELFC:" + MRF_ELFC + "MRFOELFC" + MRF_OELFC);

        System.out.println("---开始写文件啦---");


        int codeNum = 5000;

        int MRF_LTSize = MRF_LTSink.getOneDegreeData().size();
        int MRF_ELFCSize = MRF_ELFCSink.getOneDegreeData().size();
        int MRF_OELFCSize = MRF_OELFCSink.getOneDegreeData().size();

        NumberFormat instance = NumberFormat.getInstance();
        instance.setMaximumFractionDigits(2);

        String MRF_LTResult = instance.format((float) MRF_LTSize / (float) codeNum);

        String MRF_ELFCResult = instance.format((float) MRF_ELFCSize / (float) codeNum );

        String MRF_OELFCResult = instance.format((float) MRF_OELFCSize / (float) codeNum );

        out.write(Config.N + " " + MRF_LTResult + " " + MRF_ELFCResult + " " + MRF_OELFCResult + "\r\n");
        out.flush();
    }
}
