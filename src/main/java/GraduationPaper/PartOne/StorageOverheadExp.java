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
 * 存储开销的实验，通过比较EDFC收到的编码数据包数量和1之间的比较
 * 1. x:节点上停留不同数据包的数量 y:节点需要的存储空间
 * 2. x: 随机游走的一次步长(因为ELFC的总步长比较难和EDFC的总步长找到一样的) y:网络整体节点的存储开销
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
        System.out.println("---开始写文件啦---");
        File file = new File("G:/lab/GraduationPaper/节点存储开销.txt");
        StringBuffer str = new StringBuffer();
        FileWriter fw = new FileWriter("G:/lab/GraduationPaper/节点存储开销.txt", true);

        for (Integer index : EDFCMap.keySet()) {
            str.append(index + " " + EDFCMap.get(index) + " "
                    + myMap.get(index)
                    + "\r\n");
        }
        fw.write(str.toString());
        fw.close();
    }

    // 两种策略的存储开销，以及总步长对比
    public static void twoStrategyCompare() throws IOException {
        File storageFile = new File("G:/lab/GraduationPaper/一次步长和存储开销的关系.txt");
        File walkLengthFile = new File("G:/lab/GraduationPaper/一次步长和总步长的关系.txt");

        storageFile.createNewFile();
        walkLengthFile.createNewFile();

        BufferedWriter storageOut = new BufferedWriter(new FileWriter(storageFile));
        BufferedWriter walkLengthOut = new BufferedWriter(new FileWriter(walkLengthFile));
        // 一次步长从50到1000设置，观察实验结果
        for (int i = 0; i < 20; i++) {
            // 这里是为了做平均10次
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
     * 网络中的数据包是由感知节点产生的，因此x: 网络整体的随机游走的步长开销 y:网络中整体的存储开销(对于EDFC就是数据包数量，对于我们的策略就是所有节点中
     * packList.size() != 0 的节点的数量)
     * 注意EDFC有额外1.4058的开销
     * <p>
     * 这里我们需要2个东西：
     * 1. EDFC和ELFC的关于一次步长的改变，存储开销的变化
     * 2. EDFC和ELFC的关于一次步长的改变，总步长的变化
     * 通过2，可以反映对于1来说，总步长的变化，二者关于存储开销的变化
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

        // EDFC和ELFC的总步长
        double EDFCWalkLength = EDFCSimulator.walkLength;
        double ELFCWalkLength = ELFCSimulator.walkLength
                + ELFCSimulator.REDUNDANCY_WALK_LENGTH;

        double count = 0;
        Map<Integer, Node> ELFCNodes = ELFCSimulator.getNodes();
        for (Integer index : ELFCNodes.keySet()) {
            if (ELFCNodes.get(index).getPackList().size() != 0) count++;
        }

        // 注意，对于EDFC的存储开销就是b * k * xd，即所有数据包的数量
        storageOut.write(Config.WALK_LENGTH + " " + (int) (EDFCWalkLength / Config.WALK_LENGTH) + " " + count + "\r\n");
        storageOut.flush();

        walkLengthOut.write(Config.WALK_LENGTH + " " + EDFCWalkLength + " " + ELFCWalkLength + "\r\n");
        walkLengthOut.flush();
    }

    // 二次转发策略空存储问题的实验
    public static void emptyStorageExp() throws IOException {
        File storageFile = new File("G:/lab/GraduationPaper/1.5倍编码数据包的二次转发策略存储开销情况.txt");
        storageFile.createNewFile();
        BufferedWriter storageOut = new BufferedWriter(new FileWriter(storageFile));
        // 一次步长从50到1000设置，观察实验结果
        for (int i = 0; i < 20; i++) {
            // 这里是为了做平均10次
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

        // 这里使用1.5倍的原始数据包，观察ELFC的存储情况
        storageOut.write(Config.WALK_LENGTH + " " + count + "\r\n");
        storageOut.flush();
    }

    public static void main(String[] args) throws IOException {
        emptyStorageExp();
    }

}
