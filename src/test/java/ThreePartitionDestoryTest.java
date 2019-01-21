import ExperimentCode.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.File;
import java.io.IOException;
import java.util.Map;

// 不同系数C的情况下，破坏率从0.01到0.15的情况下普通LT，3分区LT，分层LT，普通分层方式收集LT的恢复情况
public class ThreePartitionDestoryTest {
    public static double cofficient_of_c = 0.2;

    public static void main(String[] args) throws IOException {
        Config.PARTITION_NUM = 3;
        for (int j = 1; j <= 10; j++) {
            cofficient_of_c = cofficient_of_c + 0.01;
            Config.DESTORY_RATIO = 0;
            for (int i = 1; i <= 15; i++) {
                Config.DESTORY_RATIO += 0.01;
                init(Config.PARTITION_NUM);
            }
        }
    }
    public static void init(int partitionNum) throws IOException {


        Experiment normalExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 40);

        Experiment regionExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 40);

//        Experiment layerExperiment = new Experiment(1000, 1000, 0,
//                5000, 10000, 40);
//
//        Experiment normalLayerLTExperiment = new Experiment(1000, 1000, 0,
//                5000, 10000, 40);


        DegreeDistribution degreeDistribution = new LTDegreeDistribution(cofficient_of_c, 0.05, 5000);
        DegreeDistribution regionDegreeDistribution = new LTDegreeDistribution(cofficient_of_c, 0.05, 5000/partitionNum);

        LTSimulator normalLTSimulator = new LTSimulator(
                new SpaceHelper(normalExperiment, degreeDistribution, NodeTypeEnum.LT));
        LTSimulator regionLTSimulator = new LTSimulator(
                new SpaceHelper(regionExperiment, regionDegreeDistribution, NodeTypeEnum.PARTITION_LT));
//        LTSimulator layerLTSimulator = new LTSimulator(
//                new SpaceHelper(layerExperiment, degreeDistribution, NodeTypeEnum.LAYER_LT));
//        LTSimulator normalLayerLTSimulator =
//                new LTSimulator(new SpaceHelper(normalLayerLTExperiment, degreeDistribution, NodeTypeEnum.NORMAL_BY_LAYER_LT));

        Sink normalSink = new Sink(normalLTSimulator);
        Sink regionSink = new Sink(regionLTSimulator);
//        Sink layerSink = new Sink(layerLTSimulator);
//        Sink normalLayerSink = new Sink(normalLayerLTSimulator);

        getResult(normalSink, regionSink);
        draw(normalSink, regionSink);


//        getResult(normalSink, layerSink, regionSink, normalLayerSink);
//        draw(partitionNum,normalSink, layerSink, regionSink, normalLayerSink);
    }

    public static void getResult (Sink s1, Sink s2){
        boolean res1 = s1.collectPackage(NodeTypeEnum.LT);
        boolean res2 = s2.collectPackage(NodeTypeEnum.PARTITION_LT);
        System.out.println("普通LT:" + res1 + " 普通LT分区收集方式:" + res2);
    }


    public static void getResult (Sink s1, Sink s2, Sink s3, Sink s4){
        boolean res1 = s1.collectPackage(NodeTypeEnum.LT);
        boolean res2 = s2.collectPackage(NodeTypeEnum.LAYER_LT);
        boolean res3 = s3.collectPackage(NodeTypeEnum.PARTITION_LT);
        boolean res4 = s4.collectPackage(NodeTypeEnum.NORMAL_BY_LAYER_LT);
        System.out.println("普通LT:" + res1 + " 分层LT:" + res2 + " 普通LT分区收集方式:" + res3 + " 分层分区LT:" + res4);
    }

    // 记录不同系数c不同破坏程度的图
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
        String mkDirectoryPath = "G:/lab/LabGraph/123/" + cofficient_of_c + "系数";
        Utils.mkDirector(mkDirectoryPath);
        File barChart = new File(mkDirectoryPath+"/"+Config.DESTORY_RATIO+"partition mixdecoding.jpg");
        //File barChart = new File("G:/lab/LabGraph/"+indexId+"partition decoding.jpg");
        ChartUtilities.saveChartAsJPEG(barChart, barChartObject, width, height);
    }
}
