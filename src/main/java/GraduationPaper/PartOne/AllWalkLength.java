package GraduationPaper.PartOne;

import ExperimentCode.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

// �ܲ������
public class AllWalkLength {
    public static void main(String[] args) throws IOException {
        File file = new File("G:/lab/GraduationPaper/�����ռ���ʽMRF�ܲ������������.txt");
        file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        for (int i = 0; i < 20; i++) {
            //for (int j = 0; j < 10; j++) {
                init(out);
            // }
            Config.WALK_LENGTH += 50;
        }
        out.flush();
        out.close();
    }

    public static void init(BufferedWriter out) throws IOException {
        Experiment LTExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 30);


        DegreeDistribution LTdegreeDistribution = new LTDegreeDistribution(0.01,0.05,5000);


        LTSimulator LTSimulator = new LTSimulator(
                new SpaceHelper(LTExperiment, LTdegreeDistribution, NodeTypeEnum.MRF_LT));


        // һ�β���
        double LTIntialWalkLength = LTSimulator.walkLength;



        // ���β���
        double LTRedudancy = LTSimulator.REDUNDANCY_WALK_LENGTH;



        // �ܲ���
        double LTAll = LTIntialWalkLength + LTRedudancy;


        System.out.println("MRF_LT:" + LTAll );


        System.out.println("---��ʼд�ļ���---");

        // LT ELFC OELFC�����貽��
        out.write(Config.WALK_LENGTH + " " + LTAll + "\r\n");
        out.flush();
    }
}
