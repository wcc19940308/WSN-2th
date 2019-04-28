package DegreeDistributionOptimization;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AverageDegreeOfNewDistribution {

    public static void init(BufferedWriter out) throws IOException {
        double c = 0.01; // ϵ��c
        double �� = 0.05; // ϵ����
        double k = 5000; // ��֪����k
        double a1 = 0.38920795976855915, b1 = 0.4108600993677495;
        double a2 =0.784499337781702, b2 = 0.2603566604258498;
        double a3 = 1.7128947521680336, b3 = 0.011768445451988518;
        double a4 = 0.8013792412144722, b4 = 0.0657077185758227;
        double a5 = 0.3503100729837485, b5 = 0.04592954906681446;
        double R = c * Math.log(k / ��) * Math.sqrt(k);
        double LTAverageDegree = Math.log(k) + 1 + Math.log(R / ��);
        double NewAverageDegree1 = a1 * (1 - 1 / (Math.pow(2, k)) - k / (Math.pow(2, k + 1)))
                + b1 * (Math.log(k) + 1 + Math.log(R / ��));
        double NewAverageDegree2 = a2 * (1 - 1 / (Math.pow(2, k)) - k / (Math.pow(2, k + 1)))
                + b2 * (Math.log(k) + 1 + Math.log(R / ��));
        double NewAverageDegree3 = a3 * (1 - 1 / (Math.pow(2, k)) - k / (Math.pow(2, k + 1)))
                + b3 * (Math.log(k) + 1 + Math.log(R / ��));
        double NewAverageDegree4 = a4 * (1 - 1 / (Math.pow(2, k)) - k / (Math.pow(2, k + 1)))
                + b4 * (Math.log(k) + 1 + Math.log(R / ��));
        double NewAverageDegree5 = a5 * (1 - 1 / (Math.pow(2, k)) - k / (Math.pow(2, k + 1)))
                + b5 * (Math.log(k) + 1 + Math.log(R / ��));

        out.write("a=" + a1 + " b=" + b1 + " " + NewAverageDegree1 + " " + "\r\n");
        out.write("a=" + a2 + " b=" + b2 + " " + NewAverageDegree2 + " " + "\r\n");
        out.write("a=" + a3 + " b=" + b3 + " " + NewAverageDegree3 + " " + "\r\n");
        out.write("a=" + a4 + " b=" + b4 + " " + NewAverageDegree4 + " " + "\r\n");
        out.write("a=" + a5 + " b=" + b5 + " " + NewAverageDegree5 + " " + "\r\n");
        out.write("" + LTAverageDegree);
        out.flush();
}
    public static void main(String[] args) throws IOException {
        File file = new File("G:/lab/LabData/NewDistribution/6�ֶȷֲ�����ƽ�������.txt");
        file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        init(out);
    }
}
