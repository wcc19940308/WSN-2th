package PartitionTest;

import ExperimentCode.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.File;
import java.io.IOException;
import java.util.Map;

// 测试用分层方式收集普通LT实验
public class SingleLTLayerTest {
    public static void main(String[] args) throws IOException {
        Experiment normalLayerLTExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 50);

        DegreeDistribution degreeDistribution = new LTDegreeDistribution(0.01, 0.05, 5000);

        LTSimulator normalLayerLTSimulator =
                new LTSimulator(new SpaceHelper(normalLayerLTExperiment, degreeDistribution, NodeTypeEnum.NORMAL_BY_LAYER_LT));
        Sink normalLTSink = new Sink(normalLayerLTSimulator);

        boolean res = normalLTSink.collectPackage(NodeTypeEnum.NORMAL_BY_LAYER_LT);
        draw(normalLTSink);
    }

    public static void draw(Sink sink) throws IOException {
        DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();
        int i = 1;
        Map<Integer, Integer> decodingRatio = sink.getDecodingRatio();
        for (int index : decodingRatio.keySet()) {
            Integer value = decodingRatio.get(index);
            line_chart_dataset.addValue(value, "" + i, "" + index);

            JFreeChart barChartObject = ChartFactory.createLineChart(
                    "Decoding Ratio", "The number of receving encoding package",
                    "The number of decoding package",
                    line_chart_dataset, PlotOrientation.VERTICAL,
                    true, true, false);

            int width = 800; /* Width of the image */
            int height = 800; /* Height of the image */
            File barChart = new File("G:/lab/LabGraph/singleLTLayerTest.jpg");
            ChartUtilities.saveChartAsJPEG(barChart, barChartObject, width, height);
        }
    }
}
