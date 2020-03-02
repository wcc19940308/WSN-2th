package GraduationPaper.PartTwo;

import ExperimentCode.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;

// 三种收集方式的随机游走比率
public class RandomWalkRatio {
    public static void activeCollection(BufferedWriter out) throws IOException {
        double x = 9.99989814e-01, y = 1.00000002e-05;
        Experiment LTExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 30);
        Experiment ERSDExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 30);
        DegreeDistribution LTdegreeDistribution = new LTDegreeDistribution(0.01,0.05,5000);
        DegreeDistribution ERSDdegreeDistribution = new NewDegreeDistribution(0.01, 0.05, 5000, x, y);


        LTSimulator LTSimulator = new LTSimulator(
                new SpaceHelper(LTExperiment, LTdegreeDistribution, NodeTypeEnum.MRF_LT));
        LTSimulator ERSDSimulator = new LTSimulator(
                new SpaceHelper(LTExperiment, ERSDdegreeDistribution, NodeTypeEnum.MRF_LT));

        double LTIntialWalkLength = LTSimulator.walkLength;
        double ERSDIntialWalkLength = ERSDSimulator.walkLength;

        double LTRedudancy = LTSimulator.REDUNDANCY_WALK_LENGTH;
        double ERSDRedudancy = ERSDSimulator.REDUNDANCY_WALK_LENGTH;

        double LTAll = LTIntialWalkLength + LTRedudancy;
        double ERSDAll = ERSDIntialWalkLength + ERSDRedudancy;

        System.err.println("RSD:" + LTAll / LTIntialWalkLength);
        System.err.println("ERSD:" + ERSDAll / ERSDIntialWalkLength);

        System.out.println("---开始写文件啦---");

        int codeNum = 5000;
        NumberFormat instance = NumberFormat.getInstance();
        instance.setMaximumFractionDigits(2);

        String LTResult = instance.format((float) LTAll / (float) LTIntialWalkLength);
        String ERSDResult = instance.format((float) ERSDAll / (float) ERSDIntialWalkLength);

        // LT ELFC OELFC的所需步长
        out.write(Config.WALK_LENGTH + " " + LTResult + " " + ERSDResult + "\r\n");
        out.flush();
    }

//
    public static void main(String[] args) throws IOException {
        File file = new File("G:/lab/GraduationPaper/PartTwo/ERSD使用主动收集方式随机游走比率.txt");
        file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        Config.WALK_LENGTH = 50;
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
            activeCollection(out);
            }
            Config.WALK_LENGTH += 50;
        }
        out.flush();
        out.close();
//
//        File file2 = new File("G:/lab/GraduationPaper/PartTwo/ERSD使用ELFC集方式随机游走比率.txt");
//        file2.createNewFile();
//        BufferedWriter out2 = new BufferedWriter(new FileWriter(file2));
//        Config.WALK_LENGTH = 50;
//        for (int i = 0; i < 20; i++) {
//            for (int j = 0; j < 20; j++) {
//            ELFCCollection(out2);
//            }
//            Config.WALK_LENGTH += 50;
//        }
//        out2.flush();
//        out2.close();
//
//        File file3 = new File("G:/lab/GraduationPaper/PartTwo/ERSD使用OELFC收集方式随机游走比率.txt");
//        file3.createNewFile();
//        BufferedWriter out3 = new BufferedWriter(new FileWriter(file3));
//        Config.WALK_LENGTH = 50;
//        for (int i = 0; i < 20; i++) {
//            for (int j = 0; j < 20; j++) {
//            OELFCCollection(out3);
//            }
//            Config.WALK_LENGTH += 50;
//        }
//        out3.flush();
//        out3.close();
    }

