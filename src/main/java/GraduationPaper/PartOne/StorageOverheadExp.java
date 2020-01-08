package GraduationPaper.PartOne;

import ExperimentCode.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * �洢������ʵ�飬ͨ���Ƚ�EDFC�յ��ı������ݰ�������1֮��ıȽ�
 * 1. x:�ڵ���ͣ����ͬ���ݰ������� y:�ڵ���Ҫ�Ĵ洢�ռ�
 * 2. x: ������ߵ�һ�β���(��ΪELFC���ܲ����Ƚ��Ѻ�EDFC���ܲ����ҵ�һ����) y:��������ڵ�Ĵ洢����
 *
  */

public class StorageOverheadExp {

    public static void distinctNodeStorageOverhead() throws IOException {
        Map<Integer, Integer> EDFCMap = new HashMap<>();
        Map<Integer, Integer> myMap = new HashMap<>();
        for (int i = 1; i <= 100; i++) {
            EDFCMap.put(i, i);
            myMap.put(i, 1);
        }
        System.out.println("---��ʼд�ļ���---");
        File file = new File("G:/lab/GraduationPaper/�ڵ�洢����.txt");
        StringBuffer str = new StringBuffer();
        FileWriter fw = new FileWriter("G:/lab/GraduationPaper/�ڵ�洢����.txt", true);

        for (Integer index : EDFCMap.keySet()) {
            str.append(index + " " + EDFCMap.get(index) + " "
                    + myMap.get(index)
                    + "\r\n");
        }
        fw.write(str.toString());
        fw.close();
    }

    // ���ֲ��ԵĴ洢�������Լ��ܲ����Ա�
    public static void twoStrategyCompare() throws IOException {
        File storageFile = new File("G:/lab/GraduationPaper/һ�β����ʹ洢�����Ĺ�ϵ.txt");
        File walkLengthFile = new File("G:/lab/GraduationPaper/һ�β������ܲ����Ĺ�ϵ.txt");

        storageFile.createNewFile();
        walkLengthFile.createNewFile();

        BufferedWriter storageOut = new BufferedWriter(new FileWriter(storageFile));
        BufferedWriter walkLengthOut = new BufferedWriter(new FileWriter(walkLengthFile));
        // һ�β�����50��1000���ã��۲�ʵ����
        for (int i = 0; i < 20; i++) {
            // ������Ϊ����ƽ��10��
            for (int j = 0; j < 10; j++) {
                networkStorageOverhead(storageOut, walkLengthOut);
            }
            Config.WALK_LENGTH += 50;
        }
        storageOut.flush();
        walkLengthOut.flush();

        storageOut.close();
        walkLengthOut.close();
    }

    /**
     * �����е����ݰ����ɸ�֪�ڵ�����ģ����x: ���������������ߵĲ������� y:����������Ĵ洢����(����EDFC�������ݰ��������������ǵĲ��Ծ������нڵ���
     * packList.size() != 0 �Ľڵ������)
     * ע��EDFC�ж���1.4058�Ŀ���
     * <p>
     * ����������Ҫ2��������
     * 1. EDFC��ELFC�Ĺ���һ�β����ĸı䣬�洢�����ı仯
     * 2. EDFC��ELFC�Ĺ���һ�β����ĸı䣬�ܲ����ı仯
     * ͨ��2�����Է�ӳ����1��˵���ܲ����ı仯�����߹��ڴ洢�����ı仯
     */
    public static void networkStorageOverhead(BufferedWriter storageOut, BufferedWriter walkLengthOut) throws IOException {
        Experiment EDFCExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 30);
        Experiment ELFCExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 30);

        DegreeDistribution EDFCdegreeDistribution = new LTDegreeDistribution(0.01, 0.05, 5000);
        DegreeDistribution ELFCdegreeDistribution = new LTDegreeDistribution(0.01, 0.05, 5000);

        LTSimulator EDFCSimulator = new LTSimulator(
                new SpaceHelper(EDFCExperiment, EDFCdegreeDistribution, NodeTypeEnum.EDFC));
        LTSimulator ELFCSimulator = new LTSimulator(
                new SpaceHelper(ELFCExperiment, ELFCdegreeDistribution, NodeTypeEnum.LT));

        // EDFC��ELFC���ܲ���
        double EDFCWalkLength = EDFCSimulator.walkLength;
        double ELFCWalkLength = ELFCSimulator.walkLength
                + ELFCSimulator.REDUNDANCY_WALK_LENGTH;

        double count = 0;
        Map<Integer, Node> ELFCNodes = ELFCSimulator.getNodes();
        for (Integer index : ELFCNodes.keySet()) {
            if (ELFCNodes.get(index).getPackList().size() != 0) count++;
        }

        // ע�⣬����EDFC�Ĵ洢��������b * k * xd�����������ݰ�������
        storageOut.write(Config.WALK_LENGTH + " " + (int) (EDFCWalkLength / Config.WALK_LENGTH) + " " + count + "\r\n");
        storageOut.flush();

        walkLengthOut.write(Config.WALK_LENGTH + " " + EDFCWalkLength + " " + ELFCWalkLength + "\r\n");
        walkLengthOut.flush();
    }

    // ����ת�����Կմ洢�����ʵ��
    public static void emptyStorageExp() throws IOException {
        File storageFile = new File("G:/lab/GraduationPaper/1.5���������ݰ��Ķ���ת�����Դ洢�������.txt");
        storageFile.createNewFile();
        BufferedWriter storageOut = new BufferedWriter(new FileWriter(storageFile));
        // һ�β�����50��1000���ã��۲�ʵ����
        for (int i = 0; i < 20; i++) {
            // ������Ϊ����ƽ��10��
            for (int j = 0; j < 10; j++) {
                emptyStorageImp(storageOut);
            }
            Config.WALK_LENGTH += 50;
        }
        storageOut.flush();
        storageOut.close();
    }

    public static void emptyStorageImp(BufferedWriter storageOut) throws IOException {
        Experiment ELFCExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 30);
        DegreeDistribution ELFCdegreeDistribution = new LTDegreeDistribution(0.01, 0.05, 5000);
        LTSimulator ELFCSimulator = new LTSimulator(
                new SpaceHelper(ELFCExperiment, ELFCdegreeDistribution, NodeTypeEnum.LT));
        double count = 0;
        Map<Integer, Node> ELFCNodes = ELFCSimulator.getNodes();
        for (Integer index : ELFCNodes.keySet()) {
            if (ELFCNodes.get(index).getPackList().size() != 0) count++;
        }

        // ����ʹ��1.5����ԭʼ���ݰ����۲�ELFC�Ĵ洢���
        storageOut.write(Config.WALK_LENGTH + " " + count + "\r\n");
        storageOut.flush();
    }

    public static void main(String[] args) throws IOException {
        emptyStorageExp();
    }

}
