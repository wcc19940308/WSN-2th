import ExperimentCode.*;
import jdk.nashorn.internal.runtime.regexp.joni.constants.NodeType;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class MixCodingSimulatorTest {

    public static double cofficient_of_c = 0.5;
    //public static int walk_length = Config.WALK_LENGTH;

    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        // c从0.01到0.5，各自对应的分区数量为1-8的4组实验对象的数据
        for (int j = 1; j <= 50; j++) {
            cofficient_of_c = cofficient_of_c + 0.01;
            Config.PARTITION_NUM = 0;
            for (int i = 1; i <= 8; i++) {
                init(++Config.PARTITION_NUM);
            }
        }

        // 分区数量为3，且c从0.01到0.5，随机游走步长为500-2000的实验效果
//        for (int j = 1; j <= 50; j++) {
//            cofficient_of_c = cofficient_of_c + 0.01;
//            Config.WALK_LENGTH = 400;
//            for (int i = 1; i <= 16; i++) {
//                Config.WALK_LENGTH = Config.WALK_LENGTH + 100;
//                init(Config.PARTITION_NUM);
//            }
//        }

        long end = System.currentTimeMillis();
        System.out.println("一次实验需要:"+(end-start)/1000+" s");
    }

    public static void init(int partitionNum) throws IOException {
//            Experiment normalExperiment = new Experiment(1000, 1000, 0,
//                    5000, 10000, 40);
//
//            Experiment regionExperiment = new Experiment(1000, 1000, 0,
//                    5000, 10000, 40);

//        Experiment normalExperiment = new Experiment(4,4,0,
//                8,16,2);
//        Experiment regionExperiment = new Experiment(4,4,0,
//                8,16,2);

        Experiment normalExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 40);

        Experiment regionExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 40);

        Experiment layerExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 40);

        Experiment layerAndRegionExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 40);


        DegreeDistribution degreeDistribution = new LTDegreeDistribution(cofficient_of_c, 0.05, 5000);
        DegreeDistribution regionDegreeDistribution = new LTDegreeDistribution(cofficient_of_c, 0.05, 5000/partitionNum);

//        DegreeDistribution degreeDistribution = new LTDegreeDistribution(0.01,0.05,8);
        //       DegreeDistribution regionDegreeDistribution = new LTDegreeDistribution(0.01, 0.05, 8/4);

        LTSimulator normalLTSimulator = new LTSimulator(
                new SpaceHelper(normalExperiment, degreeDistribution, NodeTypeEnum.LT));
        LTSimulator regionLTSimulator = new LTSimulator(
                new SpaceHelper(regionExperiment, regionDegreeDistribution, NodeTypeEnum.PARTITION_LT));
        LTSimulator layerLTSimulator = new LTSimulator(
                new SpaceHelper(layerExperiment, degreeDistribution, NodeTypeEnum.LAYER_LT));
        LTSimulator layerAndRegionTSimulator = new LTSimulator
                (new SpaceHelper(layerAndRegionExperiment, regionDegreeDistribution, NodeTypeEnum.LAYER_AND_PARTITION_LT));

        Sink normalSink = new Sink(normalLTSimulator);
        Sink layerSink = new Sink(layerLTSimulator);
        Sink regionSink = new Sink(regionLTSimulator);
        Sink layerAndRegionSink = new Sink(layerAndRegionTSimulator);

        getResult(normalSink, layerSink, regionSink, layerAndRegionSink);
        //getResult(normalSink, layerSink, regionSink, layerAndRegionSink, normalByLayerSink);
        draw(partitionNum,normalSink, layerSink, regionSink, layerAndRegionSink);
        //getResult(normalSink, layerSink, normalByLayerSink, layerAndRegionSink);
        //draw(Config.WALK_LENGTH,cofficient_of_c,normalSink, layerSink, normalByLayerSink, layerAndRegionSink);

        //getResult(normalSink, regionSink);
        //draw(partitionNum,normalSink, regionSink);
    }

    public static void getResult (Sink s1, Sink s2){
        boolean res1 = s1.collectPackage(NodeTypeEnum.LT);
        boolean res2 = s2.collectPackage(NodeTypeEnum.PARTITION_LT);
        System.out.println("普通LT:" + res1 + " 分区LT:" + res2);
    }

    public static void getResult (Sink s1, Sink s2, Sink s3, Sink s4){
        boolean res1 = s1.collectPackage(NodeTypeEnum.LT);
        boolean res2 = s2.collectPackage(NodeTypeEnum.LAYER_LT);
        boolean res3 = s3.collectPackage(NodeTypeEnum.PARTITION_LT);
        boolean res4 = s4.collectPackage(NodeTypeEnum.LAYER_AND_PARTITION_LT);
        System.out.println("普通LT:" + res1 + " 分层LT:" + res2 + " 普通LT分区收集方式:" + res3 + " 分层分区LT:" + res4);
    }

