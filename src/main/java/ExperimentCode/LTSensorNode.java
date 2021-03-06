package ExperimentCode;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.Map;

@Getter
@Setter
public class LTSensorNode extends Node{
    int copyNum; // 节点产生的数据包个数

    public LTSensorNode(double posX, double poxY, StateEnum state) {
        this.posX = posX;
        this.posY = poxY;
        this.state = StateEnum.ALIVE;
        neighbors = new LinkedList<Node>();
    }

    // 初始化每个感知节点需要发送的数据包数量b
    public int initCopyNum(Experiment experiment, DegreeDistribution degreeDistribution, NodeTypeEnum type) {
        Map<Double, Double> robustSolitonDistribution = degreeDistribution.getRobustSolitonDistribution();
//        int totalCount = experiment.getTotalCount();
//        int sensorCount = experiment.getSensorCount();

        int totalCount = Config.N;
        int sensorCount = Config.K;
        
        double sum = 0;
        for (double index : robustSolitonDistribution.keySet()) {
            Double prob = robustSolitonDistribution.get(index);
            sum += index * prob;
        }
        sum *= totalCount;
        copyNum = (int) (sum / sensorCount);
        // 如果是EDFC的话，就是要发送冗余的感知数据包用于编码操作
        if (type == NodeTypeEnum.EDFC || type == NodeTypeEnum.MOWELFC || type == NodeTypeEnum.MOWOELFC || type == NodeTypeEnum.MOW_LT) copyNum *= Config.REDUNDANCY;
        return copyNum;
    }
}