//    public static void ELFCCollection(BufferedWriter out) throws IOException {
//        double x = 9.99989814e-01, y = 1.00000002e-05;
//        Experiment LTExperiment = new Experiment(1000, 1000, 0,
//                5000, 10000, 30);
//        Experiment ERSDExperiment = new Experiment(1000, 1000, 0,
//                5000, 10000, 30);
//        DegreeDistribution LTdegreeDistribution = new LTDegreeDistribution(0.01,0.05,5000);
//        DegreeDistribution ERSDdegreeDistribution = new NewDegreeDistribution(0.01, 0.05, 5000, x, y);
//
//
//        LTSimulator LTSimulator = new LTSimulator(
//                new SpaceHelper(LTExperiment, LTdegreeDistribution, NodeTypeEnum.MRF_LT));
//        LTSimulator ERSDSimulator = new LTSimulator(
//                new SpaceHelper(LTExperiment, ERSDdegreeDistribution, NodeTypeEnum.MRF_LT));
//
//        double LTIntialWalkLength = LTSimulator.walkLength;
//        double ERSDIntialWalkLength = ERSDSimulator.walkLength;
//
//        double LTRedudancy = LTSimulator.REDUNDANCY_WALK_LENGTH;
//        double ERSDRedudancy = ERSDSimulator.REDUNDANCY_WALK_LENGTH;
//
//        double LTAll = LTIntialWalkLength + LTRedudancy;
//        double ERSDAll = ERSDIntialWalkLength + ERSDRedudancy;
//
//        System.err.println("RSD:" + LTAll / LTIntialWalkLength);
//        System.err.println("ERSD:" + ERSDAll / ERSDIntialWalkLength);
//
//        System.out.println("---开始写文件啦---");
//
//        int codeNum = 5000;
//        NumberFormat instance = NumberFormat.getInstance();
//        instance.setMaximumFractionDigits(2);
//
//        String LTResult = instance.format((float) LTAll / (float) LTIntialWalkLength);
//        String ERSDResult = instance.format((float) ERSDAll / (float) ERSDIntialWalkLength);
//
//        // LT ELFC OELFC的所需步长
//        out.write(Config.WALK_LENGTH + " " + LTResult + ERSDResult + "\r\n");
//        out.flush();
//    }
//
//    public static void OELFCCollection(BufferedWriter out) throws IOException {
//        double x = 9.99989814e-01, y = 1.00000002e-05;
//        Experiment LTExperiment = new Experiment(1000, 1000, 0,
//                5000, 10000, 30);
//        Experiment ERSDExperiment = new Experiment(1000, 1000, 0,
//                5000, 10000, 30);
//        DegreeDistribution LTdegreeDistribution = new LTDegreeDistribution(0.01,0.05,5000);
//        DegreeDistribution ERSDdegreeDistribution = new NewDegreeDistribution(0.01, 0.05, 5000, x, y);
//
//
//        LTSimulator LTSimulator = new LTSimulator(
//                new SpaceHelper(LTExperiment, LTdegreeDistribution, NodeTypeEnum.MRF_LT));
//        LTSimulator ERSDSimulator = new LTSimulator(
//                new SpaceHelper(LTExperiment, ERSDdegreeDistribution, NodeTypeEnum.MRF_LT));
//
//        double LTIntialWalkLength = LTSimulator.walkLength;
//        double ERSDIntialWalkLength = ERSDSimulator.walkLength;
//
//        double LTRedudancy = LTSimulator.REDUNDANCY_WALK_LENGTH;
//        double ERSDRedudancy = ERSDSimulator.REDUNDANCY_WALK_LENGTH;
//
//        double LTAll = LTIntialWalkLength + LTRedudancy;
//        double ERSDAll = ERSDIntialWalkLength + ERSDRedudancy;
//
//        System.err.println("RSD:" + LTAll / LTIntialWalkLength);
//        System.err.println("ERSD:" + ERSDAll / ERSDIntialWalkLength);
//
//        System.out.println("---开始写文件啦---");
//
//        int codeNum = 5000;
//        NumberFormat instance = NumberFormat.getInstance();
//        instance.setMaximumFractionDigits(2);
//
//        String LTResult = instance.format((float) LTAll / (float) LTIntialWalkLength);
//        String ERSDResult = instance.format((float) ERSDAll / (float) ERSDIntialWalkLength);
//
//        // LT ELFC OELFC的所需步长
//        out.write(Config.WALK_LENGTH + " " + LTResult + ERSDResult + "\r\n");
//        out.flush();
//    }

    }
