import ExperimentCode.*;
import com.google.common.collect.Range;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.Test;
import org.omg.CORBA.INTERNAL;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class TestDemo {

    // ���Զȷֲ������Ƿ�׼ȷ
    @Test
    public void testDisribution() {
        LTDegreeDistribution degreeDistribution = new LTDegreeDistribution(0.2, 0.05, 5000);
        //DegreeDistribution degreeDistribution = new LTDegreeDistribution(0.01,0.05,5000);
        Map<Double, Double> integerDoubleHashMap = degreeDistribution.getRobustSolitonDistribution();
        double sum = 0;
        for (double i : integerDoubleHashMap.keySet()) {
            sum += integerDoubleHashMap.get(i);
            System.out.println("i="+i+": "+integerDoubleHashMap.get(i));
        }
        System.out.println(sum);
        //System.out.println(integerDoubleHashMap);
        Map<Double, Range<Double>> probInterval = degreeDistribution.getProbInterval();
        System.out.println(probInterval);
        System.out.println(Math.random() * 100);
    }

    // ���Բ�ͬ��������µ��Ż�������
    @Test
    public void testPerformance() {
        double c1 = 0.3;
        double c2 = 0.05;
        double k = 5000;
        double region = 3;
        double k2 = k/region ;
        //ƽ�����븴�Ӷ�
        double res1 = Math.log(k) + 1 + Math.log(c1 * Math.log(k / c2) * Math.sqrt(k) / c2);
        double res2 = Math.log(k2) + 1 + Math.log(c1 * Math.log(k2 / c2) * Math.sqrt(k2) / c2);

        //ƽ����������
        double res3 = k + c1 * Math.sqrt(k) * Math.log(k / c2) * Math.log(k / c2);
        //double res4 = k + 3 * c1 * Math.sqrt(k / 3) * Math.log(k / 3 * c2) * Math.log(k / 3 * c2);
        double res4 = k2 + c1 * Math.sqrt(k2) * Math.log(k2 / c2) * Math.log(k2 / c2);
        //double res4 = (k + region * c1 * Math.sqrt(k2) * Math.log(k2 * c2) * Math.log(k2 / c2)) / region;

        //double res2 = 3 * Math.log(k / 3) + 3 + 3 * Math.log(c1 * Math.log(k / 3 * c2) * Math.sqrt(k / 3) / c2);
        System.out.println(res1);
        System.out.println(res2);
        System.out.println(res3);
        System.out.println(res4);
        System.out.println((res1 - res2) / res1);
        System.out.println((res3-res4)/res3);
    }

    //���Զȷֲ��Ĵ�����ȡֵ
    @Test
    public void testDegree() throws IOException {
//        Experiment experiment = new Experiment(4,4,0,8,16,2);
//        DegreeDistribution degreeDistribution = new LTDegreeDistribution(0.01,0.05,8);

//        Experiment experiment = new Experiment(1000,1000,0,
//                5000,5000,20);
//        DegreeDistribution degreeDistribution = new LTDegreeDistribution(0.01,0.05,5000);

        Experiment experiment = new Experiment(1000,1000,0,
                5000,10000,28);
        DegreeDistribution degreeDistribution = new LTDegreeDistribution(0.01,0.05,5000);;

        SpaceHelper spaceHelper = new SpaceHelper(experiment, degreeDistribution,NodeTypeEnum.LAYER_AND_PARTITION_LT);
        HashMap<Integer, Node> nodes = spaceHelper.uniformGenerateNodes();
        //�ȷֲ�
        spaceHelper.initDegree(nodes);
        // ����Ķȷֲ����ͼ
        DefaultCategoryDataset idealLTDataset = new DefaultCategoryDataset();
        Map<Integer, Integer> idealLTGraph = new HashMap<Integer, Integer>();
        for (int nodeId : nodes.keySet()) {
            Node node = nodes.get(nodeId);
            int degree = node.getDegree();
            idealLTGraph.put(degree, idealLTGraph.getOrDefault(degree, 0)+1);
        }

        for (int degree : idealLTGraph.keySet()) {
            Integer num = idealLTGraph.get(degree);
            System.out.println("�ȣ�"+degree+"������"+num);
        }

        for (Integer index : idealLTGraph.keySet()) {
            Integer num = idealLTGraph.get(index);
            idealLTDataset.addValue(num,"",index);
        }
        JFreeChart lineChartObject1 = ChartFactory.createBarChart(
                "1","The degree",
                "The number of degree",
                idealLTDataset, PlotOrientation.VERTICAL,
                true,true,false);

        int width = 800; /* Width of the image */
        int height = 800; /* Height of the image */
        File lineChart = new File( "G:/lab/LabGraph/LTGraph.jpg" );
        ChartUtilities.saveChartAsJPEG(lineChart ,lineChartObject1, width ,height);
    }

    //���Գ�ʼ�����,���4*4�������ֹ8����֪�ڵ㣬8������ڵ�����
    @Test
    public void testInit() {
//        Experiment experiment = new Experiment(4,4,0,
//                8,16,2);
//        DegreeDistribution degreeDistribution = new LTDegreeDistribution(0.01,0.05,8);

        Experiment experiment = new Experiment(1,0,
                5000,10000,0.033);
        DegreeDistribution degreeDistribution = new LTDegreeDistribution(0.01,0.05,5000);

        SpaceHelper spaceHelper = new SpaceHelper(experiment, degreeDistribution,NodeTypeEnum.LAYER_AND_PARTITION_LT);
        HashMap<Integer, Node> nodes = spaceHelper.uniformGenerateNodes();
        //�ȷֲ�
        spaceHelper.initDegree(nodes);
        //��ʼ��ÿ���ڵ���ھ�
        spaceHelper.initAddNeighbors(nodes);
        //��̬�ֲ�
        //spaceHelper.initSteadyState(nodes);
        //����
        if (spaceHelper.getType() == NodeTypeEnum.PARTITION_LT || spaceHelper.getType() == NodeTypeEnum.LAYER_AND_PARTITION_LT) {
            spaceHelper.initPartition(nodes);
        }
        //�ֲ�
        if (spaceHelper.getType() == NodeTypeEnum.LAYER_LT || spaceHelper.getType() == NodeTypeEnum.LAYER_AND_PARTITION_LT) {
            spaceHelper.initLayer(nodes);
        }
        double sum = 0;
        for (int index : nodes.keySet()) {
            Node node = nodes.get(index);
            sum += node.getSteadyState();
            System.out.println("id:"+node.getId()+" x:"+node.getPosX()+" y:"+node.getPosY()+" degree:"+node.getDegree()
                    +" steadyState:"+node.getSteadyState()+" region:"+node.getRegion()+" layer:"+node.getLayer());
        }
        System.out.println(sum);
    }

    // ˳�����һ��ת�Ʊ�
    @Test
    public void testInitForwardingTable() {
//        Experiment experiment = new Experiment(4,4,0,
//                8,16,2);
//        DegreeDistribution degreeDistribution = new LTDegreeDistribution(0.01,0.05,8);

        Experiment experiment = new Experiment(1000,1000,0,
                5000,10000,28);
        DegreeDistribution degreeDistribution = new LTDegreeDistribution(0.01,0.05,5000);

//        Experiment experiment = new Experiment(1,0,
//                5000,10000,0.033);
//        DegreeDistribution degreeDistribution = new LTDegreeDistribution(0.01,0.05,5000);


        SpaceHelper spaceHelper = new SpaceHelper(experiment, degreeDistribution,NodeTypeEnum.LAYER_AND_PARTITION_LT);
        HashMap<Integer, Node> nodes = spaceHelper.uniformGenerateNodes();
        // �ȷֲ�
        spaceHelper.initDegree(nodes);
        // ��ʼ��ÿ���ڵ���ھ�
        spaceHelper.initAddNeighbors(nodes);
        // ��̬�ֲ�
        //spaceHelper.initSteadyState(nodes);
        // ����
        if (spaceHelper.getType() == NodeTypeEnum.PARTITION_LT || spaceHelper.getType() == NodeTypeEnum.LAYER_AND_PARTITION_LT) {
            spaceHelper.initPartition(nodes);
        }
        // �ֲ�
        if (spaceHelper.getType() == NodeTypeEnum.LAYER_LT || spaceHelper.getType() == NodeTypeEnum.LAYER_AND_PARTITION_LT) {
            spaceHelper.initLayer(nodes);
        }
        LTSimulator simulator = new LTSimulator(spaceHelper);
        simulator.generatePackage();
        simulator.initOrRefreshForwardingTable();
        simulator.initOrRefreshForwardingTableProbInterval();
        double steadySum = 0;
        double forwardingTableSum = 0;
        double probInterval = 0;
        for (int index : nodes.keySet()) {
            Node curNode = nodes.get(index);
            steadySum += curNode.getSteadyState();
            Map<Integer, Double> forwardingTable = curNode.getForwardingTable();
            Map<Integer, Range<Double>> forwardingTableProbInterval = curNode.getForwardingTableProbInterval();
            System.out.println("index:"+index);
            for (int probIndex : forwardingTable.keySet()) {
                Double prob = forwardingTable.get(probIndex);
                System.out.print(prob+" ");
                forwardingTableSum += prob;
            }
            System.out.println();
            System.out.println(forwardingTableSum);
            forwardingTableSum = 0;
            System.out.println("===================");
            System.out.println("current degree:"+curNode.getDegree());
            for (int probIndex : forwardingTableProbInterval.keySet()) {
                Range<Double> doubleRange = forwardingTableProbInterval.get(probIndex);
                System.out.print("range: "+doubleRange+"degree: "+nodes.get(probIndex).getDegree());
                System.out.println();
            }
        }
    }

    // ������ɺ���ڵ�洢��Ϣ�����
    @Test
    public void testEncoding() throws FileNotFoundException {
        Experiment experiment = new Experiment(1000,1000,0,
                5000,10000,28);
        DegreeDistribution degreeDistribution = new LTDegreeDistribution(0.01,0.05,5000);
//        Experiment experiment = new Experiment(4,4,0,
//                8,16,2);
//        DegreeDistribution degreeDistribution = new LTDegreeDistribution(0.01,0.05,8);
        SpaceHelper spaceHelper = new SpaceHelper(experiment, degreeDistribution,NodeTypeEnum.LAYER_AND_PARTITION_LT);
        HashMap<Integer, Node> nodes = spaceHelper.uniformGenerateNodes();
        // �ȷֲ�
        spaceHelper.initDegree(nodes);
        // ��ʼ��ÿ���ڵ���ھ�
        spaceHelper.initAddNeighbors(nodes);
        // ��̬�ֲ�
        //spaceHelper.initSteadyState(nodes);
        // ����
        if (spaceHelper.getType() == NodeTypeEnum.PARTITION_LT || spaceHelper.getType() == NodeTypeEnum.LAYER_AND_PARTITION_LT) {
            spaceHelper.initPartition(nodes);
        }
        // �ֲ�
        if (spaceHelper.getType() == NodeTypeEnum.LAYER_LT || spaceHelper.getType() == NodeTypeEnum.LAYER_AND_PARTITION_LT) {
            spaceHelper.initLayer(nodes);
        }

        LTSimulator simulator = new LTSimulator(spaceHelper);
        // �������ݰ�
        simulator.generatePackage();
        // ��ʼ��ת�Ʊ�
        simulator.initOrRefreshForwardingTable();
        // ��ʼ������ת������
        simulator.initOrRefreshForwardingTableProbInterval();
        // ��ʼһ�α���
        simulator.singlecastEncoding();

        // �����������־Ŀ¼
        PrintStream printStream = new PrintStream("G:/log.txt");
        System.setOut(printStream);
    }




    // ���ڼ���ʹ�������еĽڵ�ƽ���ھӴﵽ21��Ҫ�İ뾶����
    @Test
    public void calculateRadiuTest() {
//        Experiment experiment = new Experiment(500,500,0,
//                1000,2000,28);
//        DegreeDistribution degreeDistribution = new LTDegreeDistribution(0.01,0.05,5000);
//        Experiment experiment = new Experiment(1000,1000,0,
//                6000,12000,26);
        Experiment normalExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 50);
        DegreeDistribution degreeDistribution = new LTDegreeDistribution(0.01,0.05,5000);

//        Experiment experiment = new Experiment(1,0,
//                5000,10000,0.033);
//        DegreeDistribution degreeDistribution = new LTDegreeDistribution(0.01,0.05,5000);

        SpaceHelper spaceHelper = new SpaceHelper(normalExperiment, degreeDistribution,NodeTypeEnum.LAYER_AND_PARTITION_LT);
        HashMap<Integer, Node> nodes = spaceHelper.uniformGenerateNodes();
        // �ȷֲ�
        spaceHelper.initDegree(nodes);
        // ��ʼ��ÿ���ڵ���ھ�
        spaceHelper.initAddNeighbors(nodes);
        int sum = 0;
        for (int index : nodes.keySet()) {
            Node node = nodes.get(index);
            int size = node.getNeighbors().size();
            sum += size;
        }
        System.out.println(sum/normalExperiment.getTotalCount());
    }

}