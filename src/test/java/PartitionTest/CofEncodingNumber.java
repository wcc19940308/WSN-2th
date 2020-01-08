package PartitionTest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CofEncodingNumber {
    public static void init(double cofficient_of_c, BufferedWriter out) throws IOException {
        double k = 5000;
        double δ = 0.05;
        double M = 3;
        double LTNumber = k + cofficient_of_c * Math.sqrt(k) * Math.log(k / δ)* Math.log(k / δ);
        double PartitionLTNumber = k + cofficient_of_c * Math.sqrt(M * k) * Math.log(k / (M*δ)) * Math.log(k / (M*δ));
        out.write(cofficient_of_c + " " + LTNumber + " " + PartitionLTNumber + " " + "\r\n");
        out.flush();
    }

    public static void main(String[] args) throws IOException {
        File file = new File("G:/lab/LabData/半径30的分区数3的c从0.01到0.5的LT和分区所需编码符号数量对比.txt");
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
