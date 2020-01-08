package GraduationPaper.PartOne;

import ExperimentCode.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class SixStrategyNormalDecoding {
    public static void main(String[] args) throws IOException {
        Experiment MOW_ELFCExp = new Experiment(1000, 1000, 0,
                5000, 10000, 30);
        Experiment MOW_OELFCExp = new Experiment(1000, 1000, 0,
                5000, 10000, 30);
        Experiment MRF_ELFCExp = new Experiment(1000, 1000, 0,
                5000, 10000, 30);
        Experiment MRF_OELFCExp = new Experiment(1000, 1000, 0,
                5000, 10000, 30);
        Experiment MOW_LTExp = new Experiment(1000, 1000, 0,
                5000, 10000, 30);
        Experiment MRF_LTExp = new Experiment(1000, 1000, 0,
                5000, 10000, 30);

        DegreeDistribution MOW_ELFCdegreeDistribution = new LTDegreeDistribution(0.01,0.05,5000);
        DegreeDistribution MOW_OELFCdegreeDistribution = new LTDegreeDistribution(0.01,0.05,5000);
        DegreeDistribution MRF_ELFCdegreeDistribution = new LTDegreeDistribution(0.01, 0.05, 5000);
        DegreeDistribution MRF_OELFCdegreeDistribution = new LTDegreeDistribution(0.01, 0.05, 5000);
        DegreeDistribution MOW_LTdegreeDistribution = new LTDegreeDistribution(0.01,0.05,5000);
        DegreeDistribution MRF_LTdegreeDistribution = new LTDegreeDistribution(0.01, 0.05, 5000);


        LTSimulator MOW_ELFCSimulator = new LTSimulator(
                new SpaceHelper(MOW_ELFCExp, MOW_ELFCdegreeDistribution, NodeTypeEnum.MOWELFC));
        LTSimulator MOW_OELFCSimulator = new LTSimulator(
                new SpaceHelper(MOW_OELFCExp, MOW_OELFCdegreeDistribution, NodeTypeEnum.MOWOELFC));
        LTSimulator MRF_ELFCSimulator = new LTSimulator(
                new SpaceHelper(MRF_ELFCExp, MRF_ELFCdegreeDistribution, NodeTypeEnum.MRFELFC));
        LTSimulator MRF_OELFCSimulator = new LTSimulator(
                new SpaceHelper(MRF_OELFCExp, MRF_OELFCdegreeDistribution, NodeTypeEnum.MRFOELFC));
        LTSimulator MOW_LTSimulator = new LTSimulator(
                new SpaceHelper(MOW_LTExp, MOW_LTdegreeDistribution, NodeTypeEnum.MOW_LT));
        LTSimulator MRF_LTSimulator = new LTSimulator(
                new SpaceHelper(MRF_LTExp, MRF_LTdegreeDistribution, NodeTypeEnum.MRF_LT));

        Sink MOW_ELFCSink = new Sink(MOW_ELFCSimulator);
        Sink MOW_OELFCSink = new Sink(MOW_OELFCSimulator);
        Sink MRF_ELFCSink = new Sink(MRF_ELFCSimulator);
        Sink MRF_OELFCSink = new Sink(MRF_OELFCSimulator);
        Sink MOW_LTSink = new Sink(MOW_LTSimulator);
        Sink MRF_LTSink = new Sink(MRF_LTSimulator);

        boolean MOW_ELFC = MOW_ELFCSink.collectPackage(NodeTypeEnum.MOWELFC);
        boolean MOW_OELFC = MOW_OELFCSink.collectPackage(NodeTypeEnum.MOWOELFC);
        boolean MRF_ELFC = MRF_ELFCSink.collectPackage(NodeTypeEnum.MRFELFC);
        boolean MRF_OELFC = MRF_OELFCSink.collectPackage(NodeTypeEnum.MRFOELFC);
        boolean MOW_LT = MOW_LTSink.collectPackage(NodeTypeEnum.MOW_LT);
        boolean MRF_LT = MRF_LTSink.collectPackage(NodeTypeEnum.MRF_LT);
        System.err.println("MOWELFC:" + MOW_ELFC + " MOWOELFC:" + MOW_OELFC
                + " MRFELFC:" + MRF_ELFC + " MRFOELFC:" + MRF_OELFC + "MOW_LT:" + MOW_LT + " MRF_LT:" + MRF_LT);


        System.out.println("---开始写文件啦---");


        Map<Integer, Integer> MOW_ELFC_Map = MOW_ELFCSink.getDecodingRatio();
        Map<Integer, Integer> MOW_OELFC_Map = MOW_OELFCSink.getDecodingRatio();
        Map<Integer, Integer> MRF_ELFC_Map = MRF_ELFCSink.getDecodingRatio();
        Map<Integer, Integer> MRF_OELFC_Map = MRF_OELFCSink.getDecodingRatio();
        Map<Integer, Integer> MOW_LT_Map = MOW_LTSink.getDecodingRatio();
        Map<Integer, Integer> MRF_LT_Map = MRF_LTSink.getDecodingRatio();
        File file = new File("G:/lab/GraduationPaper/6种策略正常解码情况.txt");
        file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(file));

        for (Integer index : MOW_ELFC_Map.keySet()) {
            Integer MOW_ELFC_num = MOW_ELFC_Map.get(index);
            Integer MOW_OELFC_Map_num = MOW_OELFC_Map.get(index);
            Integer MRF_ELFC_Map_num = MRF_ELFC_Map.get(index);
            Integer MRF_OELFC_Map_num = MRF_OELFC_Map.get(index);
            Integer MOW_LT_Map_num = MOW_LT_Map.get(index);
            Integer MRF_LT_Map_num = MRF_LT_Map.get(index);
            out.write(index + " "
                    + (MOW_ELFC_num == null ? 5000 : MOW_ELFC_num) + " "
                    + (MOW_OELFC_Map_num == null ? 5000 : MOW_OELFC_Map_num)  + " "
                    + (MRF_ELFC_Map_num == null ? 5000 : MRF_ELFC_Map_num)  +" "
                    + (MRF_OELFC_Map_num == null ? 5000 : MRF_OELFC_Map_num)  + " "
                    + (MOW_LT_Map_num == null ? 5000 : MOW_LT_Map_num) + " "
                    + (MRF_LT_Map_num == null ? 5000 : MRF_LT_Map_num)  +
                    "\r\n");
        }
        out.flush();
        out.close();
    }

}
