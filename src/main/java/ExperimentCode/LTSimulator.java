package ExperimentCode;

import com.google.common.collect.Range;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Getter
@Setter
// 模拟开始的编码行为
public class LTSimulator {
    SpaceHelper spaceHelper;
    Map<Integer, CodingPackage> packageInfo; // 网络中整个的数据包情况,记录数据包编号和数据包具体信息
    Map<Integer, MixCodingPackage> mixPackageInfo; // 网络中用于二次编码的数据包的情况
    Map<Integer, Node> nodes;
    public double walkLength = 0;
    // 二次转发步长
    public double REDUNDANCY_WALK_LENGTH = 0;
    // 传入初始化场景的布置
    public LTSimulator(SpaceHelper spaceHelper) {
        this.spaceHelper = spaceHelper;
        this.packageInfo = new HashMap<>();
        this.mixPackageInfo = new HashMap<>();
        this.nodes = spaceHelper.getExperiment().getNodes(); // 实验场景中各个节点的信息情况

        init(spaceHelper.getType());
    }
    // 做好开始编码前的所有准备
    public void init(NodeTypeEnum type) {
        HashMap<Integer, Node> nodes = spaceHelper.uniformGenerateNodes();
        // 将每个节点按照度分布分配相应的度
        spaceHelper.initDegree(nodes);
        // 初始化每个节点的邻居
        spaceHelper.initAddNeighbors(nodes);
        // 稳态分布
        spaceHelper.initSteadyState(nodes,type);
        // 网络整体需要进行的一次步长,应该是网络中真正生成的编码数据包的数量 * 每个编码数据包的步长设定
        walkLength = spaceHelper.getIntialWalkLength(nodes, type);

        // 分层
        if (spaceHelper.getType() == NodeTypeEnum.LAYER_LT || spaceHelper.getType() == NodeTypeEnum.LAYER_AND_PARTITION_LT || spaceHelper.getType() == NodeTypeEnum.NORMAL_BY_LAYER_LT

                || spaceHelper.getType() == NodeTypeEnum.MOWELFC || spaceHelper.getType() == NodeTypeEnum.MOWOELFC
                || spaceHelper.getType() == NodeTypeEnum.MRFELFC || spaceHelper.getType() == NodeTypeEnum.MRFOELFC) {

            spaceHelper.initLayer(nodes);
        }
        // 产生数据包
        generatePackage();
        // TODO 以下这两个方法在编码过程中还应该要时刻运行
        // TODO 转移表这里是需要重新生成实验的地方，因为生成的转移表可能不符合现实条件，即总概率可能大于1
        // 初始化或者刷新转移表以及概率区间
        initOrRefreshForwardingTable(type);
        initOrRefreshForwardingTableProbInterval();
        // 数据包进行游走编码存储
        if (type == NodeTypeEnum.EDFC

                || type == NodeTypeEnum.MOWELFC || type == NodeTypeEnum.MOWOELFC
                || type == NodeTypeEnum.MOW_LT ) {

            EDFCSingleCastEncoding2();
        } else {
            singlecastEncoding();
        }
    }

    // 从感知节点产生数据包
    public void generatePackage() {
        // 数据包编号从1开始
        int packageId = 1;
        int sensorCount = 0;
        // 对于网络中每个感知节点产生b个数据包
        for (int nodeId : nodes.keySet()) {
            // 如果是感知节点,那么对于这个感知节点产生b个数据包,并且赋上相应的分区号
            if (nodes.get(nodeId) instanceof LTSensorNode) {
                sensorCount++;
                // 注意这里会根据不同的编码策略产生不同数量的编码数据包
                int copyNum = ((LTSensorNode) nodes.get(nodeId)).getCopyNum();
                int regionIndex = (nodes.get(nodeId)).getRegion();
                for (int i = 0; i < copyNum; i++) {
                    CodingPackage pack = new CodingPackage(nodeId, nodeId, regionIndex
                            , Config.WALK_LENGTH, 0, StateEnum.ALIVE);
                    packageInfo.put(packageId++, pack);
                }
            }
        }
        System.out.println("一共有" + sensorCount + "个感知节点用于产生数据包");
        System.out.println("一共有" + packageInfo.size() + "个编码数据包");
    }

