package ExperimentCode;

import com.google.common.collect.Range;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
// ����ʵ��Ҫ�����Ϣ������Ӧ��ʵ�鳡��,�������еĽڵ���г�ʼ��������

// ��ʼ��λ�ã�id��Ҫ��Ķȣ���̬�ֲ�
// ���ݽڵ�������ṩ�����ͷֲ�ķ����Թ�ѡ��
public class SpaceHelper {
    Experiment experiment;
    DegreeDistribution degreeDistribution;
    NodeTypeEnum type;

    // ������Կ��ƴ���Ķȷֲ���������
    public SpaceHelper(Experiment experiment, DegreeDistribution degreeDistribution, NodeTypeEnum type) {
        this.experiment = experiment;
        this.degreeDistribution = degreeDistribution;
        this.type = type;
    }

    // ����1000*1000��������ȷ���5000����֪�ڵ��5000������ڵ㣬�ӣ�10,10������10000,10000������
    // ����������4*4������������ȷ�ֹ8����֪�ڵ��8������ڵ㣬��(1,1),(1,2)...(4,3),(4,4)����
    // ���Ȳ����ڵ�
    public HashMap<Integer,Node> uniformGenerateNodes() {
        // ��һ�¸�֪�ڵ��������ٷ�����Ŀ���
        int copyNum = 0;
        // ÿ��ÿ�зŶ��ٸ����ڵ�
        double posNum = Math.ceil(Math.sqrt(experiment.getTotalCount()));
        // ÿ�и����ڵ��ľ���
        double rowDist = experiment.getExperimentWidth() / posNum;
        double colDist = experiment.getExperimentHeight() / posNum;
        // �����Ÿ�֪�ڵ㣬ż���ű���ڵ�
        for (int i=1; i<=posNum; i++) {
            for (int j=1; j<=posNum; j++) {
                Node node;
                if (j % 2 == 1){
                    node = new LTSensorNode(i * rowDist, j * colDist, StateEnum.ALIVE);
                    node.setId((int) (j + (i - 1) * posNum));
                    // Ϊÿ����֪�ڵ��ʼ����Ҫ���͵����ݰ�����b
                    copyNum = node.initCopyNum(experiment, degreeDistribution);
                    experiment.getNodes().put((int) (j + (i - 1) * posNum), node);
                }
                else{
                    node = new LTCodingNode(i * rowDist, j * colDist, StateEnum.ALIVE);
                    node.setId((int) (j + (i - 1) * posNum));
                    experiment.getNodes().put((int) (j + (i - 1) * posNum), node);
                }
            }
        }

        //System.out.println("��֪�ڵ㿽����"+copyNum);
        // ����key�ǽڵ�������ţ���1��10000
        return (HashMap<Integer, Node>) experiment.getNodes();
    }

    // ��ȫ���������Ӧ�ڵ�,����ʹ��Բ��ʵ������ע�����������Ҫ�ظ�ʵ��
    public HashMap<Integer, Node> randomGenerateNodes() {
        double experimentRadiu = experiment.getExperimentRadiu();
        int totalCount = experiment.getTotalCount();
        for (int i=1; i<=totalCount; i++) {
            double posX = 0;
            double posY = 0;
            // ��ָ����������������ڵ�
            while (true) {
                posX = Math.random();
                posY = Math.random();
                // ������ȷ�Ľڵ�
                if (posX*posX+posY*posY <= experimentRadiu)
                    break;
            }
            if (i % 2 == 1) {
                Node node = new LTSensorNode(posX, posY, StateEnum.ALIVE);
                node.setId(i);
                experiment.getNodes().put(i, node);
            } else {
                Node node = new LTCodingNode(posX, posY, StateEnum.ALIVE);
                node.setId(i);
                experiment.getNodes().put(i, node);
            }
        }
        return (HashMap<Integer, Node>) experiment.getNodes();
    }

