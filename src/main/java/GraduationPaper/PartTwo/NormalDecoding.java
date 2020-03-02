package GraduationPaper.PartTwo;

import ExperimentCode.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

// 新的度分布函数正常解码
public class NormalDecoding {

    public static void activeCollection(BufferedWriter out) throws IOException {
        Experiment MRF_LTExp = new Experiment(1000, 1000, 0,
                5000, 10000, 30);
        DegreeDistribution MRF_LTdegreeDistribution = new LTDegreeDistribution(0.01, 0.05, 5000);
        LTSimulator MRF_LTSimulator = new LTSimulator(
                new SpaceHelper(MRF_LTExp, MRF_LTdegreeDistribution, NodeTypeEnum.MRF_LT));
        Sink MRF_LTSink = new Sink(MRF_LTSimulator);
        boolean MRF_LT = MRF_LTSink.collectPackage(NodeTypeEnum.MRF_LT);

        double x = 9.99989814e-01, y = 1.00000002e-05;
        Experiment ERSDExp = new Experiment(1000, 1000, 0,
                5000, 10000, 30);
        DegreeDistribution ERSDdegreeDistribution = new NewDegreeDistribution(0.01, 0.05, 5000, x, y);
        LTSimulator ERSDSimulator = new LTSimulator(
                new SpaceHelper(ERSDExp, ERSDdegreeDistribution, NodeTypeEnum.MRF_LT));
        Sink ERSDSink = new Sink(ERSDSimulator);
        boolean ERSD = ERSDSink.collectPackage(NodeTypeEnum.MRF_LT);

        System.err.println("RSD:" + MRF_LT + " ERSD:" + ERSD);

        System.out.println("---开始写文件啦---");
        Map<Integer, Integer> MRF_LT_Map = MRF_LTSink.getDecodingRatio();
        Map<Integer, Integer> ERSD_Map = ERSDSink.getDecodingRatio();
        for (Integer index : MRF_LT_Map.keySet()) {
            Integer MRF_LT_Map_num = MRF_LT_Map.get(index);
            Integer ERSD_num = ERSD_Map.get(index);
            out.write(index + " "
                    + (MRF_LT_Map_num == null ? 5000 : MRF_LT_Map_num) + " "
                    + (ERSD_num == null ? 5000 : ERSD_num) +
                    "\r\n");
        }
        out.flush();
    }
    public static void main(String[] args) throws IOException {
        File file = new File("G:/lab/GraduationPaper/PartTwo/RSD和ERSD使用主动收集解码恢复情况.txt");
        file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        for (int i = 0; i < 20; i++) {
            activeCollection(out);
        }
        out.close();
    }
}
