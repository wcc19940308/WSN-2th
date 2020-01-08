package ExperimentCode;

import com.google.common.collect.Range;
import lombok.Getter;
import lombok.Setter;


import java.util.*;

@Getter
@Setter
public abstract class Node {
    // ���ｫ�����ȷŵ����࣬Ȼ���ʼ����ʱ�����������������ж��Ƿ�Ҫ���������2�����Խ��и�ֵ
    int maxDegree; // ��õ�ǰ�ڵ��ھ������Ķ�,���ڼ���ת�Ʊ�
    int layer; // �ֲ�
    int region = 0; // ����,���û�з����Ļ��̶�����0�������Ļ����Ǵ�1��ʼ���з�����
    int id; // �ڵ�ı��
    double steadyState; // ��̬����
    List<Node> neighbors = new ArrayList<Node>(); // �ھӼ���
    List<Integer> packList = new ArrayList<Integer>(); // �Ѿ����յ������ݰ�������ĵĸ�֪�ڵ�ı���б�
    // ���б���������,���sink���յ���data����֪�ڵ�ͱ���ڵ㿪ʼ����׶�data��û��ֵ��
    // TODO ���ܻ�Ҫ���Ľ��������б����ʱ�����жϽڵ��Ƿ���ܹ�������ݰ���
    int data; // �ڵ������洢��������Ϣ��������������
    // �ڵ�����λ��
    double posX;
    double posY;
    StateEnum state; // �ڵ�״̬
    int degree; // Ҫ��Ķ�

    Map<Integer,Double> forwardingTable = new HashMap<Integer, Double>(); // ת�Ʊ�,int��¼�ھӵ�id��double��¼ת�Ƹ���
    Map<Integer, Range<Double>> forwardingTableProbInterval
            = new HashMap<Integer, Range<Double>>(); // ת�Ʊ�ĸ�������
    // ��ϱ����ת�Ʊ�͸�������
    Map<Integer, Double> mixForwardingTable = new HashMap<>();
    Map<Integer, Range<Double>> mixForwardingTableProbInterval
            = new HashMap<>();

    Queue<DecodingPackage> cacheQueue; // ���������ڵ�洢���յ������ݰ�,���ڲ�κ�Ϊ1�Ľڵ��ڽ��г�ʼ��

    int loadBalanceIndex = -1; // �ڵ��ռ��׶�ʱ���͸��ھӽڵ����ѯ���,�����¼���ǵڼ����ھӣ��������ھӵı��


    // ��ʼ������ھ�
    public void initAddNeighbors(Experiment experiment) {
        // ���ÿ���ڵ��ͨ�Ű뾶
        double radius = experiment.getCommunicateRadiu();
        Map<Integer, Node> nodes = experiment.getNodes();
        Node node;
        // ���Լ�Ҳ�����ھӱ��У���Ϊ�Լ�Ҳ�����Լ����ھ�
        for (int nodeId : nodes.keySet()) {
            // �������Լ�����С�ڵ���ͨ�Ű뾶�ģ����뵽�Լ����ھӱ���
            node = nodes.get(nodeId);
            double dis = (Math.sqrt(Math.pow((this.getPosX() - node.getPosX()), 2) +
                    Math.pow((this.getPosY() - node.getPosY()), 2)));
            if (dis <= radius) {
                this.getNeighbors().add(node);
            }
        }
    }

    // ̽���ھӽڵ��Ƿ���Ȼ���,���������ھӴ��ھӱ����Ƴ�
    public void detectNeighbors() {
        // ���Ľڵ�����ʸ�̽���ھӽڵ�
        if (this.state == StateEnum.ALIVE) {
//            for (Node node : neighbors) {
//                if (node.state == StateEnum.DIE) {
//                    neighbors.remove(node);
//                }
//            }
            Iterator<Node> neighborIterator = neighbors.iterator();
            while (neighborIterator.hasNext()) {
                Node node = neighborIterator.next();
                if (node.state == StateEnum.DIE)
                    neighborIterator.remove();
            }
        }
    }
    // ����ھӽڵ������Ķ�,����ת�Ʊ�ļ���
    public int getMaxDegree() {
        detectNeighbors();
        int maxDegree = 0;
        for (Node node : neighbors) {
            if (maxDegree < node.getDegree()) {
                maxDegree = node.getDegree();
            }
        }
        return maxDegree;
    }

    // �Ľ��󣺿��ǽ��ڵ���ھ�������ΪM�������Ǽ������ת�Ʊ��е�M
    public int  getM() {
        detectNeighbors();
        return neighbors.size();
//        return 5000;
    }

    // ����ֱ���ڸ�����д�˷ֲ�����ķ������������õ�ʱ�����ж��Ƿ��Ƿֲ���������ڵ�����Щ����
    public void setLayer(int lay) {
        this.layer = lay;
    }


    public int getLayer() {
        return layer;
    }


    public void setRegion(int region) {
        this.region = region;
    }

    public int getRegion() {
        return region;
    }

    public int initCopyNum(Experiment experiment, DegreeDistribution degreeDistribution, NodeTypeEnum type) {
        return 0;
    }

}
