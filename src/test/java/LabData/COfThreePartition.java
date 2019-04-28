package LabData;

import ExperimentCode.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;

// ����c��0.01��0.5��3��������������ʵ�Ӱ��
public class COfThreePartition {
    public static void main(String[] args) throws IOException {
        File file = new File("G:/lab/LabData/�뾶Ϊ30�ķ�����Ϊ3��c��0.01��0.5�����.txt");
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

    // ���������ܵ��ƻ���Ӱ������Сһ�㣬��˲����˰뾶Ϊ31��50��������²�ͬ�ƻ��ʵ�Ӱ��
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


        System.out.println("---��ʼд�ļ���---");

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
