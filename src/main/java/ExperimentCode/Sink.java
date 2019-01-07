package ExperimentCode;

import lombok.Getter;
import lombok.Setter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static ExperimentCode.Config.DESTORY_RATIO;

@Getter
@Setter
public class Sink {
    // 已经解码出的源数据,即1度数据包集合
    Set<Integer> oneDegreeData = new TreeSet<Integer>();
    // 凭当前已知信息还不能解码的数据集合，即大于1度的数据包集合,按度从小到大的顺序排列
    Set<List<Integer>> highDegreeData;
    List<Node> outerNodes = new ArrayList<Node>(); // 外层节点的列表
    LTSimulator simulator;
    Map<Integer, Integer> decodingRatio = new HashMap<Integer, Integer>(); // 恢复速率比，即收到编码包和已经解码源数据之间的关系
    int packageNum = 0; // 已经收到的编码包的数量
    int decodingPackageNum = 0; // 解码数据包的数量

    public Sink(LTSimulator simulator) {
        this.simulator = simulator;
        highDegreeData = new TreeSet<List<Integer>>(new Comparator<List<Integer>>() {
            public int compare(List<Integer> o1, List<Integer> o2) {
                if (o1.size() == o2.size()) {
                    return o1.hashCode() - o2.hashCode();
                } else {
                    return o1.size() - o2.size();
                    return;
                }
            }
        });
    }

    public boolean collectPackage(NodeTypeEnum type) {
        boolean flag = false;
        // 存储了一段时间，有些节点遭到破坏
        randomDestory();
        if (type == NodeTypeEnum.LAYER_LT || type == NodeTypeEnum.LAYER_AND_PARTITION_LT) {
            boost();
            sendDecodingPackage();
            flag = layerDecode();
        } else if (type == NodeTypeEnum.LT || type==NodeTypeEnum.PARTITION_LT) {
            flag = normalDecode();
        } else if (type == NodeTypeEnum.NORMAL_BY_LAYER_LT) {
            boost();
            sendRandomDecodingPackage();
            flag = normalLayerDecode();
        }
        return flag;
    }

    // 模拟向网络中的节点发送激发信号，其实这里就相当于给层号为1的节点建立缓冲的优先队列
    public void boost() {
        Map<Integer, Node> nodes = simulator.getNodes();
        for (int nodeId : nodes.keySet()) {
            Node curNode = nodes.get(nodeId);
            // 如果是最外层的节点，那么就需要有一个存储有内层节点传送过来的用于解码阶段的数据包信息
            if (curNode.getLayer() == 1) {
                curNode.cacheQueue = new LinkedList<DecodingPackage>();
                // 在Sink的列表中添加层数为1的节点
                outerNodes.add(curNode);
            }
        }
        System.out.println("一共有"+outerNodes.size()+"个最外层节点");
    }

    // 解码阶段
    // 先将全部的节点上存储的信息以数据包的形式传送到最外层节点上
    // 然后由sink节点从最外层节点上收集相应的数据包


