package ExperimentCode;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class WSN {

    public static void init() throws IOException {

//                Experiment layerExperiment = new Experiment(4,4,0,
//                8,16,2);
//        Experiment nonLayerExperiment = new Experiment(4,4,0,
//                8,16,2);
//        DegreeDistribution degreeDistribution = new LTDegreeDistribution(0.01,0.05,8);


//                Experiment layerExperiment = new Experiment(100,100,0,
//                50,100,10);
//        DegreeDistribution degreeDistribution = new LTDegreeDistribution(0.01,0.05,50);
//
//        Experiment nonLayerExperiment = new Experiment(100,100,0,
//                50,100,10);
//        //DegreeDistribution degreeDistribution = new LTDegreeDistribution(0.01,0.05,50);


        // 分层LT
        Experiment layerExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 30);
        // 不分层LT
        Experiment nonLayerExperiment = new Experiment(1000,1000,0,
                5000,10000,30);
        // 分区分层LT
        Experiment layerAndPartitionExperiment = new Experiment(1000,1000,0,
                5000,10000,30);


        DegreeDistribution degreeDistribution = new LTDegreeDistribution(0.01,0.05,5000);

        LTSimulator layerSimulator = new LTSimulator(new SpaceHelper(layerExperiment, degreeDistribution, NodeTypeEnum.LAYER_LT));
        LTSimulator nonLayerSimulator = new LTSimulator(new SpaceHelper(nonLayerExperiment, degreeDistribution, NodeTypeEnum.LT));

        Sink layerSink = new Sink(layerSimulator);
        Sink nonLayerSink = new Sink(nonLayerSimulator);


        boolean layerFlag = layerSink.collectPackage(NodeTypeEnum.LAYER_LT);
        boolean nonLayerFlag = nonLayerSink.collectPackage(NodeTypeEnum.LT);

        // 输出收到数据包和恢复源数据的关系的图形
        Map<Integer, Integer> layerDecodingRatio = layerSink.getDecodingRatio();
        Map<Integer, Integer> nonLayerSinkDecodingRatio = nonLayerSink.getDecodingRatio();


        DefaultCategoryDataset bar_chart_dataset = new DefaultCategoryDataset();
        for (int index : layerDecodingRatio.keySet()) {
            Integer value = layerDecodingRatio.get(index);
            bar_chart_dataset.addValue(value,"Layer LT Code",""+index);
        }
        for (int index : nonLayerSinkDecodingRatio.keySet()) {
            Integer value = nonLayerSinkDecodingRatio.get(index);
            bar_chart_dataset.addValue(value,"Non-Layer LT Code",""+index);
        }
        JFreeChart barChartObject = ChartFactory.createLineChart(
                "Decoding Ratio","The number of receving encoding package",
                "The number of decoding package",
                bar_chart_dataset, PlotOrientation.VERTICAL,
                true,true,false);

        int width = 800; /* Width of the image */
        int height = 800; /* Height of the image */
        File barChart = new File( "G:/lab/LabGraph/decoding.jpg" );
        ChartUtilities.saveChartAsJPEG(barChart ,barChartObject, width ,height);

        System.out.println("分层的LT"+layerFlag+"， 未分层的LT"+nonLayerFlag);
    }

    public static void main(String[] args) throws IOException {

        long start = System.currentTimeMillis();
        init();
        long end = System.currentTimeMillis();
        System.out.println("一次实验需要:"+(end-start)/1000);

    }
}
