package GraduationPaper.PartTwo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AverageDegreeOfNewDistribution {
    public static void init(BufferedWriter out) throws IOException {
        double c = 0.01; // 系数c
        double δ = 0.05; // 系数δ
        double k = 5000; // 感知数量k

        double x = 9.99989814e-01, y = 1.00000002e-05;

        double R = c * Math.log(k / δ) * Math.sqrt(k);
        double LTAverageDegree = Math.log(k) + 1 + Math.log(R / δ);
        double NewAverageDegree1 = x * (1 - 1 / (Math.pow(2, k)) - k / (Math.pow(2, k + 1)))
                + y * (Math.log(k) + 1 + Math.log(R / δ));


//
//        out.write("a=" + a1 + " b=" + b1 + " " + NewAverageDegree1 + " " + "\r\n");
//        out.write("a=" + a2 + " b=" + b2 + " " + NewAverageDegree2 + " " + "\r\n");
//        out.write("a=" + a3 + " b=" + b3 + " " + NewAverageDegree3 + " " + "\r\n");

        out.write("" + LTAverageDegree);
        out.flush();
    }
    public static void main(String[] args) throws IOException {
        File file = new File("G:/lab/GraduationPaper/PartTwo/3种度分布函数平均编码度.txt");
        file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        init(out);
    }
}