//    public static void getResult(Sink s1, Sink s2, Sink s3, Sink s4, Sink s5) {
//        boolean res1 = s1.collectPackage(NodeTypeEnum.LT);
//        boolean res2 = s2.collectPackage(NodeTypeEnum.LAYER_LT);
//        boolean res3 = s3.collectPackage(NodeTypeEnum.PARTITION_LT);
//        boolean res4 = s4.collectPackage(NodeTypeEnum.LAYER_AND_PARTITION_LT);
//        boolean res5 = s4.collectPackage(NodeTypeEnum.NORMAL_BY_LAYER_LT);
//        System.out.println("普通LT:" + res1 + " 分层LT:" + res2 + " 分区LT:" + res3
//                + " 分层分区LT:" + res4 + " 普通LT分层收集方式:" + res5);
//    }

    // 记录不同随机游走步长，不同系数c的图
    public static void draw(int walkLength,double cofficient,Sink...sinks) throws IOException {
        DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();
        int i = 1;
        for (Sink sink : sinks) {
            Map<Integer, Integer> decodingRatio = sink.getDecodingRatio();
            for (int index : decodingRatio.keySet()) {
                Integer value = decodingRatio.get(index);
                line_chart_dataset.addValue(value, "" + i, "" + index);
            }
            i++;
        }
        JFreeChart barChartObject = ChartFactory.createLineChart(
                "Decoding Ratio", "The number of receving encoding package",
                "The number of decoding package",
                line_chart_dataset, PlotOrientation.VERTICAL,
                true, true, false);

        int width = 800; /* Width of the image */
        int height = 800; /* Height of the image */
        String mkDirectoryPath = "G:/lab/LabGraph/123/" + cofficient_of_c + "系数";
        Utils.mkDirector(mkDirectoryPath);
        File barChart = new File(mkDirectoryPath+"/"+walkLength+"walkLength "+cofficient+"cofficient.jpg");
        //File barChart = new File("G:/lab/LabGraph/"+indexId+"partition decoding.jpg");
        ChartUtilities.saveChartAsJPEG(barChart, barChartObject, width, height);
    }

    // 记录不同分区不同系数c的图
    public static void draw (int indexId,Sink...sinks) throws IOException {
        DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();
        int i = 1;
        for (Sink sink : sinks) {
            Map<Integer, Integer> decodingRatio = sink.getDecodingRatio();
            for (int index : decodingRatio.keySet()) {
                Integer value = decodingRatio.get(index);
                line_chart_dataset.addValue(value, "" + i, "" + index);
            }
            i++;
        }
        JFreeChart barChartObject = ChartFactory.createLineChart(
                "Decoding Ratio", "The number of receving encoding package",
                "The number of decoding package",
                line_chart_dataset, PlotOrientation.VERTICAL,
                true, true, false);

        int width = 800; /* Width of the image */
        int height = 800; /* Height of the image */
        String mkDirectoryPath = "G:/lab/LabGraph/123/" + cofficient_of_c + "系数";
        Utils.mkDirector(mkDirectoryPath);
        File barChart = new File(mkDirectoryPath+"/"+indexId+"partition mixdecoding.jpg");
        //File barChart = new File("G:/lab/LabGraph/"+indexId+"partition decoding.jpg");
        ChartUtilities.saveChartAsJPEG(barChart, barChartObject, width, height);
    }

    public static void draw (Sink...sinks) throws IOException {
        DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();
        int i = 1;
        for (Sink sink : sinks) {
            Map<Integer, Integer> decodingRatio = sink.getDecodingRatio();
            for (int index : decodingRatio.keySet()) {
                Integer value = decodingRatio.get(index);
                line_chart_dataset.addValue(value, "" + i, "" + index);
            }
            i++;
        }
        JFreeChart barChartObject = ChartFactory.createLineChart(
                "Decoding Ratio", "The number of receving encoding package",
                "The number of decoding package",
                line_chart_dataset, PlotOrientation.VERTICAL,
                true, true, false);

        int width = 800; /* Width of the image */
        int height = 800; /* Height of the image */
        File barChart = new File("G:/lab/LabGraph/4decoding.jpg");
        ChartUtilities.saveChartAsJPEG(barChart, barChartObject, width, height);
    }


}
