package ExperimentCode;

import com.google.common.collect.Range;
import lombok.Getter;
import lombok.Setter;


import java.util.*;

@Getter
@Setter
public abstract class Node {
    // 这里将属性先放到父类，然后初始化的时候根据子类的类型来判断是否要将该类的这2个属性进行赋值
    int maxDegree; // 获得当前节点邻居中最大的度,用于计算转移表
    int layer; // 分层
    int region = 0; // 分区,如果没有分区的话固定就是0，分区的话就是从1开始进行分区的
    int id; // 节点的编号
    double steadyState; // 稳态概率
    List<Node> neighbors = new ArrayList<Node>(); // 邻居集合
    List<Integer> packList = new ArrayList<Integer>(); // 已经接收到的数据包所代表的的感知节点的编号列表
    // 进行编码后的数据,最后sink接收的是data，感知节点和编码节点开始编码阶段data都没有值。
    // TODO 可能还要做改进，即进行编码的时候先判断节点是否接受过这个数据包。
    int data; // 节点真正存储的数据信息，即编码后的数据
    // 节点所在位置
    double posX;
    double posY;
    StateEnum state; // 节点状态
    int degree; // 要求的度

    Map<Integer,Double> forwardingTable = new HashMap<Integer, Double>(); // 转移表,int记录邻居的id，double记录转移概率
    Map<Integer, Range<Double>> forwardingTableProbInterval
            = new HashMap<Integer, Range<Double>>(); // 转移表的概率区间
    // 混合编码的转移表和概率区间
    Map<Integer, Double> mixForwardingTable = new HashMap<>();
    Map<Integer, Range<Double>> mixForwardingTableProbInterval
            = new HashMap<>();

    Queue<DecodingPackage> cacheQueue; // 用于最外层节点存储接收到的数据包,对于层次号为1的节点在进行初始化

    int loadBalanceIndex = -1; // 节点收集阶段时传送给邻居节点的轮询序号,这里记录的是第几个邻居，而不是邻居的编号


    // 初始化添加邻居
    public void initAddNeighbors(Experiment experiment) {
        // 获得每个节点的通信半径
        double radius = experiment.getCommunicateRadiu();
        Map<Integer, Node> nodes = experiment.getNodes();
        Node node;
        // 把自己也加入邻居表中，因为自己也算是自己的邻居
        for (int nodeId : nodes.keySet()) {
            // 对于与自己距离小于等于通信半径的，加入到自己的邻居表中
            node = nodes.get(nodeId);
            double dis = (Math.sqrt(Math.pow((this.getPosX() - node.getPosX()), 2) +
                    Math.pow((this.getPosY() - node.getPosY()), 2)));
            if (dis <= radius) {
                this.getNeighbors().add(node);
            }
        }
    }

    // 探测邻居节点是否依然存活,将不存活的邻居从邻居表中移除
    public void detectNeighbors() {
        // 存活的节点才有资格探测邻居节点
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
    // 获得邻居节点中最大的度,用于转移表的计算
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

    // 改进后：考虑将节点的邻居数量作为M，这里是计算概率转移表中的M
    public int  getM() {
        detectNeighbors();
        return neighbors.size();
//        return 5000;
    }

    // 这里直接在父类中写了分层分区的方法，真正调用的时候先判断是否是分层分区类型在调用这些方法
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
