package ExperimentCode;

import com.google.common.collect.Range;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
// 根据实验要求的信息生成相应的实验场景,并对所有的节点进行初始化的配置

// 初始化位置，id，要求的度，稳态分布
// 根据节点的类型提供分区和分层的方法以供选择
public class SpaceHelper {
    Experiment experiment;
    DegreeDistribution degreeDistribution;
    NodeTypeEnum type;

    // 这里可以控制传入的度分布函数类型，以及要进行的数据编码类型
    public SpaceHelper(Experiment experiment, DegreeDistribution degreeDistribution, NodeTypeEnum type) {
        this.experiment = experiment;
        this.degreeDistribution = degreeDistribution;
        this.type = type;
    }

    /**
     *  按照1000*1000，随机均匀放置5000个感知节点和5000个编码节点,按照(10,10),(10,20)...
     *  从（10,10）到（10000,10000）放置
     *  因为均匀产生节点，因此感知节点和存储节点交替放置，节点编号沿着水平方向开始计数
     *  即 1,2,3,...,100
     *     101,102,103,...,200
     *     ...
     *     901,902,903,...,1000
     *  其中1号节点的坐标为(10,10),1000号节点的坐标为(1000,1000)
     */

    public HashMap<Integer, Node> uniformGenerateNodes() {
        // 看一下感知节点会产生多少分自身的拷贝
        int copyNum = 0;
        // 每行每列放多少个个节点 100
        double posNum = Math.ceil(Math.sqrt(experiment.getTotalCount()));
        // 每行各个节点间的距离
        double rowDist = experiment.getExperimentWidth() / posNum;
        double colDist = experiment.getExperimentHeight() / posNum;
        // 奇数放感知节点，偶数放编码节点
        for (int i = 1; i <= posNum; i++) {
            for (int j = 1; j <= posNum; j++) {
                Node node;
                if (j % 2 == 1) {
                    node = new LTSensorNode(i * rowDist, j * colDist, StateEnum.ALIVE);
                    node.setId((int) (j + (i - 1) * posNum));
                    // 感知节点需要在这里计算需要产生的编码数据包数量b
                    node.initCopyNum(experiment, degreeDistribution, type);
                    // node.getPackList().add(node.getId());
                    experiment.getNodes().put((int) (j + (i - 1) * posNum), node);
                } else {
                    node = new LTCodingNode(i * rowDist, j * colDist, StateEnum.ALIVE);
                    node.setId((int) (j + (i - 1) * posNum));
                    experiment.getNodes().put((int) (j + (i - 1) * posNum), node);
                }
            }
        }

        //System.out.println("感知节点拷贝数"+copyNum);
        // 其中key是节点的索引号，从1到10000
        return (HashMap<Integer, Node>) experiment.getNodes();
    }

