package LabData;

import ExperimentCode.*;
import org.junit.Test;

import java.io.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CollectiData {
    public static void main(String[] args) throws IOException {
        Experiment normalExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 40);
        DegreeDistribution degreeDistribution = new LTDegreeDistribution(0.2, 0.05, 5000);
        LTSimulator normalLTSimulator = new LTSimulator(
                new SpaceHelper(normalExperiment, degreeDistribution, NodeTypeEnum.LT));
        Sink normalSink = new Sink(normalLTSimulator);
        normalSink.collectPackage(NodeTypeEnum.LT);
        Map<Integer, Integer> map = normalSink.getDecodingRatio();

        System.out.println("---开始写文件啦---");


        File file = new File("G:/lab/LabData/demo.txt");
        StringBuffer str = new StringBuffer();
        FileWriter fw = new FileWriter("G:/lab/LabData/demo.txt", true);
        Set set = map.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            str.append(entry.getKey() + " " + entry.getValue() + "\r\n");
        }
        fw.write(str.toString());
        fw.close();
    }

    @Test
    public void test() throws IOException {
        String s = "123";
        FileWriter file = new FileWriter("G:/lab/LabData/demo.txt", true);
        file.write(s);
        file.close();
    }
}
