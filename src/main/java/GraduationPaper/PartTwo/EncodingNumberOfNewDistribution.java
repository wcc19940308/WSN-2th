package GraduationPaper.PartTwo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EncodingNumberOfNewDistribution {
    public static void init(BufferedWriter out) throws IOException {
        double c = 0.01; // 系数c
        double δ = 0.05; // 系数δ
        double k = 5000; // 感知数量k
        double a1 = 9.99989814e-01, b1 = 1.00000002e-05;

        double R = c * Math.log(k / δ) * Math.sqrt(k);
        double LTEncodingNumber = k + c * Math.sqrt(k) * Math.log(k / δ) * Math.log(k / δ);
        double NewAverageDegree1 = a1 * k * (1 - 1 / (Math.pow(2, k))) + b1 * (k + c * Math.sqrt(k) * Math.log(k / δ) * Math.log(k / δ));

        System.err.println(R);
        System.err.println(LTEncodingNumber);
        System.err.println(NewAverageDegree1);

        out.write("a=" + a1 + " b=" + b1 + " " + NewAverageDegree1 + " " + "\r\n");
        out.write("" + LTEncodingNumber);
        out.flush();
    }
    public static void main(String[] args) throws IOException {
        File file = new File("G:/lab/GraduationPaper/PartTwo/ERSD所需编码符号数量.txt");
        file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        init(out);
    }
}
