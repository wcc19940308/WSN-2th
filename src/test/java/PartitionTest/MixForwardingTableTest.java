package PartitionTest;

import ExperimentCode.*;
import com.google.common.collect.Range;

import java.util.HashMap;
import java.util.Map;

// 测试用于混合编码的转移表
public class MixForwardingTableTest {

    public static void main(String[] args) {
        Experiment experiment = new Experiment(1000,1000,0,
                5000,10000,30);
        DegreeDistribution degreeDistribution = new LTDegreeDistribution(0.01, 0.05, 5000);

        LTSimulator simulator = new LTSimulator(new SpaceHelper(experiment, degreeDistribution, NodeTypeEnum.LT));
        simulator.initOrRefreshMixForwardingTable();
        simulator.initOrRefreshMixForwardingTableProbInterval();
        double tableSum = 0;
        Map<Integer, Node> nodes = experiment.getNodes();
        for (int nodeId : nodes.keySet()) {
            Node curNode = nodes.get(nodeId);
            Map<Integer, Double> mixForwardingTable = curNode.getMixForwardingTable();
            Map<Integer, Range<Double>> mixForwardingTableProbInterval
                    = curNode.getMixForwardingTableProbInterval();
            System.out.println("index:" + nodeId);
            for (int probIndex : mixForwardingTable.keySet()) {
                Double prob = mixForwardingTable.get(probIndex);
                System.out.println(prob + "");
                tableSum += prob;
            }
            System.out.println();
            System.out.println(tableSum);
            tableSum = 0;
//            System.out.println("===================");
//            for (int probIndex : mixForwardingTableProbInterval.keySet()) {
//                Range<Double> range = mixForwardingTableProbInterval.get(probIndex);
//                System.out.println("range:" + range);
//                System.out.println();
//            }
        }

    }
}
