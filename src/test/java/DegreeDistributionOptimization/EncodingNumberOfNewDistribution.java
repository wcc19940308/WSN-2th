package DegreeDistributionOptimization;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

// 新的度分布函数的编码符号数量和平均度的比较
public class EncodingNumberOfNewDistribution {

    public static void init(BufferedWriter out) throws IOException {
        double c = 0.01; // 系数c
        double δ = 0.05; // 系数δ
        double k = 5000; // 感知数量k
        double a1 = 0.38920795976855915, b1 = 0.4108600993677495;
        double a2 =0.784499337781702, b2 = 0.2603566604258498;
        double a3 = 1.7128947521680336, b3 = 0.011768445451988518;
        double a4 = 0.8013792412144722, b4 = 0.0657077185758227;
        double a5 = 0.3503100729837485, b5 = 0.04592954906681446;
        double R = c * Math.log(k / δ) * Math.sqrt(k);
        double LTEncodingNumber = k + c * Math.sqrt(k) * Math.log(k / δ) * Math.log(k / δ);
        double NewAverageDegree1 = a1 * k * (1 - 1 / (Math.pow(2, k))) + b1 * (k + c * Math.sqrt(k) * Math.log(k / δ) * Math.log(k / δ));
        double NewAverageDegree2 = a2 * k * (1 - 1 / (Math.pow(2, k))) + b2 * (k + c * Math.sqrt(k) * Math.log(k / δ) * Math.log(k / δ));
        double NewAverageDegree3 = a3 * k * (1 - 1 / (Math.pow(2, k))) + b3 * (k + c * Math.sqrt(k) * Math.log(k / δ) * Math.log(k / δ));
        double NewAverageDegree4 = a4 * k * (1 - 1 / (Math.pow(2, k))) + b4 * (k + c * Math.sqrt(k) * Math.log(k / δ) * Math.log(k / δ));
        double NewAverageDegree5 = a5 * k * (1 - 1 / (Math.pow(2, k))) + b5 * (k + c * Math.sqrt(k) * Math.log(k / δ) * Math.log(k / δ));

        out.write("a=" + a1 + " b=" + b1 + " " + NewAverageDegree1 + " " + "\r\n");
        out.write("a=" + a2 + " b=" + b2 + " " + NewAverageDegree2 + " " + "\r\n");
        out.write("a=" + a3 + " b=" + b3 + " " + NewAverageDegree3 + " " + "\r\n");
        out.write("a=" + a4 + " b=" + b4 + " " + NewAverageDegree4 + " " + "\r\n");
        out.write("a=" + a5 + " b=" + b5 + " " + NewAverageDegree5 + " " + "\r\n");
        out.write("" + LTEncodingNumber);
        out.flush();
    }
    public static void main(String[] args) throws IOException {
        File file = new File("G:/lab/LabData/NewDistribution/6种度分布函数所需编码符号数量.txt");
        file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        init(out);
    }
}
