package DegreeDistributionOptimization;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

// 新的度分布函数的度分布情况
public class NewDistribution {
    public static void init(double coefficient1, double coefficient2, BufferedWriter out) throws IOException {

        //out.write("a=" + a + " b=" + b + " " + LTAverageDegree + " " + NewAverageDegree + " " + "\r\n");
        out.flush();
    }
    public static void main(String[] args) throws IOException {
        File file = new File("G:/lab/LabData/NewDistribution/新的度分布函数度分布情况.txt");
        file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        init(1, 0.5, out);
    }
}