    // 将全部节点上存储的信息以低度包高度的数据包的形式传送到最外层节点上
    public void sendDecodingPackage() {
        Map<Integer, Node> nodes = simulator.getNodes();
        int maxDegree = simulator.getSpaceHelper().getExperiment().getSensorCount();
        // 按度从低到高遍历所有节点，并按顺序将数据包传递到最外层节点上
        for (int i=1; i<=maxDegree; i++) {
            for (int nodeId : nodes.keySet()) {
                Node curNode = nodes.get(nodeId);
                // 当前节点的邻居轮询序号
                //int index = curNode.getLoadBalanceIndex();
                // 如果当前节点存储的度为需要的度，则将他的数据用数据包形式沿层向外层节点传递
                // 这里的度是节点真实的度，即收到的数据包的总数
                if (curNode.getPackList().size() == i) {
                    // 如果是需要的度，则产生需要传送给最外层节点的解码数据包,解码数据包的data队列直接引用node的队列就可以了
                    DecodingPackage decodingPackage =
                            new DecodingPackage(i, curNode.getData(), curNode.getId(),curNode.getPackList());
                    decodingPackageNum++;
                    // 只要当前数据包所处的节点的位置的层数不为1，那么一直往外层传，直到传到层数为1的节点上为止
                    while (nodes.get(decodingPackage.getCurId()).layer != 1) {
                        Node currentNode = nodes.get(decodingPackage.getCurId());
                        currentNode.detectNeighbors();
                        List<Node> neighbors = currentNode.getNeighbors();
                        // 当前节点的邻居轮询序号
                        int loadBalanceIndex = currentNode.getLoadBalanceIndex();
                        // 轮询调度周围的邻居节点，查找需要传送的节点，负载均匀
                        // count记录了已经轮询了的邻居数量，如果查遍了所有的邻居节点都找不到，则表示这个数据包无法传送到sink
                        int count = 0;
                        int size = neighbors.size();
                        while (count != size) {
                            loadBalanceIndex = (loadBalanceIndex + 1) % size;
                            Node neighborNode = neighbors.get(loadBalanceIndex);
                            if (neighborNode.getLayer() == currentNode.getLayer() - 1) {
                                decodingPackage.setCurId(neighborNode.getId());
                                currentNode.setLoadBalanceIndex(loadBalanceIndex);
                                break;
                            }
                            count++;
                        }
                    }
                    // 如果当前节点已经是最外层的节点了,那么直接把数据包存下即可
                    if (nodes.get(decodingPackage.getCurId()).layer == 1) {
                        nodes.get(decodingPackage.getCurId()).getCacheQueue().offer(decodingPackage);
                    }
                }
            }
        }

    }

    // 将节点上的数据包随机发向最外层节点
    public void sendRandomDecodingPackage() {
        Map<Integer, Node> nodes = simulator.getNodes();
        for (int nodeId : nodes.keySet()) {
            Node curNode = nodes.get(nodeId);
            DecodingPackage decodingPackage =
                    new DecodingPackage(curNode.getPackList().size(), curNode.getData(), curNode.getId(), curNode.getPackList());
            while (nodes.get(decodingPackage.getCurId()).layer != 1) {
                Node currentNode = nodes.get(decodingPackage.getCurId());
                currentNode.detectNeighbors();
                List<Node> neighbors = currentNode.getNeighbors();
                int loadBalanceIndex = currentNode.getLoadBalanceIndex();
                int count = 0;
                int size = neighbors.size();
                while (count != size) {
                    loadBalanceIndex = (loadBalanceIndex + 1) % size;
                    Node neighborNode = neighbors.get(loadBalanceIndex);
                    if (neighborNode.getLayer() == currentNode.getLayer() - 1) {
                        decodingPackage.setCurId(neighborNode.getId());
                        currentNode.setLoadBalanceIndex(loadBalanceIndex);
                        break;
                    }
                    count++;
                }
            }
            // 如果当前节点已经是最外层的节点了,那么直接把数据包存下即可
            if (nodes.get(decodingPackage.getCurId()).layer == 1) {
                nodes.get(decodingPackage.getCurId()).getCacheQueue().offer(decodingPackage);
            }
        }
    }

