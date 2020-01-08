package GraduationPaper.PartTwo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AverageDegreeOfNewDistribution {
    public static void init(BufferedWriter out) throws IOException {
        double c = 0.01; // ϵ��c
        double �� = 0.05; // ϵ����
        double k = 5000; // ��֪����k

        double a1 =0.76, b1 = 0.85;
        double a2 = 0.14, b2 = 0.83;
        double a3 = 0.16, b3 = 0.15;

        double R = c * Math.log(k / ��) * Math.sqrt(k);
        double LTAverageDegree = Math.log(k) + 1 + Math.log(R / ��);
        double NewAverageDegree1 = a1 * (1 - 1 / (Math.pow(2, k)) - k / (Math.pow(2, k + 1)))
                + b1 * (Math.log(k) + 1 + Math.log(R / ��));
        double NewAverageDegree2 = a2 * (1 - 1 / (Math.pow(2, k)) - k / (Math.pow(2, k + 1)))
                + b2 * (Math.log(k) + 1 + Math.log(R / ��));
        double NewAverageDegree3 = a3 * (1 - 1 / (Math.pow(2, k)) - k / (Math.pow(2, k + 1)))
                + b3 * (Math.log(k) + 1 + Math.log(R / ��));


        out.write("a=" + a1 + " b=" + b1 + " " + NewAverageDegree1 + " " + "\r\n");
        out.write("a=" + a2 + " b=" + b2 + " " + NewAverageDegree2 + " " + "\r\n");
        out.write("a=" + a3 + " b=" + b3 + " " + NewAverageDegree3 + " " + "\r\n");

        out.write("" + LTAverageDegree);
        out.flush();
    }
    public static void main(String[] args) throws IOException {
        File file = new File("G:/lab/GraduationPaper/PartTwo/3�ֶȷֲ�����ƽ�������.txt");
        file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        init(out);
    }
}
