package LabData;

import ExperimentCode.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;

// 分区数量M对分区效果的影响
public class MOfPartitoin {

    public static void main(String[] args) throws IOException {
        File file = new File("G:/lab/LabData/半径为30的分区数M从2到10的情况.txt");
        file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        Config.PARTITION_NUM = 1;
        for (int i = 2; i <= 10; i++) {
            ++Config.PARTITION_NUM;
            for (int j = 0; j < 50; j++) {
                init(Config.PARTITION_NUM, out);
            }
        }
        out.flush();
        out.close();
    }

    // 稠密网络受到破坏的影响会相对小一点，因此测试了半径为31和50两种情况下不同破坏率的影响
    public static void init(int partitionNum, BufferedWriter out) throws IOException {
        //Experiment LTExperiment = new Experiment(1000, 1000, 0,
                //5000, 10000, 50);
        Experiment LPFCExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 50);


        //DegreeDistribution LTdegreeDistribution = new LTDegreeDistribution(0.01,0.05,5000);
        DegreeDistribution LPFCdegreeDistribution = new LTDegreeDistribution(0.01,0.05,5000/partitionNum);

        //LTSimulator LTSimulator = new LTSimulator(
                //new SpaceHelper(LTExperiment, LTdegreeDistribution, NodeTypeEnum.LT));
        LTSimulator LPFCSimulator = new LTSimulator(
                new SpaceHelper(LPFCExperiment, LPFCdegreeDistribution, NodeTypeEnum.PARTITION_LT));


        //Sink LTSink = new Sink(LTSimulator);
        Sink LPFCSink = new Sink(LPFCSimulator);

        //boolean LT = LTSink.collectPackage(NodeTypeEnum.LT);
        boolean LPFC = LPFCSink.collectPackage(NodeTypeEnum.PARTITION_LT);
        //System.out.println("LT:" + LT + " LPFC:" + LPFC);
        System.out.println(" LPFC:" + LPFC);


        System.out.println("---开始写文件啦---");

        int codeNum = 5000;

        //int LTSize = LTSink.getOneDegreeData().size();
        int LPFCSize = LPFCSink.getOneDegreeData().size();

        NumberFormat instance = NumberFormat.getInstance();
        instance.setMaximumFractionDigits(2);

        //String LTResult = instance.format((float) LTSize / (float) codeNum);
        String LPFCResult = instance.format((float) LPFCSize / (float) codeNum);

        out.write(Config.PARTITION_NUM + " " + LPFCResult + " " + "\r\n");
        out.flush();
    }
}