    // 解码阶段,返回是否解码成功,此方法用于对于分层的解码
    public boolean layerDecode() {
        // 输出最外层节点中存储的编码包的数量，即最后会发送的总的数据包数量
        int num = 0;
        for (Node node : outerNodes) {
            num += node.getCacheQueue().size();
        }
        System.out.println("在解码之前，最外层节点中一共存储了"+num+"个数据包");
        System.out.println("一共有"+decodingPackageNum+"个解码用的编码数据包");

        int maxDegree = simulator.getSpaceHelper().getExperiment().getSensorCount();
        //int index = 1;
        // 从1度包向高度包一个个解
        for (int i=1; i<=maxDegree; i++) {
            for (Node curNode : outerNodes) {
                Queue<DecodingPackage> cacheQueue = curNode.getCacheQueue();
                // 这里主要是为了防止最外层节点如果没有存储数据而引发异常
                if (cacheQueue.size() == 0) {
                    continue;
                }
                // 因为开始传递的时候是按低度到高度的，因此队列中低度的数据一定是在队列前端的
                while ((cacheQueue.size() != 0) && (cacheQueue.peek().getDegree() == i)) {
                    // 如果解出数据包的集合长度达到了感知节点的数据包数量k，那么表示已经恢复出所有的原始数据了
                    if (oneDegreeData.size() == maxDegree) {
                        System.out.println("分区的成功恢复源数据，当前收到编码包数量为:"+packageNum);
                        return true;
                    }
                    // 如果是1度的包，那么直接加入到已解出的集合中
                    if (i == 1) {
                        oneDegreeData.add(cacheQueue.poll().getData());
                        //decodingRatio.put(++packageNum, oneDegreeData.size());
                    } else {
                        // 取出最外层节点中存储的数据包，构成数据包的数据列表，如果其中n-1个数据包sink都已经有了，那么
                        DecodingPackage pack = cacheQueue.poll();
                        List<Integer> dataList = pack.getDataList();
                        // 对于数据包中携带的每个感知节点的信息，如果其中有n-1个在已恢复的集合列表中，那个该数据包就是可以恢复的
                        // 并且把剩下的那1个也加入到恢复的集合列表中
                        int count = 0;
                        // 因为会消解dataList中已有的数据包，导致dataList改变，所以这里先记录一下之前的长度
                        int length = dataList.size();
                        Iterator<Integer> dataIterator = dataList.iterator();
                        // 用已知信息消解数据包
                        while (dataIterator.hasNext()) {
                            Integer next = dataIterator.next();
                            if (oneDegreeData.contains(next)) {
                                dataIterator.remove();
                                count++;
                            }
                        }
                        // 如果count刚好等于n-1，则表示该数据包可以被解出
                        if (count == length - 1) {
                            oneDegreeData.add(dataList.get(0));
                            //decodingRatio.put(++packageNum, oneDegreeData.size());
                            // 如果又解出了新的1度包，那么就先去消解待解数据包集合中的数据
                            decodeHighDegreeData();
                            // 如果count小于n-1，则表示该数据包解不出
                            // 先用已知数据对数据包进行消解，再加入待解数据包集合
                        } else if (count < dataList.size() - 1) {
                            // 当前还没解出的节点添加到未解出数据包集合中
                            highDegreeData.add(dataList);
                        }
                    }
                    decodingRatio.put(++packageNum, oneDegreeData.size());
                }
            }
        }
        //decodeHighDegreeData();
        // 这里也需要做判断，因为有可能最后一个包解出来才完全解码
        if (oneDegreeData.size() == maxDegree) {
            System.out.println("分区的成功恢复源数据，当前收到编码包数量为:"+packageNum);
            return true;
        }
        return false;
    }

    // 解码那些待解的高度的数据包
    public void decodeHighDegreeData() {
        for (List<Integer> dataList : highDegreeData) {
            int count = 0;
            int length = dataList.size();
            Iterator<Integer> dataIterator = dataList.iterator();
            while (dataIterator.hasNext()) {
                Integer next = dataIterator.next();
                if (oneDegreeData.contains(next)) {
                    dataIterator.remove();
                    count++;
                }
            }
            if (count == length - 1) {
                oneDegreeData.add(dataList.get(0));
            }
        }
    }

