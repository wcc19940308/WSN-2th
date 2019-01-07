package ExperimentCode;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.Map;

@Getter
@Setter
public class LTSensorNode extends Node{
    int copyNum; // �ڵ���������ݰ�����

    public LTSensorNode(double posX, double poxY, StateEnum state) {
        this.posX = posX;
        this.posY = poxY;
        this.state = StateEnum.ALIVE;
        neighbors = new LinkedList<Node>();
    }

    // ��ʼ��ÿ����֪�ڵ���Ҫ���͵����ݰ�����b
    public int initCopyNum(Experiment experiment, DegreeDistribution degreeDistribution) {
        Map<Double, Double> robustSolitonDistribution = degreeDistribution.getRobustSolitonDistribution();
        int totalCount = experiment.getTotalCount();
        int sensorCount = experiment.getSensorCount();
        double sum = 0;
        for (double index : robustSolitonDistribution.keySet()) {
            Double prob = robustSolitonDistribution.get(index);
            sum += index * prob * Config.REDUNDANCY;
        }
        sum *= totalCount;
        copyNum = (int) (sum / sensorCount);
        return copyNum;
    }
}
