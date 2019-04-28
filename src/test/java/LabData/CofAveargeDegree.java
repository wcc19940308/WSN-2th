package LabData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CofAveargeDegree {
    public static void init(double cofficient_of_c, BufferedWriter out) throws IOException {
        double k = 5000;
        double δ = 0.05;
        double M = 3;
        double R =  cofficient_of_c * Math.log(k / δ) * Math.sqrt(k);
        double R2 = cofficient_of_c * Math.log(k / (M * δ)) * Math.sqrt(k / M);
        double LTAverageDegree = Math.log(k) + 1 + Math.log(R / δ);
        double PartitionLTAverageDegree = Math.log(k / M) + 1 + Math.log(R2 / δ);
        out.write(cofficient_of_c + " " + LTAverageDegree + " " + PartitionLTAverageDegree + " " + "\r\n");
        out.flush();
    }

    public static void main(String[] args) throws IOException {
        File file = new File("G:/lab/LabData/半径30的分区数3的c从0.01到0.5的LT和分区平均编码度对比.txt");
        file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        double cofficient_of_c = 0;
        for (int i = 0; i <= 50; i++) {
            cofficient_of_c += 0.01;
            //for (int j = 0; j < 50; j++) {
                init(cofficient_of_c, out);
            //}
        }
        out.flush();
        out.close();
    }
}