    // ���նȷֲ�������ʼ������ڵ�Ķ�
    // ����Ƿ����Ļ�ֻ��Ҫ���ƴ���Ķȷֲ��Ϳ����ˣ���һ�ֱ��뻹�Ǻ�����LT���һ������
    public void initDegree(Map<Integer,Node> nodes) {
        // ��öȷֲ���������
        Map<Double, Range<Double>> probInterval = degreeDistribution.getProbInterval();
        // �������е����нڵ�����
        for (int index : nodes.keySet()) {
            double random = Math.random() * 100;
            // ������������������ĸ��ȸ��������У�Ȼ���Ǹ��ڵ�Ķ���Ϊ��Ӧ��ֵ
            // ע�������и����⣬��Ϊ���ʷֲ������ǽ��Ƶģ�������һ����ȡ����������Ҫ��ȡ�����Ĳ��������������Ѿ������ɸ��ʷֲ������ʱ�����˴���
            for (double probIndex : probInterval.keySet()) {
                Range<Double> range = probInterval.get(probIndex);
                if (range.contains(random)){
                    nodes.get(index).setDegree((int) probIndex);
                    break;
                }
            }
        }
    }

    // ��ʼ���ڵ���ھӽڵ�
    public void initAddNeighbors(Map<Integer, Node> nodes) {
        for (int index : nodes.keySet()) {
            Node node = nodes.get(index);
            node.initAddNeighbors(experiment);
        }
    }

    // ����³�����ӷֲ��ĸ��ʷֲ�����ڵ����̬����
    public void initSteadyState(Map<Integer,Node> nodes,NodeTypeEnum type) {
        // ��öȷֲ�����
        Map<Double, Double> robustSolitonDistribution = degreeDistribution.getRobustSolitonDistribution();
        int totalCount = experiment.getTotalCount();
        // ����Ƿ�������̬���ʷֲ���Ҫ������
        if (type == NodeTypeEnum.PARTITION_LT || type == NodeTypeEnum.LAYER_AND_PARTITION_LT) {
            totalCount = totalCount / Config.PARTITION_NUM;
        }
        double sum = 0;
        for (double index : robustSolitonDistribution.keySet()) {
            Double prob = robustSolitonDistribution.get(index);
            sum += index * prob;
        }
        sum *= totalCount;
        for (int index : nodes.keySet()) {
            double steadyState = nodes.get(index).getDegree() / sum;
            nodes.get(index).setSteadyState(steadyState);
        }
    }

    // ��ʼ������
    public void initPartition(Map<Integer,Node> nodes) {
        // �����ĺ�,ÿ4��һѭ��������4����
        int regionIndex = 1;
        for (int index : nodes.keySet()) {
            if (regionIndex > Config.PARTITION_NUM)
                regionIndex = 1;
            nodes.get(index).setRegion(regionIndex++);
        }

    }

    // ��ʼ���ڵ�ķֲ�
    public void initLayer(Map<Integer,Node> nodes) {
        // ÿ��ÿ�зŶ��ٸ����ڵ�
        double posNum = Math.ceil(Math.sqrt(experiment.getTotalCount()));
        // ÿ�и����ڵ��ľ���
        double rowDist = experiment.getExperimentWidth() / posNum;
        double colDist = experiment.getExperimentHeight() / posNum;
        // ʹ�ö��д洢�������ڵ㣬������ȣ��������
        Queue<Node> queue = new LinkedList<Node>();
        int layIndex = 1;
        for (int index : nodes.keySet()) {
            // ���������ڱ߽��ϵĵ㣬��������x���ڵ�
            if (nodes.get(index).getPosX() == rowDist || nodes.get(index).getPosX() == experiment.getExperimentWidth()
                    || nodes.get(index).getPosY() == colDist || nodes.get(index).getPosY() == experiment.getExperimentHeight()) {
                //�Ѵ�����Ľڵ���뵽������
                nodes.get(index).setLayer(layIndex);
                queue.offer(nodes.get(index));
            }
        }
        //��������еĽڵ㣬ֱ�����нڵ㶼�����Լ��Ĳ��
        while (!queue.isEmpty()) {
            Node curNode = queue.poll();
            List<Node> neighbors = curNode.getNeighbors();
            for (Node node : neighbors) {
                //���node���ھ��ǵ�һ�ν��յ������ź�,���丸����ź��ϼ�1��Ϊ�Լ����źţ�Ȼ����������
                if (node.getLayer() == 0) {
                    node.setLayer(curNode.getLayer()+1);
                    queue.offer(node);
                }
            }
        }
    }
}