    // 完全随机产生相应节点,并且使用圆形实验区域，注意这里可能需要重复实验
    public HashMap<Integer, Node> randomGenerateNodes() {
        double experimentRadiu = experiment.getExperimentRadiu();
        int totalCount = experiment.getTotalCount();
        for (int i = 1; i <= totalCount; i++) {
            double posX = 0;
            double posY = 0;
            // 在指定区域内随机产生节点
            while (true) {
                posX = Math.random();
                posY = Math.random();
                // 产生正确的节点
                if (posX * posX + posY * posY <= experimentRadiu)
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

    // 按照度分布函数初始化编码节点的度
    // 如果是分区的话只需要控制传入的度分布就可以了，第一轮编码还是和正常LT情况一样编码
    public void initDegree(Map<Integer,Node> nodes) {
        // 获得度分布概率区间
        Map<Double, Range<Double>> probInterval = degreeDistribution.getProbInterval();
        // 给网络中的所有节点分配度
        for (int index : nodes.keySet()) {
            double random = Math.random() * 100;
            // 看产生的随机数落在哪个度概率区间中，然后将那个节点的度设为相应的值
            // 注意这里有个问题，因为概率分布区间是近似的，可能有一部分取不到，所以要对取不到的部分区间做处理，已经在生成概率分布区间的时候做了处理
            for (double probIndex : probInterval.keySet()) {
                Range<Double> range = probInterval.get(probIndex);
                if (range.contains(random)){
                    nodes.get(index).setDegree((int) probIndex);
                    break;
                }
            }
        }
    }

    // 初始化节点的邻居节点
    public void initAddNeighbors(Map<Integer, Node> nodes) {
        for (int index : nodes.keySet()) {
            Node node = nodes.get(index);
            node.initAddNeighbors(experiment);
        }
    }

    // 根据鲁棒孤子分布的概率分布计算节点的稳态概率,注意EDFC的稳态概率分式上下都有xd，因此我们直接稳态概率不变
    public void initSteadyState(Map<Integer, Node> nodes, NodeTypeEnum type) {
        // 获得度分布概率
        Map<Double, Double> robustSolitonDistribution = degreeDistribution.getRobustSolitonDistribution();


        int totalCount = experiment.getTotalCount();
//        int totalCount = Config.N; // 这里是为了计算高估N对网络整体的影响

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

    // 获得达到设定的初试步长所需要的随机游走步数
    public int getIntialWalkLength(Map<Integer, Node> nodes, NodeTypeEnum type) {
        Map<Double, Double> robustSolitonDistribution = degreeDistribution.getRobustSolitonDistribution();
        int totalCount = experiment.getTotalCount();
        double sum = 0;
        for (double index : robustSolitonDistribution.keySet()) {
            Double prob = robustSolitonDistribution.get(index);
            sum += index * prob;
        }
        sum *= totalCount; // 这一步计算的就是b * K,即所有感知数据包的数量
        // 如果是EDFC，那么计算初始随机游走步长时需要乘上冗余系数
        if (type == NodeTypeEnum.EDFC || type == NodeTypeEnum.MOWELFC || type == NodeTypeEnum.MOWOELFC || type == NodeTypeEnum.MOW_LT) sum *= Config.REDUNDANCY;
        return (int) (sum * Config.WALK_LENGTH);
    }

//    // 初始化分区
    public void initPartition(Map<Integer,Node> nodes) {
        // 分区的号,每4个一循环，即分4个区
        int regionIndex = 1;
        for (int index : nodes.keySet()) {
            if (regionIndex > Config.PARTITION_NUM)
                regionIndex = 1;
            nodes.get(index).setRegion(regionIndex++);
        }
    }

    // 初始化节点的分层
    public void initLayer(Map<Integer,Node> nodes) {
        int layerCount = 0;
        // 每行每列放多少个个节点
        double posNum = Math.ceil(Math.sqrt(experiment.getTotalCount()));
        // 每行各个节点间的距离 10
        double rowDist = experiment.getExperimentWidth() / posNum;
        double colDist = experiment.getExperimentHeight() / posNum;
        // 使用队列存储所有外层节点，广度优先，逐个处理
        Queue<Node> queue = new LinkedList<>();
        int layIndex = 1;
        for (int index : nodes.keySet()) {
            /**
             * 如果坐标等于边界上的点，则视作最x外层节点,即如下点：
             * (10,10) (20,10) (30,10) ... (1000,10)
             * (10,10) (10,20) (10,30) ... (10,1000)
             * (10,1000) (20,1000) (30,1000) ... (1000,1000)
             * (1000,10) (1000,20) (1000,30) ... (1000,1000)
             * 即横坐标等于10或者1000，以及纵坐标等于10或者1000的节点
             */

            if (nodes.get(index).getPosX() == rowDist || nodes.get(index).getPosX() == experiment.getExperimentWidth()
                    || nodes.get(index).getPosY() == colDist || nodes.get(index).getPosY() == experiment.getExperimentHeight()) {
                //把处理过的节点加入到队列中
                nodes.get(index).setLayer(layIndex);
                queue.offer(nodes.get(index));
                layerCount++;
            }
        }
        System.out.println("最外层一共有" + layerCount + "个节点");
        //处理队列中的节点，直达所有节点都有了自己的层号
        while (!queue.isEmpty()) {
            Node curNode = queue.poll();
            List<Node> neighbors = curNode.getNeighbors();
            for (Node node : neighbors) {
                //如果node的邻居是第一次接收到激发信号,则将其父类的信号上加1作为自己的信号，然后加入队列中
                if (node.getLayer() == 0) {
                    node.setLayer(curNode.getLayer() + 1);
                    queue.offer(node);
                }
            }
        }
    }
}
