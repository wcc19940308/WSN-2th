package DegreeDistributionOptimization;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

// �µĶȷֲ������Ķȷֲ����
public class NewDistribution {
    public static void init(double coefficient1, double coefficient2, BufferedWriter out) throws IOException {

        //out.write("a=" + a + " b=" + b + " " + LTAverageDegree + " " + NewAverageDegree + " " + "\r\n");
        out.flush();
    }
    public static void main(String[] args) throws IOException {
        File file = new File("G:/lab/LabData/NewDistribution/�µĶȷֲ������ȷֲ����.txt");
        file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        init(1, 0.5, out);
    }
}
