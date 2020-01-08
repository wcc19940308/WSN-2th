package GraduationPaper.PartOne;

import ExperimentCode.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

// 高估K和N在三种收集方式下成功解码所需要的数据包数量，每个重复20次
public class OverestimationOfKAndNDecodingNumber {
    public static void main(String[] args) throws IOException {
        File file = new File("G:/lab/GraduationPaper/MRFRW策略高估N在3种收集方式下的成功解码所需数据包数量.txt");
        file.createNewFile  ();
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
        File file2 = new File("G:/lab/GraduationPaper/MRFRW策略高估K在3种收集方式下的成功解码所需数据包数量.txt");
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


        System.err.println(" MRFELFC:" + MRF_ELFC  + " MRF_LT:" + MRF_LT);


        System.out.println("---开始写文件啦---");

        out.write(Config.K + " "
                + MRF_LTSink.getSuccessDecodingPackageNumber() + " "
                + MRF_ELFCSink.getSuccessDecodingPackageNumber() + " "
                + MRF_OELFCSink.getSuccessDecodingPackageNumber() + " "
                + "\r\n");

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


        System.err.println(" MRFELFC:" + MRF_ELFC + "MRFOELFC" + MRF_OELFC + " MRF_LT:" + MRF_LT);

        System.out.println("---开始写文件啦---");

        out.write(Config.N + " "
                + MRF_LTSink.getSuccessDecodingPackageNumber() + " "
                + MRF_ELFCSink.getSuccessDecodingPackageNumber() + " "
                + MRF_OELFCSink.getSuccessDecodingPackageNumber() + " "
                + "\r\n");

        out.flush();
    }

}
