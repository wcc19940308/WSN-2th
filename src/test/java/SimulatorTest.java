import ExperimentCode.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

public class SimulatorTest {
    // 一次完整的模拟实验
    public static void main(String[] args) throws IOException {
        simulatorTest();
    }

    public static void sortHashMap(Map<Integer,Integer> map) {
        List<Map.Entry<Integer, Integer>> mapList = new ArrayList<Map.Entry<Integer, Integer>>(map.entrySet());
        Collections.sort(mapList, new Comparator<Map.Entry<Integer, Integer>>() {
            @Override
            public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                return o1.getKey() - o2.getKey();
            }
        });
    }


    public static void simulatorTest() throws IOException {
        Experiment experiment = new Experiment(4,4,0,
                8,16,2);
        DegreeDistribution degreeDistribution = new LTDegreeDistribution(0.01,0.05,8);



//        Experiment experiment = new Experiment(100,100,0,
//                50,100,10);
//        DegreeDistribution degreeDistribution = new LTDegreeDistribution(0.01,0.05,50);

//        Experiment experiment = new Experiment(1000,1000,0,
//                5000,10000,30);
//        DegreeDistribution degreeDistribution = new LTDegreeDistribution(0.01,0.05,5000);

        // 模拟分层的实验场景
        SpaceHelper spaceHelper = new SpaceHelper(experiment, degreeDistribution,NodeTypeEnum.LAYER_LT);
        HashMap<Integer, Node> nodes = spaceHelper.uniformGenerateNodes();
        // 度分布
        spaceHelper.initDegree(nodes);


        // 理想的度分布情况图
        DefaultCategoryDataset idealLTDataset = new DefaultCategoryDataset();
        Map<Integer, Integer> idealLTGraph = new HashMap<>();
        int sum = 0; // 用于记录度的总数，求每个度的所占概率
        for (int nodeId : nodes.keySet()) {
            Node node = nodes.get(nodeId);
            int degree = node.getDegree();
            idealLTGraph.put(degree, idealLTGraph.getOrDefault(degree, 0)+1);
        }
        //sortHashMap(idealLTGraph);

        for (Integer index : idealLTGraph.keySet()) {
            // 相应度的数量
            Integer num = idealLTGraph.get(index);

            idealLTDataset.addValue(1.0*num/nodes.size(),"",index);
        }
        JFreeChart IdealDegreeChartObject = ChartFactory.createBarChart(
                "Ideal Degree Distribution","The degree",
                "The number of degree",
                idealLTDataset, PlotOrientation.VERTICAL,
                true,true,false);

        int width1 = 1500; /* Width of the image */
        int height1 = 1500; /* Height of the image */
        File BarChart1 = new File( "G:/lab/LabGraph/LTGraph.jpg" );
        ChartUtilities.saveChartAsJPEG(BarChart1 ,IdealDegreeChartObject, width1 ,height1);


        // 初始化每个节点的邻居
        spaceHelper.initAddNeighbors(nodes);
        // 稳态分布
        //spaceHelper.initSteadyState(nodes);
        // 分区
        if (spaceHelper.getType() == NodeTypeEnum.PARTITION_LT || spaceHelper.getType() == NodeTypeEnum.LAYER_AND_PARTITION_LT) {
            spaceHelper.initPartition(nodes);
        }
        // 分层
        if (spaceHelper.getType() == NodeTypeEnum.LAYER_LT || spaceHelper.getType() == NodeTypeEnum.LAYER_AND_PARTITION_LT) {
            spaceHelper.initLayer(nodes);
        }
        LTSimulator simulator = new LTSimulator(spaceHelper);
        //simulator.init();


        // 真实的度分布情况
        DefaultCategoryDataset realLTDataset = new DefaultCategoryDataset();
        Map<Integer, Integer> realLTGraph = new HashMap<>();
        for (int nodeId : nodes.keySet()) {
            Node node = nodes.get(nodeId);
            int realDegree = node.getPackList().size();
            realLTGraph.put(realDegree, realLTGraph.getOrDefault(realDegree, 0)+1);
        }
        //sortHashMap(realLTGraph);

        for (Integer index : realLTGraph.keySet()) {
            Integer num = realLTGraph.get(index);
            realLTDataset.addValue(1.0*num/nodes.size(),"",index);
        }
        JFreeChart realDegreeChartObject = ChartFactory.createBarChart(
                "Real Degree Distribution","The real degree",
                "The number of real degree",
                realLTDataset, PlotOrientation.VERTICAL,
                true,true,false);

        int width2 = 1500; /* Width of the image */
        int height2 = 1500; /* Height of the image */
        File BarChart2 = new File( "G:/lab/LabGraph/RealLTGraph.jpg" );
        ChartUtilities.saveChartAsJPEG(BarChart2 ,realDegreeChartObject, width2 ,height2);




        Sink sink = new Sink(simulator);
        sink.boost();
        //sink.sendDecodingPackage();


        // 设置输出的日志目录
        PrintStream printStream1 = new PrintStream("G:/lab/LabData/log.txt");
        System.setOut(printStream1);

        //Map<Integer, CodingPackage> packageInfo = simulator.getPackageInfo();

//        for (int id : packageInfo.keySet()) {
//            CodingPackage codingPackage = packageInfo.get(id);
//            System.out.println(codingPackage);
//        }

//        for (int nodeId : nodes.keySet()) {
//            Node curNode = nodes.get(nodeId);
//            System.out.println("degree:"+curNode.getDegree()+" real degree:"+curNode.getPackList().size()
//                    +" packList:"+curNode.getPackList());
//        }

        // 输出最外层节点中存储的编码包的数量，即最后会发送的总的数据包数量


        // System.out.println("发送的编码数据包"+sink.getDecodingPackageNum());

        boolean flag = sink.normalDecode();
        Map<Integer, Integer> decodingRatio = sink.getDecodingRatio();

        for (int index : decodingRatio.keySet()) {
            Integer integer = decodingRatio.get(index);
            System.out.println("收到编码包:"+index+"个，解出源数据:"+integer+"个");
        }

        System.out.println(flag);

        System.out.println(sink.getOneDegreeData().size());

        System.out.println(1.0*sink.getOneDegreeData().size()/spaceHelper.getExperiment().getSensorCount());

        // 输出收到数据包和恢复源数据的关系的图形
        DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();
        for (int index : decodingRatio.keySet()) {
            Integer value = decodingRatio.get(index);
            line_chart_dataset.addValue(value,"LT Code",""+index);
        }
        JFreeChart lineChartObject = ChartFactory.createLineChart(
                "1","The number of receving encoding package",
                "The number of decoding package",
                line_chart_dataset, PlotOrientation.VERTICAL,
                true,true,false);

        int width = 800; /* Width of the image */
        int height = 800; /* Height of the image */
        File lineChart1 = new File( "G:/lab/LabGraph/decoding.jpg" );
        ChartUtilities.saveChartAsJPEG(lineChart1 ,lineChartObject, width ,height);
    }


}
