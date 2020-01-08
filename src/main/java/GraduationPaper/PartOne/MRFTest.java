package GraduationPaper.PartOne;

import ExperimentCode.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;

import static java.lang.System.out;

public class MRFTest {
    public static void main(String[] args) throws Exception {
        File file = new File("G:/lab/LabData/MRF≤‚ ‘.txt");
        file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        for (int i = 0; i < 20; i++) {
            init(out);
            Config.WALK_LENGTH += 50;
        }
        out.flush();
        out.close();

    }

    public static void init(BufferedWriter out) throws Exception {
        Experiment ELFCExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 30);

        DegreeDistribution ELFCdegreeDistribution = new LTDegreeDistribution(0.01,0.05,5000);
        LTSimulator ELFCSimulator = new LTSimulator(
                new SpaceHelper(ELFCExperiment, ELFCdegreeDistribution, NodeTypeEnum.NORMAL_BY_LAYER_LT));

        Sink ELFCSink = new Sink(ELFCSimulator);

        boolean ELFC = ELFCSink.collectPackage(NodeTypeEnum.NORMAL_BY_LAYER_LT);

        int codeNum = 5000;
        int ELFCSize = ELFCSink.getOneDegreeData().size();


        NumberFormat instance = NumberFormat.getInstance();
        instance.setMaximumFractionDigits(2);

        String ELFCResult = instance.format((float) ELFCSize / (float) codeNum);

        out.write(Config.WALK_LENGTH + " " + ELFCResult + " " + "\r\n");
        out.flush();
    }
}
