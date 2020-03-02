//package GraduationPaper.PartTwo;
//
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.*;
//
//
//// 就约定条件Pm(2) < 0.5 ， Pm(k/R) < Pμ(k/R) 得出的结果：0<a+b<2 , 0<b<1 进行500W次实验得出的平均度
//// 舍弃一些不合理的值，对剩余的值进行数据恢复情况的判断
//public class GenerateXAndY {
//    static double a;
//    static double b;
//    static double minD = Double.MAX_VALUE;
//    // 编码复杂度从高到低进行排序
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
//        double c = 0.01; // 系数c
//        double δ = 0.05; // 系数δ
//        double k = 5000; // 感知数量k
//        double R = c * Math.log(k / δ) * Math.sqrt(k);
//        //a = 1.77;
//        //b = 0.2;
//        double LTAverageDegree = Math.log(k) + 1 + Math.log(R / δ);
//        double NewAverageDegree = 0;
//        for (int i = 0; i < 100000000; i++) {
//            generateAandB();
//            NewAverageDegree = a * (1 - 1 / (Math.pow(2, k)) - k / (Math.pow(2, k + 1)))
//                    + b * (Math.log(k) + 1 + Math.log(R / δ));
//            //minD = Math.min(minD, NewAverageDegree);
//            if (NewAverageDegree < minD) {
//                List<Double> list = new ArrayList<>();
//                list.add(a);
//                list.add(b);
//                map.put(NewAverageDegree, list);
//                minD = NewAverageDegree;
//            }
//        }
//        File file = new File("G:/lab/GraduationPaper/PartTwo/新的度分布函数度分布情况11.txt");
//        file.createNewFile();
//        BufferedWriter out = new BufferedWriter(new FileWriter(file));
//        for (Map.Entry<Double, List<Double>> entry : map.entrySet()) {
//            // 平均编码度要大于等于1
//            if (entry.getKey() >= 2) {
//                System.err.println("key=" + entry.getKey() + " value=" + entry.getValue());
//                out.write("key=" + entry.getKey() + " value=" + entry.getValue() + "\r\n");
//            }
//        }
//        out.flush();
//    }
//}
