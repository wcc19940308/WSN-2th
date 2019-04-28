package LabData;

import ExperimentCode.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;

// 考察c从0.01到0.5对3分区的网络解码率的影响
public class COfThreePartition {
    public static void main(String[] args) throws IOException {
        File file = new File("G:/lab/LabData/半径为30的分区数为3的c从0.01到0.5的情况.txt");
        file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        double cofficient_of_c = 0;
        for (int i = 0; i <= 50; i++) {
            cofficient_of_c += 0.01;
            for (int j = 0; j < 50; j++) {
                init(cofficient_of_c, out);
            }
        }
        out.flush();
        out.close();
    }

    // 稠密网络受到破坏的影响会相对小一点，因此测试了半径为31和50两种情况下不同破坏率的影响
    public static void init(double cofficient_of_c, BufferedWriter out) throws IOException {
        //Experiment LTExperiment = new Experiment(1000, 1000, 0,
        //5000, 10000, 50);
        Experiment LPFCExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 30);


        //DegreeDistribution LTdegreeDistribution = new LTDegreeDistribution(0.01,0.05,5000);
        DegreeDistribution LPFCdegreeDistribution = new LTDegreeDistribution(cofficient_of_c,0.05,5000/3);

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

        out.write(cofficient_of_c + " " + LPFCResult + " " + "\r\n");
        out.flush();
    }
}
