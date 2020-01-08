package GraduationPaper.PartOne;

import ExperimentCode.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

// EDFC�ڵ��Ԥ��Ⱥ���ʵ�ȵ����ƱȽ�ͼ
public class EDFCDegreeCompare {
    public static void main(String[] args) throws IOException {
        Experiment EDFCExperiment = new  Experiment(1000, 1000, 0,
                5000, 10000, 40);
        DegreeDistribution degreeDistribution = new LTDegreeDistribution(0.2, 0.05, 5000);
        LTSimulator EDFCSimulator = new LTSimulator(
                new SpaceHelper(EDFCExperiment, degreeDistribution, NodeTypeEnum.EDFC));
        Sink EDFCSink = new Sink(EDFCSimulator);
        EDFCSink.collectPackage(NodeTypeEnum.EDFC);
        Map<Integer, Node> nodes = EDFCSimulator.getNodes();
        // ÿ���ڵ���������ʵ��
        Map<Integer, Integer> idealDegree = new TreeMap<>();
        Map<Integer, Integer> realDegree = new TreeMap<>();
        for (Integer index : nodes.keySet()) {
            Integer ideal = nodes.get(index).getDegree();
            Integer real = nodes.get(index).getPackList().size();
            idealDegree.put(index, ideal);
            realDegree.put(index, real);
        }
        File file = new File("G:/lab/GraduationPaper/EDFC���������ʵ��.txt");
        StringBuffer str = new StringBuffer();
        FileWriter fw = new FileWriter("G:/lab/GraduationPaper/EDFC���������ʵ��.txt", true);
        for (Integer index : nodes.keySet()) {
            fw.write(index + " " + idealDegree.get(index) + " " + realDegree.get(index) + "\r\n");
            fw.flush();
        }
        fw.close();
    }
}