    // 普通的解码，即没有分层的正常LT解码
    public boolean normalDecode() {
        Map<Integer, Node> nodes = simulator.getNodes();
        int maxDegree = simulator.getSpaceHelper().getExperiment().getSensorCount();
        int nodesNum = nodes.size();
        int[] nodeArr = Utils.getRandoms(1, nodesNum, nodesNum);
        //for (int nodeId : nodes.keySet()) {
        //Node curNode = nodes.get(nodeId);
        for (int i=0; i<nodeArr.length; i++){
            if (oneDegreeData.size() == maxDegree) {
                System.out.println("未分区的成功恢复源数据，当前收到编码包数量为:"+packageNum);
                return true;
            }
            Node curNode = nodes.get(nodeArr[i]);
            if (curNode.getPackList().size() == 0) {
                decodingRatio.put(++packageNum, oneDegreeData.size());
                continue;
            }
            // 如果是1度包那么直接就解出来了
            if (curNode.getPackList().size() == 1) {
                oneDegreeData.add(curNode.getData());
            }
            else {
                List<Integer> dataList = curNode.getPackList();
                int count = 0;
                // 因为会消解dataList中已有的数据包，导致dataList改变，所以这里先记录一下之前的长度
                int length = dataList.size();
                Iterator<Integer> dataIterator = dataList.iterator();
                // 用已知信息消解数据包
                while (dataIterator.hasNext()) {
                    Integer next = dataIterator.next();
                    if (oneDegreeData.contains(next)) {
                        dataIterator.remove();
                        count++;
                    }
                }
                // 如果count刚好等于n-1，则表示该数据包可以被解出
                if (count == length - 1) {
                    oneDegreeData.add(dataList.get(0));
                    // 如果又解出了新的1度包，那么就先去消解待解数据包集合中的数据
                    decodeHighDegreeData();
                } else if (count < dataList.size() - 1) {
                    // 当前还没解出的节点添加到未解出数据包集合中
                    highDegreeData.add(dataList);
                }
            }
            decodingRatio.put(++packageNum, oneDegreeData.size());
        }
       // decodeHighDegreeData();
        if (oneDegreeData.size() == maxDegree) {
            System.out.println("分区的成功恢复源数据，当前收到编码包数量为:"+packageNum);
            return true;
        }
        System.out.println("未分区的最终收到的数据包数量"+packageNum);
        return false;
    }

    // 普通LT基于层次的解码方案
    // 因为数据包发送到最外层节点的时候就是随机发送的，因此考虑直接遍历所有的外层节点，收完当前节点所处的所有数据包在选择下一个节点
    public boolean normalLayerDecode() {
        int maxDegree = simulator.getSpaceHelper().getExperiment().getSensorCount();
        for (Node curNode : outerNodes) {
            Queue<DecodingPackage> cacheQueue = curNode.getCacheQueue();
            if (cacheQueue.size() == 0) {
                continue;
            }
            while (cacheQueue.size() != 0) {
                DecodingPackage pack = cacheQueue.poll();
                List<Integer> dataList = pack.getDataList();
                int count = 0;
                int length = dataList.size();
                Iterator<Integer> dataIterator = dataList.iterator();
                while (dataIterator.hasNext()) {
                    Integer next = dataIterator.next();
                    if (oneDegreeData.contains(next)) {
                        dataIterator.remove();
                        count++;
                    }
                }
                if (count == length - 1) {
                    oneDegreeData.add(dataList.get(0));
                    decodeHighDegreeData();
                } else if (count < dataList.size() - 1) {
                    highDegreeData.add(dataList);
                }
                decodingRatio.put(++packageNum, oneDegreeData.size());
            }
        }
        if (oneDegreeData.size() == maxDegree) {
            System.out.println("分区的成功恢复源数据，当前收到编码包数量为:"+packageNum);
            return true;
        }
        return false;
    }

    // 随机选取节点进行分破坏
    public void randomDestory() {
        Map<Integer, Node> nodes = simulator.getNodes();
        int totalCount = nodes.size();
        double destoryCount = totalCount * DESTORY_RATIO;
        int[] destoryNodes = Utils.getRandoms(1, 10000, (int) destoryCount);
        for (int i = 0; i < destoryNodes.length; i++) {
            nodes.get(i).setState(StateEnum.DIE);
        }
    }

    // 区域性破坏,以某个点为半径，定长为半径，破坏整个区域内的所有节点
    public void regionDestory(double x, double y, double r) {
        Map<Integer, Node> nodes = simulator.getNodes();
        for (Map.Entry<Integer, Node> node : nodes.entrySet()) {
            Node curNode = node.getValue();
            if (Math.sqrt(Math.pow(x - curNode.getPosX(), 2) +
                    Math.pow(y - curNode.getPosY(), 2)) <= r) {
                curNode.setState(StateEnum.DIE);
            }
        }
    }
}
