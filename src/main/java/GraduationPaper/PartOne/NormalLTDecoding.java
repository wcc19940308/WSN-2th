package GraduationPaper.PartOne;

import ExperimentCode.DegreeDistribution;
import ExperimentCode.LTDegreeDistribution;
import ExperimentCode.Utils;
import com.google.common.collect.Range;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 实验一下：
 * 随机从5000个源数据中，根据度分布函数的d进行编码，总共生成10000个编码数据包，看是否可以成功解码
 * 一般需要6446个节点就可以成功解码
 */
public class NormalLTDecoding {
    public static void main(String[] args) throws IOException {
        int oneDegreeNodeCount = 0;
        DegreeDistribution degreeDistribution = new LTDegreeDistribution(0.2, 0.05, 5000);
        // 度分布区间
        Map<Double, Range<Double>> map = degreeDistribution.getProbInterval();
        // 包含度 + 数据信息的需要进行解码的数据包集合
        Set<Integer> oneDegreeData = new TreeSet<>();
        Set<List<Integer>> highDegreeData = new TreeSet<>(new Comparator<List<Integer>>() {
            @Override
            public int compare(List<Integer> o1, List<Integer> o2) {
                if (o1.size() == o2.size()) {
                    return o1.hashCode() - o2.hashCode();
                } else {
                    return o1.size() - o2.size();
                }
            }
        });

        /**
         * 一共1w个编码数据包，每个数据包都是根据度分布函数随机产生一个度为d的数据包
         * 然后每个度为d的数据包都要从5000个源数据中随机选择d个来进行异或编码
          */
        for (int i = 0; i < 10000; i++) {
            if (oneDegreeData.size() == 5000) {
                System.out.println("恢复5000个节点需要" + (i + 1) + "个编码数据包");
                break;
            }
            double random = Math.random() * 100;
            for (Double index : map.keySet()) {
                // 已经设定了度d,大小为Index
                if (map.get(index).contains(random)) {
                    int degree = index.intValue();
                    int[] dataList = Utils.getRandoms(1, 5000, degree);
                    // 如果是1度的数据包，直接解码
                    if (degree == 1) {
                        oneDegreeData.add(dataList[0]);
                        oneDegreeNodeCount++;
                    } else if (degree > 1) {
                        List<Integer> list = Arrays.stream(dataList).boxed().collect(Collectors.toList());
                        highDegreeData.add(list);
                    }
                    decodeHighDegreeData(oneDegreeData, highDegreeData);
                }
            }
        }
        System.out.println("1度的节点由" + oneDegreeNodeCount + "个");
        System.out.println("成功恢复了" + oneDegreeData.size() + "个源数据");
    }

    // 解码那些待解的高度的数据包
    public static void decodeHighDegreeData(Set<Integer> oneDegreeData, Set<List<Integer>> highDegreeData) {
        for (List<Integer> dataList : highDegreeData) {
            int count = 0;
            int length = dataList.size();
            Iterator<Integer> dataIterator = dataList.iterator();
            while (dataIterator.hasNext()) {
                Integer next = dataIterator.next();
                if (oneDegreeData.contains(next)) {
                    dataIterator.remove();
                    count++;
                }
            }
            if (count == length - 1) {
                oneDegreeData.add(dataList.get(0));
            }
        }
    }
}
