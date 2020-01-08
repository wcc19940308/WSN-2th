package GraduationPaper.PartOne;

import ExperimentCode.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;

// 四种策略随一次步长的升高的解码情况
public class FourStrategyDecodingRate {
    public static void main(String[] args) throws IOException {
        File file = new File("G:/lab/GraduationPaper/MOW中的两种策略的解码率随步长变化的影响.txt");
        file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        for (int i = 0; i < 20; i++) {
            // for (int j = 0; j < 30; j++) {
                init(out);
            //}
            Config.WALK_LENGTH += 50;
        }
        out.flush();
        out.close();
    }

    public static void init(BufferedWriter out) throws IOException {
        Experiment MOW_ELFCExp = new Experiment(1000, 1000, 0,
                5000, 10000, 30);
        Experiment MOW_OELFCExp = new Experiment(1000, 1000, 0,
                5000, 10000, 30);
        Experiment MRF_ELFCExp = new Experiment(1000, 1000, 0,
                5000, 10000, 30);
        Experiment MRF_OELFCExp = new Experiment(1000, 1000, 0,
                5000, 10000, 30);

        DegreeDistribution MOW_ELFCdegreeDistribution = new LTDegreeDistribution(0.01,0.05,5000);
        DegreeDistribution MOW_OELFCdegreeDistribution = new LTDegreeDistribution(0.01,0.05,5000);
        DegreeDistribution MRF_ELFCdegreeDistribution = new LTDegreeDistribution(0.01, 0.05, 5000);
        DegreeDistribution MRF_OELFCdegreeDistribution = new LTDegreeDistribution(0.01, 0.05, 5000);

        LTSimulator MOW_ELFCSimulator = new LTSimulator(
                new SpaceHelper(MOW_ELFCExp, MOW_ELFCdegreeDistribution, NodeTypeEnum.MOWELFC));
        LTSimulator MOW_OELFCSimulator = new LTSimulator(
                new SpaceHelper(MOW_OELFCExp, MOW_OELFCdegreeDistribution, NodeTypeEnum.MOWOELFC));
        LTSimulator MRF_ELFCSimulator = new LTSimulator(
                new SpaceHelper(MRF_ELFCExp, MRF_ELFCdegreeDistribution, NodeTypeEnum.MRFELFC));
        LTSimulator MRF_OELFCSimulator = new LTSimulator(
                new SpaceHelper(MRF_OELFCExp, MRF_OELFCdegreeDistribution, NodeTypeEnum.MRFOELFC));

        Sink MOW_ELFCSink = new Sink(MOW_ELFCSimulator);
        Sink MOW_OELFCSink = new Sink(MOW_OELFCSimulator);
        Sink MRF_ELFCSink = new Sink(MRF_ELFCSimulator);
        Sink MRF_OELFCSink = new Sink(MRF_OELFCSimulator);

        boolean MOW_ELFC = MOW_ELFCSink.collectPackage(NodeTypeEnum.MOWELFC);
        boolean MOW_OELFC = MOW_OELFCSink.collectPackage(NodeTypeEnum.MOWOELFC);
        boolean MRF_ELFC = MRF_ELFCSink.collectPackage(NodeTypeEnum.MRFELFC);
        boolean MRF_OELFC = MRF_OELFCSink.collectPackage(NodeTypeEnum.MRFOELFC);
        System.out.println("MOWELFC:" + MOW_ELFC + " MOWOELFC:" + MOW_OELFC + " MRFELFC:" + MRF_ELFC + " MRFOELFC:" + MRF_OELFC);


        System.out.println("---开始写文件啦---");

        int codeNum = 5000;

        int MOW_ELFCSize = MOW_ELFCSink.getOneDegreeData().size();
        int MOW_OELFCSize = MOW_OELFCSink.getOneDegreeData().size();
        int MRF_ELFCSize = MRF_ELFCSink.getOneDegreeData().size();
        int MRF_OELFCSize = MRF_OELFCSink.getOneDegreeData().size();

        NumberFormat instance = NumberFormat.getInstance();
        instance.setMaximumFractionDigits(2);

        String MOW_ELFCResult = instance.format((float) MOW_ELFCSize / (float) codeNum);

        String MOW_OELFCResult = instance.format((float) MOW_OELFCSize / (float) codeNum);

        String MRF_ELFCResult = instance.format((float) MRF_ELFCSize / (float) codeNum );

        String MRF_OELFCResult = instance.format((float) MRF_OELFCSize / (float) codeNum );

        out.write(Config.WALK_LENGTH + " " + MOW_ELFCResult + " " + MOW_OELFCResult + " " + MRF_ELFCResult + " " + MRF_OELFCResult + "\r\n");
        out.flush();
    }
}
