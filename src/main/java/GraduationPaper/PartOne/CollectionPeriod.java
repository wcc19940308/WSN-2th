package GraduationPaper.PartOne;

import ExperimentCode.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

// 数据收集周期，LT主动访问节点和访问边缘节点的比较
public class CollectionPeriod {
    public static void main(String[] args) throws IOException {
        File file = new File("G:/lab/GraduationPaper/成功解码需要访问的节点数量多组重复.txt");
        file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 20; j++) {
                init(out);
            }
            Config.WALK_LENGTH += 50;
        }
        out.flush();
        out.close();
    }

    public static void init(BufferedWriter out) throws IOException{
        Experiment MRF_ELFCExp = new Experiment(1000, 1000, 0,
                5000, 10000, 30);

        Experiment MRF_LTExp = new Experiment(1000, 1000, 0,
                5000, 10000, 30);

        DegreeDistribution MRF_ELFCdegreeDistribution = new LTDegreeDistribution(0.01, 0.05, 5000);
        DegreeDistribution MRF_LTdegreeDistribution = new LTDegreeDistribution(0.01, 0.05, 5000);


        LTSimulator MRF_ELFCSimulator = new LTSimulator(
                new SpaceHelper(MRF_ELFCExp, MRF_ELFCdegreeDistribution, NodeTypeEnum.MRFELFC));
        LTSimulator MRF_LTSimulator = new LTSimulator(
                new SpaceHelper(MRF_LTExp, MRF_LTdegreeDistribution, NodeTypeEnum.MRF_LT));


        Sink MRF_ELFCSink = new Sink(MRF_ELFCSimulator);
        Sink MRF_LTSink = new Sink(MRF_LTSimulator);

        boolean MRF_ELFC = MRF_ELFCSink.collectPackage(NodeTypeEnum.MRFELFC);
        boolean MRF_LT = MRF_LTSink.collectPackage(NodeTypeEnum.MRF_LT);
        System.err.println(" MRFELFC:" + MRF_ELFC  + " MRF_LT:" + MRF_LT);


        System.out.println("---开始写文件啦---");

        out.write(Config.WALK_LENGTH + " "
                + MRF_LTSink.getAccessNodeCount() + " "
                + MRF_ELFCSink.getAccessNodeCount() + " "
                + "\r\n");

        out.flush();
    }
}