    // 初始化(更新)所有节点各自的转移表和概率区间
    public void initOrRefreshForwardingTable(NodeTypeEnum type) {
        for (int nodeId : nodes.keySet()) {
            Node curNode = nodes.get(nodeId);
            // 对于存活的节点才更新转移表
            if (curNode.state == StateEnum.ALIVE) {
                int curId = curNode.getId();
                // 获得自己的转移表
                Map<Integer, Double> forwardingTable = curNode.getForwardingTable();
                // 注意这里有一步更新的操作,是用于从新探测网络中的节点情况，并更新转移表
                forwardingTable.clear();
                // 初始化的时候先探测一下自己的邻居
                curNode.detectNeighbors();
                List<Node> neighbors = curNode.getNeighbors();
                // 先计算出所有的非自身转移的概率
                double notSelfProb = 0;
                for (Node neighbor : neighbors) {
                    int neighborId = neighbor.getId();
                    // 如果不是是到自身的转移
                    if (neighborId != curId) {
                        double prob =  Math.min(1, neighbor.getSteadyState() / curNode.getSteadyState()) / curNode.getM();
//                        if (type == NodeTypeEnum.MOWOELFC || type == NodeTypeEnum.MOWELFC || type == NodeTypeEnum.MOW_LT) {
//                            // 概率转移表的设定
//                            prob = Math.min(1, neighbor.getSteadyState() / curNode.getSteadyState()) / 5000;
//                        } else if (type == NodeTypeEnum.MRFELFC || type == NodeTypeEnum.MRFOELFC || type == NodeTypeEnum.MRF_LT) {
//                            prob = Math.min(1, neighbor.getSteadyState() / curNode.getSteadyState()) / curNode.getM();
//                        }
//                        double prob = Math.min(1 / curNode.getDegree(), neighbor.getSteadyState() / (neighbor.getDegree() * curNode.getSteadyState()));
                        notSelfProb += prob;
                        forwardingTable.put(neighborId, prob);
                    }
                }
                // TODO 这里可能有误差，应该是如果notSelfProb的值大于1，则推翻重新生成转移表
                // 如果是到自身的转移
                if (notSelfProb >= 1) {
                    forwardingTable.put(curId, 0.0);
                } else {
                    forwardingTable.put(curId, 1 - notSelfProb);
                }
            }
        }
    }

    public void initOrRefreshForwardingTableProbInterval() {
        for (int nodeId : nodes.keySet()) {
            Node curNode = nodes.get(nodeId);
            // 对于存活的节点才更新转移表概率区间
            if (curNode.state == StateEnum.ALIVE) {
                // 获得自己的转移表和转移表概率区间
                Map<Integer, Double> forwardingTable = curNode.getForwardingTable();
                Map<Integer, Range<Double>> forwardingTableProbInterval
                        = curNode.getForwardingTableProbInterval();
                double start = 0;
                double end = 0;
                for (int index : forwardingTable.keySet()) {
                    double prob = forwardingTable.get(index);
                    end = prob * 100 + start;
                    forwardingTableProbInterval.put(index, Range.closedOpen(start, end));
                    start = end;
                }
            }
        }
    }

    // 初始化或者刷新所有节点的混合编码的转移表和概率区间
    public void initOrRefreshMixForwardingTable() {
        for (int nodeId : nodes.keySet()) {
            Node curNode = nodes.get(nodeId);
            // 对于存活的节点才更新转移表
            if (curNode.state == StateEnum.ALIVE) {
                int curId = curNode.getId();
                // 获得自己的转移表
                Map<Integer, Double> mixForwardingTable = curNode.getMixForwardingTable();
                // 注意这里有一步更新的操作,是用于从新探测网络中的节点情况，并更新转移表
                mixForwardingTable.clear();
                // 初始化的时候先探测一下自己的邻居
                curNode.detectNeighbors();
                List<Node> neighbors = curNode.getNeighbors();
                // 先计算出所有的非自身转移的概率
                double notSelfProb = 0;
                for (Node neighbor : neighbors) {
                    int neighborId = neighbor.getId();
                    // 如果不是是到自身的转移
                    if (neighborId != curId) {
                        double prob = 1.0 / (curNode.getM() - 1);
                        mixForwardingTable.put(neighborId, prob);
                    }
                }
            }
        }
    }

