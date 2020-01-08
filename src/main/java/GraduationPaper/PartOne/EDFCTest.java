package GraduationPaper.PartOne;

import ExperimentCode.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;


// 对EDFC的不同冗余系数+随机游走步长进行实验
public class EDFCTest {
    public static void main(String[] args) throws IOException {
        File file = new File("G:/lab/GraduationPaper/单向随机游走策略MOW对步长和冗余包的变化.txt");
        file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        for (int i = 0; i < 20; i++) {
            Config.REDUNDANCY = 1;
            for (int j = 0; j < 15; j++) {
                init(out);
                Config.REDUNDANCY += 0.2;
            }
            Config.WALK_LENGTH += 50;
        }
        out.flush();
        out.close();
    }

    public static void init(BufferedWriter out) throws IOException {
        Experiment EDFCExperiment = new  Experiment(1000, 1000, 0,
                5000, 10000, 40);
        DegreeDistribution degreeDistribution = new LTDegreeDistribution(0.2, 0.05, 5000);
        LTSimulator EDFCSimulator = new LTSimulator(
                new SpaceHelper(EDFCExperiment, degreeDistribution, NodeTypeEnum.MOW_LT));
        Sink EDFCSink = new Sink(EDFCSimulator);
        EDFCSink.collectPackage(NodeTypeEnum.MOW_LT);

        int MOW_LTSize = EDFCSink.getOneDegreeData().size();
        NumberFormat instance = NumberFormat.getInstance();
        instance.setMaximumFractionDigits(2);
        int codeNum = 5000;
        String MOW_LTResult = instance.format((float) MOW_LTSize / (float) codeNum);
        out.write("步长：" + Config.WALK_LENGTH + " 冗余系数：" + Config.REDUNDANCY + " " + MOW_LTResult + "\r\n");
        out.flush();
    }
}
