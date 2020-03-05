package GraduationPaper.PartTwo;


import ExperimentCode.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;

/**
 * 译码开销的实验，成功的和灾难的
 * 0%-40%的灾难性实验，同时记录译码开销, 0%的可以直接拿来当作成功解码的译码开销(每个10次)
 */
public class DecodingOverheadAndDestory {
    public static void main(String[] args) throws IOException {
        File file = new File("G:/lab/GraduationPaper/PartTwo/RSD和ERSD0-40破坏实验.txt");
        file.createNewFile();
        BufferedWriter out1 = new BufferedWriter(new FileWriter(file));

        File file2 = new File("G:/lab/GraduationPaper/PartTwo/RSD和ERSD译码开销.txt");
        file2.createNewFile();
        BufferedWriter out2 = new BufferedWriter(new FileWriter(file2));

        Config.DESTORY_RATIO = 0;
        for (int i = 1; i <= 40; i++) {
            for (int j = 0; j <= 5; j++)
                init(Config.DESTORY_RATIO, out1, out2);
            Config.DESTORY_RATIO += 0.01;
        }
        out1.flush();
        out1.close();

        out2.flush();
        out2.close();
    }

    // 稠密网络受到破坏的影响会相对小一点，因此测试了半径为31和50两种情况下不同破坏率的影响
    public static void init(double destoryRatio, BufferedWriter out1, BufferedWriter out2) throws IOException {
        Experiment LTExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 50);
        Experiment ELFCExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 50);
        double x = 9.99989814e-01, y = 1.00000002e-05;
        DegreeDistribution LTdegreeDistribution = new LTDegreeDistribution(0.01,0.05,5000);
        DegreeDistribution ELFCdegreeDistribution
                = new NewDegreeDistribution(0.01, 0.05, 5000, x, y);

        // 开始进行编码操作
        LTSimulator LTSimulator = new LTSimulator(
                new SpaceHelper(LTExperiment, LTdegreeDistribution, NodeTypeEnum.MRF_LT));
        LTSimulator ELFCSimulator = new LTSimulator(
                new SpaceHelper(ELFCExperiment, ELFCdegreeDistribution, NodeTypeEnum.MRF_LT));

        Sink LTSink = new Sink(LTSimulator);
        Sink ELFCSink = new Sink(ELFCSimulator);

        boolean LT = LTSink.collectPackage(NodeTypeEnum.MRF_LT);
        boolean ELFC = ELFCSink.collectPackage(NodeTypeEnum.MRF_LT);
        System.out.println("LT:" + LT + " ELFC:" + ELFC);


        System.out.println("---开始写文件啦---");

        int codeNum = 5000;

        int LTSize = LTSink.getOneDegreeData().size();
        int ELFCSize = ELFCSink.getOneDegreeData().size();
        System.out.println("LTSize:" + LTSize + " ELFCSize:" + ELFCSize);

        NumberFormat instance = NumberFormat.getInstance();
        instance.setMaximumFractionDigits(2);

        String LTResult = instance.format((float) LTSize / (float) codeNum);
        String ELFCResult = instance.format((float) ELFCSize / (float) codeNum);

        out1.write(Config.DESTORY_RATIO + " " + LTResult + " " + ELFCResult + "\r\n");
        out1.flush();


        double RSDsuccessDecodingRatio = (LTSink.getSuccessDecodingPackageNumber() - 5000) / 5000;
        double ERSDsuccessDecodingRatio = (ELFCSink.getSuccessDecodingPackageNumber() - 5000) / 5000;
        out2.write(Config.DESTORY_RATIO + " " + RSDsuccessDecodingRatio
                + " " + ERSDsuccessDecodingRatio);
    }
}
