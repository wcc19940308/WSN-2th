package GraduationPaper.PartOne;

import ExperimentCode.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;

// MOW和MRF在LT的收集策略下的数据恢复情况
public class TwoStrategyDecodingRate {
    public static void main(String[] args) throws IOException {
        File file = new File("G:/lab/GraduationPaper/MOW和MRF主动收集方式的解码率1111.txt");
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
        Experiment MOW_LTExp = new Experiment(1000, 1000, 0,
                5000, 10000, 30);
        Experiment MRF_LTExp = new Experiment(1000, 1000, 0,
                5000, 10000, 30);


        DegreeDistribution MOW_LTdegreeDistribution = new LTDegreeDistribution(0.01,0.05,5000);
        DegreeDistribution MRF_LTdegreeDistribution = new LTDegreeDistribution(0.01, 0.05, 5000);

        LTSimulator MOW_LTSimulator = new LTSimulator(
                new SpaceHelper(MOW_LTExp, MOW_LTdegreeDistribution, NodeTypeEnum.MOW_LT));
        LTSimulator MRF_LTSimulator = new LTSimulator(
                new SpaceHelper(MRF_LTExp, MRF_LTdegreeDistribution, NodeTypeEnum.MRF_LT));

        Sink MOW_LTSink = new Sink(MOW_LTSimulator);
        Sink MRF_LTSink = new Sink(MRF_LTSimulator);

        boolean MOW_LT = MOW_LTSink.collectPackage(NodeTypeEnum.MOW_LT);
        boolean MRF_LT = MRF_LTSink.collectPackage(NodeTypeEnum.MRF_LT);

        System.out.println("MOW_LT:" + MOW_LT + " MRF_LT:" + MRF_LT);


        System.out.println("---开始写文件啦---");

        int codeNum = 5000;

        int MOW_LTSize = MOW_LTSink.getOneDegreeData().size();
        int MRF_LTSize = MRF_LTSink.getOneDegreeData().size();

        NumberFormat instance = NumberFormat.getInstance();
        instance.setMaximumFractionDigits(2);

        String MOW_LTResult = instance.format((float) MOW_LTSize / (float) codeNum);
        String MRF_LTResult = instance.format((float) MRF_LTSize / (float) codeNum);


        out.write(Config.WALK_LENGTH + " " + MOW_LTResult + " " + MRF_LTResult + "\r\n");
        out.flush();
    }
}

