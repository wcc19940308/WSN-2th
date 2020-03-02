//package GraduationPaper.PartTwo;
//
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.*;
//
//
//// ��Լ������Pm(2) < 0.5 �� Pm(k/R) < P��(k/R) �ó��Ľ����0<a+b<2 , 0<b<1 ����500W��ʵ��ó���ƽ����
//// ����һЩ�������ֵ����ʣ���ֵ�������ݻָ�������ж�
//public class GenerateXAndY {
//    static double a;
//    static double b;
//    static double minD = Double.MAX_VALUE;
//    // ���븴�ӶȴӸߵ��ͽ�������
//    static Map<Double, List<Double>> map = new TreeMap<>(new Comparator<Double>() {
//        @Override
//        public int compare(Double o1, Double o2) {
//            return o2.compareTo(o1);
//        }
//    });
//    public static void generateAandB() {
//        b = Math.random();
//        while (b == 0) {
//            b = Math.random();
//        }
//        a = Math.random() * 2;
//        while (!(a + b < 2)) {
//            a = Math.random();
//        }
////        System.out.println("a=" + a + ", b=" + b);
//    }
//
//    public static void main(String[] args) throws IOException {
//        double c = 0.01; // ϵ��c
//        double �� = 0.05; // ϵ����
//        double k = 5000; // ��֪����k
//        double R = c * Math.log(k / ��) * Math.sqrt(k);
//        //a = 1.77;
//        //b = 0.2;
//        double LTAverageDegree = Math.log(k) + 1 + Math.log(R / ��);
//        double NewAverageDegree = 0;
//        for (int i = 0; i < 100000000; i++) {
//            generateAandB();
//            NewAverageDegree = a * (1 - 1 / (Math.pow(2, k)) - k / (Math.pow(2, k + 1)))
//                    + b * (Math.log(k) + 1 + Math.log(R / ��));
//            //minD = Math.min(minD, NewAverageDegree);
//            if (NewAverageDegree < minD) {
//                List<Double> list = new ArrayList<>();
//                list.add(a);
//                list.add(b);
//                map.put(NewAverageDegree, list);
//                minD = NewAverageDegree;
//            }
//        }
//        File file = new File("G:/lab/GraduationPaper/PartTwo/�µĶȷֲ������ȷֲ����11.txt");
//        file.createNewFile();
//        BufferedWriter out = new BufferedWriter(new FileWriter(file));
//        for (Map.Entry<Double, List<Double>> entry : map.entrySet()) {
//            // ƽ�������Ҫ���ڵ���1
//            if (entry.getKey() >= 2) {
//                System.err.println("key=" + entry.getKey() + " value=" + entry.getValue());
//                out.write("key=" + entry.getKey() + " value=" + entry.getValue() + "\r\n");
//            }
//        }
//        out.flush();
//    }
//}
