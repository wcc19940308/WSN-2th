package GraduationPaper.PartOne;

import ExperimentCode.DegreeDistribution;
import ExperimentCode.LTDegreeDistribution;
import ExperimentCode.Utils;
import com.google.common.collect.Range;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ʵ��һ�£�
 * �����5000��Դ�����У����ݶȷֲ�������d���б��룬�ܹ�����10000���������ݰ������Ƿ���Գɹ�����
 * һ����Ҫ6446���ڵ�Ϳ��Գɹ�����
 */
public class NormalLTDecoding {
    public static void main(String[] args) throws IOException {
        int oneDegreeNodeCount = 0;
        DegreeDistribution degreeDistribution = new LTDegreeDistribution(0.2, 0.05, 5000);
        // �ȷֲ�����
        Map<Double, Range<Double>> map = degreeDistribution.getProbInterval();
        // ������ + ������Ϣ����Ҫ���н�������ݰ�����
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
         * һ��1w���������ݰ���ÿ�����ݰ����Ǹ��ݶȷֲ������������һ����Ϊd�����ݰ�
         * Ȼ��ÿ����Ϊd�����ݰ���Ҫ��5000��Դ���������ѡ��d��������������
          */
        for (int i = 0; i < 10000; i++) {
            if (oneDegreeData.size() == 5000) {
                System.out.println("�ָ�5000���ڵ���Ҫ" + (i + 1) + "���������ݰ�");
                break;
            }
            double random = Math.random() * 100;
            for (Double index : map.keySet()) {
                // �Ѿ��趨�˶�d,��СΪIndex
                if (map.get(index).contains(random)) {
                    int degree = index.intValue();
                    int[] dataList = Utils.getRandoms(1, 5000, degree);
                    // �����1�ȵ����ݰ���ֱ�ӽ���
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
        System.out.println("1�ȵĽڵ���" + oneDegreeNodeCount + "��");
        System.out.println("�ɹ��ָ���" + oneDegreeData.size() + "��Դ����");
    }

    // ������Щ����ĸ߶ȵ����ݰ�
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
