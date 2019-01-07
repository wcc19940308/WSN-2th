import ExperimentCode.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class NormalLTAndLayerLTTest
{
    public static void main(String[] args) throws IOException {
        Experiment normalExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 50);

        Experiment layerExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 50);

        // ʹ�÷ֲ�����ռ���ʽ����ͨLT
        Experiment normalLayerLTExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 50);

        DegreeDistribution degreeDistribution = new LTDegreeDistribution(0.01, 0.05, 5000);

        LTSimulator normalLTSimulator = new LTSimulator(new SpaceHelper(normalExperiment, degreeDistribution, NodeTypeEnum.LT));
        LTSimulator layerLTSimulator = new LTSimulator(new SpaceHelper(layerExperiment, degreeDistribution, NodeTypeEnum.LAYER_LT));
        LTSimulator normalLayerLTSimulator =
                new LTSimulator(new SpaceHelper(normalLayerLTExperiment, degreeDistribution, NodeTypeEnum.NORMAL_BY_LAYER_LT));

        Sink normalSink = new Sink(normalLTSimulator);
        Sink layerSink = new Sink(layerLTSimulator);
        Sink normalLTSink = new Sink(normalLayerLTSimulator);

        System.out.println("��ͨLT��Ҫ���ⲽ��:" + normalLTSimulator.getREDUNDANCY_WALK_LENGTH() +
                " �ֲ�LT��Ҫ���ⲽ��:" + layerLTSimulator.getREDUNDANCY_WALK_LENGTH() +
                " ��ͨLT�Էֲ㷽ʽ�ռ���Ҫ���ⲽ��:" + normalLayerLTSimulator.getREDUNDANCY_WALK_LENGTH());

        getResult(normalSink, layerSink, normalLTSink);

        List<Sink> list = new ArrayList<>(Arrays.asList(normalSink, layerSink, normalLTSink));
        draw(list);

    }

    public static void getResult(Sink s1, Sink s2, Sink s3) {
        boolean res1 = s1.collectPackage(NodeTypeEnum.LT);
        boolean res2 = s2.collectPackage(NodeTypeEnum.LAYER_LT);
        boolean res3 = s3.collectPackage(NodeTypeEnum.NORMAL_BY_LAYER_LT);
        System.out.println("��ͨLT:" + res1 + " �ֲ�LT:" + res2 + " ��ͨLT�Էֲ㷽ʽ�ռ�:" + res3);
    }

    public static void draw(List<Sink> sinks) throws IOException {
        DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();
        int i = 1;
        for (Sink sink : sinks) {
            Map<Integer, Integer> decodingRatio = sink.getDecodingRatio();
            for (int index : decodingRatio.keySet()) {
                Integer value = decodingRatio.get(index);
                line_chart_dataset.addValue(value,""+i,""+index);
            }
            i++;
        }
        JFreeChart barChartObject = ChartFactory.createLineChart(
                "Decoding Ratio","The number of receving encoding package",
                "The number of decoding package",
                line_chart_dataset, PlotOrientation.VERTICAL,
                true,true,false);

        int width = 800; /* Width of the image */
        int height = 800; /* Height of the image */
        File barChart = new File( "G:/lab/LabGraph/noLayerAndNoRegionDecoding.jpg" );
        ChartUtilities.saveChartAsJPEG(barChart ,barChartObject, width ,height);
    }
}