    public void initOrRefreshMixForwardingTableProbInterval() {
        for (int nodeId : nodes.keySet()) {
            Node curNode = nodes.get(nodeId);
            // 对于存活的节点才更新转移表概率区间
            if (curNode.state == StateEnum.ALIVE) {
                // 获得自己的转移表和转移表概率区间
                Map<Integer, Double> mixForwardingTable = curNode.getMixForwardingTable();
                Map<Integer, Range<Double>> mixForwardingTableProbInterval
                        = curNode.getMixForwardingTableProbInterval();
                double start = 0;
                double end = 0;
                for (int index : mixForwardingTable.keySet()) {
                    double prob = mixForwardingTable.get(index);
                    end = prob * 100 + start;
                    mixForwardingTableProbInterval.put(index, Range.closedOpen(start, end));
                    start = end;
                }
            }
        }
    }

    // 单播随机游走第一轮编码
    public void singlecastEncoding() {
        // 遍历每个数据包进行随机游走，直至耗尽步数
        for (int packId : packageInfo.keySet()) {
            CodingPackage curCodingPackage = packageInfo.get(packId);
            int step = curCodingPackage.getStep();
            int walkLength = curCodingPackage.getWalkLength();
            // 如果还没有走够设定的步长
            while (step < walkLength && curCodingPackage.getState() == StateEnum.ALIVE) {
                // 得到数据包当前所在位置
                int curId = curCodingPackage.getCurId();
                Node curNode = nodes.get(curId);
                Map<Integer, Range<Double>> forwardingTableProbInterval
                        = curNode.getForwardingTableProbInterval();
                // 产生随机数，来选择需要传送数据包的节点
                double random = Math.random() * 100;
                for (int neighborId : forwardingTableProbInterval.keySet()) {
                    Range<Double> range = forwardingTableProbInterval.get(neighborId);
                    if (range.contains(random)){
                        // 用选择的邻居的信息更新数据包
                        curCodingPackage.setCurId(neighborId);
                        curCodingPackage.setStep(++step);
                        // 如果步数已经用尽了，那么直接在这个邻居上将数据包存储下来
                        if (step == walkLength) {
                            Node node = nodes.get(neighborId);
                            List<Integer> packList = node.getPackList();
                            // 先看检查最后这个节点的分区是否符合数据包的要求
                            // 如果是的话，再看最后停留的这个节点是否已经收到过这个数据包的信息
                            // 如果是的话再将他转送给邻居，即将step+1让他继续游走即可,这样做是为了提高数据包的利用率
                            if (curCodingPackage.getRegionIndex() == node.getRegion()) {
                                //for (int i = 0; i < packList.size(); i++) {
                                if (packList.contains(curCodingPackage.getSensorId())) {
                                    REDUNDANCY_WALK_LENGTH++;
                                    --step;
                                    break;
                                }
                                // 如果这个节点即没有达到指定的度，又没有收到过这个数据包，那么接收这个数据包
                                // 这里需要再次判断step的值，因为前面可能减过了
                                if (step == walkLength) {
                                    // 如果没有收到过这个数据包的信息则接受该数据包并进行编码
                                    packList.add(curCodingPackage.getSensorId());
                                    node.setData(node.getData() ^ curCodingPackage.getSensorId());
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    // EDFC的单播随机游走编码策略
    public void EDFCSingleCastEncoding() {
        // 遍历每个数据包进行随机游走，直至耗尽步数
        for (int packId : packageInfo.keySet()) {
            CodingPackage curCodingPackage = packageInfo.get(packId);
            int step = curCodingPackage.getStep();
            int walkLength = curCodingPackage.getWalkLength();
            // 如果还没有走够设定的步长
            while (step < walkLength && curCodingPackage.getState() == StateEnum.ALIVE) {
                // 数据包当前位置
                int curId = curCodingPackage.getCurId();
                Node curNode = nodes.get(curId);
                Map<Integer, Range<Double>> forwardingTableProbInterval
                        = curNode.getForwardingTableProbInterval();
                // 产生随机数，来选择需要传送数据包的节点
                double random = Math.random() * 100;
                for (int neighborId : forwardingTableProbInterval.keySet()) {
                    Range<Double> range = forwardingTableProbInterval.get(neighborId);
                    if (range.contains(random)) {
                        // 用选择的邻居的信息更新数据包
                        curCodingPackage.setCurId(neighborId);
                        curCodingPackage.setStep(++step);
                        // 如果步数已经用尽了，那么直接在这个邻居上将数据包存储下来
                        if (step == walkLength) {
                            Node node = nodes.get(neighborId);
                            List<Integer> packList = node.getPackList();
                            // 不满足度的要求且之前没有接收过，才进行编码。但是存是都要存的
                            if (!checkDegree(node) && !packList.contains(curCodingPackage.getSensorId())) {
                                node.setData(node.getData() ^ curCodingPackage.getSensorId());
                            }
                            // 因为是EDFC，所以数据包直接停留并接收
                            packList.add(curCodingPackage.getSensorId());
                        }
                        // 注意！这里这个break是为了保证如果找到了合适的邻居节点能够跳出对邻居节点列表的轮询
                        break;
                    }
                }
            }
        }
    }

    // EDFC的单播随机游走编码策略2
    public void EDFCSingleCastEncoding2() {
        // 遍历每个数据包进行随机游走，直至耗尽步数
        for (int packId : packageInfo.keySet()) {
            CodingPackage curCodingPackage = packageInfo.get(packId);
            int step = curCodingPackage.getStep();
            int walkLength = curCodingPackage.getWalkLength();
            // 如果还没有走够设定的步长
            while (step < walkLength && curCodingPackage.getState() == StateEnum.ALIVE) {
                // 数据包当前位置
                int curId = curCodingPackage.getCurId();
                Node curNode = nodes.get(curId);
                Map<Integer, Range<Double>> forwardingTableProbInterval
                        = curNode.getForwardingTableProbInterval();
                // 产生随机数，来选择需要传送数据包的节点
                double random = Math.random() * 100;
                for (int neighborId : forwardingTableProbInterval.keySet()) {
                    Range<Double> range = forwardingTableProbInterval.get(neighborId);
                    if (range.contains(random)) {
                        // 用选择的邻居的信息更新数据包
                        curCodingPackage.setCurId(neighborId);
                        curCodingPackage.setStep(++step);
                        // 如果步数已经用尽了，那么直接在这个邻居上将数据包存储下来
                        if (step == walkLength) {
                            Node node = nodes.get(neighborId);
                            if (checkDegree(node)) {
                                curCodingPackage.setState(StateEnum.DIE);
                                break;
                            } else {
                                // 如果该节点已经有了这个数据包的信息了，那么丢弃数据包
                                if (node.getPackList().contains(curCodingPackage.getSensorId())) {
                                    curCodingPackage.setState(StateEnum.DIE);
                                } else {
                                    node.getPackList().add(curCodingPackage.getSensorId());
                                    node.setData(node.getData() ^ curCodingPackage.getSensorId());
                                    curCodingPackage.setState(StateEnum.DIE);
                                }
                                break;
                            }
                        }
                        // 为了保证选择完一个邻居后直接及时退出
                        break;
                    }
                }
            }
        }
    }


    // 冗余的单播随机游走第一轮编码
    public void redundancySingleCastCoding() {
        for (int packId : packageInfo.keySet()) {
            CodingPackage curCodingPackage = packageInfo.get(packId);
            int step = curCodingPackage.getStep();
            int walkLength = curCodingPackage.getWalkLength();
            while (step < walkLength && curCodingPackage.getState() == StateEnum.ALIVE) {
                // 得到数据包当前所在位置
                int curId = curCodingPackage.getCurId();
                Node curNode = nodes.get(curId);
                Map<Integer, Range<Double>> forwardingTableProbInterval
                        = curNode.getForwardingTableProbInterval();
                // 产生随机数，来选择需要传送编码数据包的邻居节点
                double random = Math.random() * 100;
                for (int neighborId : forwardingTableProbInterval.keySet()) {
                    Range<Double> range = forwardingTableProbInterval.get(neighborId);
                    if (range.contains(random)) {
                        // 用选择的邻居的信息更新数据包
                        curCodingPackage.setCurId(neighborId);
                        curCodingPackage.setStep(++step);
                        Node node = nodes.get(neighborId);
                        if (checkDegree(node)) {
                            curCodingPackage.setState(StateEnum.DIE);
                        } else {
                            // 如果该节点已经有了这个数据包的信息了，那么丢弃数据包
                            if (node.getPackList().contains(curCodingPackage.getSensorId())) {
                                curCodingPackage.setState(StateEnum.DIE);
                            } else {
                                node.getPackList().add(curCodingPackage.getSensorId());
                                node.setData(node.getData() ^ curCodingPackage.getSensorId());
                                curCodingPackage.setState(StateEnum.DIE);
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    // 检查节点是否已经获得了指定的度，如果是，返回true，否则返回false
    public boolean checkDegree(Node node) {
//        if (node.getDegree() == node.getPackList().size()) {
//            return true;
//        } else {
//            return false;
//        }
        if (node.getDegree() > node.getPackList().size()) {
            return false;
        } else {
            return true;
        }
    }

    // 单播随机游走第二轮混合编码,选择各区最高度的节点产生数据包进行编码，以保证网络整体中的k/R处有尖峰存在
    public void mixCoding (Map<Integer,Node> nodes){
        // 获得分区数
        int partitionNum = Config.PARTITION_NUM;
        // 获得分区中的尖峰值
        double peak = spaceHelper.getDegreeDistribution().getPeak();
        double peakProb = spaceHelper.getDegreeDistribution().getPeakProb();
        // 获得每个分区需要用于编码的尖峰值节点的个数
        int peakNum = (int) (spaceHelper.getExperiment().getTotalCount() * peakProb) / 2;
        // 产生混合编码包
        generateMixPackage(peakNum, peak);
        for (int packId : mixPackageInfo.keySet()) {
            MixCodingPackage curMixCodingPackage = mixPackageInfo.get(packId);
            int step = curMixCodingPackage.getStep();
            int walkLength = curMixCodingPackage.getWalkLength();
            while (step < walkLength && curMixCodingPackage.getState() == StateEnum.ALIVE) {
                int curId = curMixCodingPackage.getCurId();
                Node curNode = nodes.get(curId);
                Map<Integer, Range<Double>> mixForwardingTableProbInterval
                        = curNode.getMixForwardingTableProbInterval();
                double random = Math.random() * 100;
                for (int neighborId : mixForwardingTableProbInterval.keySet()) {
                    Range<Double> range = mixForwardingTableProbInterval.get(neighborId);
                    if (range.contains(random)) {
                        curMixCodingPackage.setCurId(neighborId);
                        curMixCodingPackage.setStep(++step);
                        Node neighborNode = nodes.get(neighborId);
                        // 如果数据包的分区与节点的分区不同，并且这个节点没有收到过混合数据包
                        if (neighborNode.getRegion() != curMixCodingPackage.getRegionIndex()
                                && (neighborNode.getPackList().size() == peak)) {
                            // 把数据包中的信息存到节点中
                            List<Integer> packList = neighborNode.getPackList();
                            packList.addAll(curMixCodingPackage.getDataList());
                            // 数据包已经被接受了
                            curMixCodingPackage.setState(StateEnum.DIE);
                            break;
                        }
                    }
                }
            }
            // curMixCodingPackage.setState(StateEnum.DIE);
        }
    }

    public void generateMixPackage (int peakNum,double peak){
        // 用于记录各区的尖峰节点是否已经取完了
        int[] packCount = new int[Config.PARTITION_NUM + 1];
        // 在所有节点中不重复随机选择
        int[] randoms = Utils.getRandoms(1, nodes.size(), nodes.size());
        int packageId = 1;
        for (int i = 0; i < randoms.length; i++) {
            // 如果各个区的数据包都已经全部产生了，那么返回
            if (packCount[1] == peakNum && packCount[2] == peakNum
                    && packCount[3] == peakNum && packCount[4] == peakNum) {
                return;
            }
            Node curNode = nodes.get(randoms[i]);
            // 如果当前节点的度是峰值的(这里可以用偏离peak值来代替)
            //if (curNode.getDegree() == peak) {
            if (Math.abs(peak - curNode.getDegree()) <= Config.OFFSET * peak) {
                int regionIndex = curNode.getRegion();
                if (regionIndex >= 1 && regionIndex <= Config.PARTITION_NUM && packCount[regionIndex] != peakNum) {
                    // 这里存的是对节点的packList的引用，如果节点被破坏了，这里可能就是null了
                    MixCodingPackage mixCodingPackage = new MixCodingPackage(curNode.getId(), regionIndex,
                            Config.MIX_WALK_LENGTH, 0, curNode.getPackList(), StateEnum.ALIVE);
                    mixPackageInfo.put(packageId++, mixCodingPackage);
                }
            }
        }
    }

}
