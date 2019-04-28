package LabData;

import ExperimentCode.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class LTandNewTest {
    public static void main(String[] args) throws IOException {
        Experiment normalLayerLTExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 50);

        DegreeDistribution degreeDistribution = new NewDegreeDistribution(0.01, 0.05, 5000,1,0.5);

        LTSimulator normalLayerLTSimulator =
                new LTSimulator(new SpaceHelper(normalLayerLTExperiment, degreeDistribution, NodeTypeEnum.LT));
        Sink LTSink = new Sink(normalLayerLTSimulator);

        boolean res = LTSink.collectPackage(NodeTypeEnum.LT);
        //draw(normalLTSink);

        System.out.println("---开始写文件啦---");

        Map<Integer, Integer> LTMap = LTSink.getDecodingRatio();
        File file = new File("G:/lab/LabData/新的度分布函数恢复情况.txt");
        file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        for (Integer index : LTMap.keySet()) {
            out.write(index + " " + LTMap.get(index) + "\r\n");
        }
        out.flush();
        out.close();
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
            File barChart = new File("G:/lab/LabGraph/2019-3-6.jpg");
            ChartUtilities.saveChartAsJPEG(barChart, barChartObject, width, height);
        }
    }
}
