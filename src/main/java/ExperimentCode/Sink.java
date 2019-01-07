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
    // �Ѿ��������Դ����,��1�����ݰ�����
    Set<Integer> oneDegreeData = new TreeSet<Integer>();
    // ƾ��ǰ��֪��Ϣ�����ܽ�������ݼ��ϣ�������1�ȵ����ݰ�����,���ȴ�С�����˳������
    Set<List<Integer>> highDegreeData;
    List<Node> outerNodes = new ArrayList<Node>(); // ���ڵ���б�
    LTSimulator simulator;
    Map<Integer, Integer> decodingRatio = new HashMap<Integer, Integer>(); // �ָ����ʱȣ����յ���������Ѿ�����Դ����֮��Ĺ�ϵ
    int packageNum = 0; // �Ѿ��յ��ı����������
    int decodingPackageNum = 0; // �������ݰ�������

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
        // �洢��һ��ʱ�䣬��Щ�ڵ��⵽�ƻ�
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

    // ģ���������еĽڵ㷢�ͼ����źţ���ʵ������൱�ڸ����Ϊ1�Ľڵ㽨����������ȶ���
    public void boost() {
        Map<Integer, Node> nodes = simulator.getNodes();
        for (int nodeId : nodes.keySet()) {
            Node curNode = nodes.get(nodeId);
            // ����������Ľڵ㣬��ô����Ҫ��һ���洢���ڲ�ڵ㴫�͹��������ڽ���׶ε����ݰ���Ϣ
            if (curNode.getLayer() == 1) {
                curNode.cacheQueue = new LinkedList<DecodingPackage>();
                // ��Sink���б�����Ӳ���Ϊ1�Ľڵ�
                outerNodes.add(curNode);
            }
        }
        System.out.println("һ����"+outerNodes.size()+"�������ڵ�");
    }

    // ����׶�
    // �Ƚ�ȫ���Ľڵ��ϴ洢����Ϣ�����ݰ�����ʽ���͵������ڵ���
    // Ȼ����sink�ڵ�������ڵ����ռ���Ӧ�����ݰ�


    // ��ȫ���ڵ��ϴ洢����Ϣ�ԵͶȰ��߶ȵ����ݰ�����ʽ���͵������ڵ���
    public void sendDecodingPackage() {
        Map<Integer, Node> nodes = simulator.getNodes();
        int maxDegree = simulator.getSpaceHelper().getExperiment().getSensorCount();
        // ���ȴӵ͵��߱������нڵ㣬����˳�����ݰ����ݵ������ڵ���
        for (int i=1; i<=maxDegree; i++) {
            for (int nodeId : nodes.keySet()) {
                Node curNode = nodes.get(nodeId);
                // ��ǰ�ڵ���ھ���ѯ���
                //int index = curNode.getLoadBalanceIndex();
                // �����ǰ�ڵ�洢�Ķ�Ϊ��Ҫ�Ķȣ����������������ݰ���ʽ�ز������ڵ㴫��
                // ����Ķ��ǽڵ���ʵ�Ķȣ����յ������ݰ�������
                if (curNode.getPackList().size() == i) {
                    // �������Ҫ�Ķȣ��������Ҫ���͸������ڵ�Ľ������ݰ�,�������ݰ���data����ֱ������node�Ķ��оͿ�����
                    DecodingPackage decodingPackage =
                            new DecodingPackage(i, curNode.getData(), curNode.getId(),curNode.getPackList());
                    decodingPackageNum++;
                    // ֻҪ��ǰ���ݰ������Ľڵ��λ�õĲ�����Ϊ1����ôһֱ����㴫��ֱ����������Ϊ1�Ľڵ���Ϊֹ
                    while (nodes.get(decodingPackage.getCurId()).layer != 1) {
                        Node currentNode = nodes.get(decodingPackage.getCurId());
                        currentNode.detectNeighbors();
                        List<Node> neighbors = currentNode.getNeighbors();
                        // ��ǰ�ڵ���ھ���ѯ���
                        int loadBalanceIndex = currentNode.getLoadBalanceIndex();
                        // ��ѯ������Χ���ھӽڵ㣬������Ҫ���͵Ľڵ㣬���ؾ���
                        // count��¼���Ѿ���ѯ�˵��ھ������������������е��ھӽڵ㶼�Ҳ��������ʾ������ݰ��޷����͵�sink
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
                    // �����ǰ�ڵ��Ѿ��������Ľڵ���,��ôֱ�Ӱ����ݰ����¼���
                    if (nodes.get(decodingPackage.getCurId()).layer == 1) {
                        nodes.get(decodingPackage.getCurId()).getCacheQueue().offer(decodingPackage);
                    }
                }
            }
        }

    }

    // ���ڵ��ϵ����ݰ�������������ڵ�
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
            // �����ǰ�ڵ��Ѿ��������Ľڵ���,��ôֱ�Ӱ����ݰ����¼���
            if (nodes.get(decodingPackage.getCurId()).layer == 1) {
                nodes.get(decodingPackage.getCurId()).getCacheQueue().offer(decodingPackage);
            }
        }
    }

    // ����׶�,�����Ƿ����ɹ�,�˷������ڶ��ڷֲ�Ľ���
    public boolean layerDecode() {
        // ��������ڵ��д洢�ı�����������������ᷢ�͵��ܵ����ݰ�����
        int num = 0;
        for (Node node : outerNodes) {
            num += node.getCacheQueue().size();
        }
        System.out.println("�ڽ���֮ǰ�������ڵ���һ���洢��"+num+"�����ݰ�");
        System.out.println("һ����"+decodingPackageNum+"�������õı������ݰ�");

        int maxDegree = simulator.getSpaceHelper().getExperiment().getSensorCount();
        //int index = 1;
        // ��1�Ȱ���߶Ȱ�һ������
        for (int i=1; i<=maxDegree; i++) {
            for (Node curNode : outerNodes) {
                Queue<DecodingPackage> cacheQueue = curNode.getCacheQueue();
                // ������Ҫ��Ϊ�˷�ֹ�����ڵ����û�д洢���ݶ������쳣
                if (cacheQueue.size() == 0) {
                    continue;
                }
                // ��Ϊ��ʼ���ݵ�ʱ���ǰ��Ͷȵ��߶ȵģ���˶����еͶȵ�����һ�����ڶ���ǰ�˵�
                while ((cacheQueue.size() != 0) && (cacheQueue.peek().getDegree() == i)) {
                    // ���������ݰ��ļ��ϳ��ȴﵽ�˸�֪�ڵ�����ݰ�����k����ô��ʾ�Ѿ��ָ������е�ԭʼ������
                    if (oneDegreeData.size() == maxDegree) {
                        System.out.println("�����ĳɹ��ָ�Դ���ݣ���ǰ�յ����������Ϊ:"+packageNum);
                        return true;
                    }
                    // �����1�ȵİ�����ôֱ�Ӽ��뵽�ѽ���ļ�����
                    if (i == 1) {
                        oneDegreeData.add(cacheQueue.poll().getData());
                        //decodingRatio.put(++packageNum, oneDegreeData.size());
                    } else {
                        // ȡ�������ڵ��д洢�����ݰ����������ݰ��������б��������n-1�����ݰ�sink���Ѿ����ˣ���ô
                        DecodingPackage pack = cacheQueue.poll();
                        List<Integer> dataList = pack.getDataList();
                        // �������ݰ���Я����ÿ����֪�ڵ����Ϣ�����������n-1�����ѻָ��ļ����б��У��Ǹ������ݰ����ǿ��Իָ���
                        // ���Ұ�ʣ�µ���1��Ҳ���뵽�ָ��ļ����б���
                        int count = 0;
                        // ��Ϊ������dataList�����е����ݰ�������dataList�ı䣬���������ȼ�¼һ��֮ǰ�ĳ���
                        int length = dataList.size();
                        Iterator<Integer> dataIterator = dataList.iterator();
                        // ����֪��Ϣ�������ݰ�
                        while (dataIterator.hasNext()) {
                            Integer next = dataIterator.next();
                            if (oneDegreeData.contains(next)) {
                                dataIterator.remove();
                                count++;
                            }
                        }
                        // ���count�պõ���n-1�����ʾ�����ݰ����Ա����
                        if (count == length - 1) {
                            oneDegreeData.add(dataList.get(0));
                            //decodingRatio.put(++packageNum, oneDegreeData.size());
                            // ����ֽ�����µ�1�Ȱ�����ô����ȥ����������ݰ������е�����
                            decodeHighDegreeData();
                            // ���countС��n-1�����ʾ�����ݰ��ⲻ��
                            // ������֪���ݶ����ݰ��������⣬�ټ���������ݰ�����
                        } else if (count < dataList.size() - 1) {
                            // ��ǰ��û����Ľڵ���ӵ�δ������ݰ�������
                            highDegreeData.add(dataList);
                        }
                    }
                    decodingRatio.put(++packageNum, oneDegreeData.size());
                }
            }
        }
        //decodeHighDegreeData();
        // ����Ҳ��Ҫ���жϣ���Ϊ�п������һ�������������ȫ����
        if (oneDegreeData.size() == maxDegree) {
            System.out.println("�����ĳɹ��ָ�Դ���ݣ���ǰ�յ����������Ϊ:"+packageNum);
            return true;
        }
        return false;
    }

    // ������Щ����ĸ߶ȵ����ݰ�
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

    // ��ͨ�Ľ��룬��û�зֲ������LT����
    public boolean normalDecode() {
        Map<Integer, Node> nodes = simulator.getNodes();
        int maxDegree = simulator.getSpaceHelper().getExperiment().getSensorCount();
        int nodesNum = nodes.size();
        int[] nodeArr = Utils.getRandoms(1, nodesNum, nodesNum);
        //for (int nodeId : nodes.keySet()) {
        //Node curNode = nodes.get(nodeId);
        for (int i=0; i<nodeArr.length; i++){
            if (oneDegreeData.size() == maxDegree) {
                System.out.println("δ�����ĳɹ��ָ�Դ���ݣ���ǰ�յ����������Ϊ:"+packageNum);
                return true;
            }
            Node curNode = nodes.get(nodeArr[i]);
            if (curNode.getPackList().size() == 0) {
                decodingRatio.put(++packageNum, oneDegreeData.size());
                continue;
            }
            // �����1�Ȱ���ôֱ�Ӿͽ������
            if (curNode.getPackList().size() == 1) {
                oneDegreeData.add(curNode.getData());
            }
            else {
                List<Integer> dataList = curNode.getPackList();
                int count = 0;
                // ��Ϊ������dataList�����е����ݰ�������dataList�ı䣬���������ȼ�¼һ��֮ǰ�ĳ���
                int length = dataList.size();
                Iterator<Integer> dataIterator = dataList.iterator();
                // ����֪��Ϣ�������ݰ�
                while (dataIterator.hasNext()) {
                    Integer next = dataIterator.next();
                    if (oneDegreeData.contains(next)) {
                        dataIterator.remove();
                        count++;
                    }
                }
                // ���count�պõ���n-1�����ʾ�����ݰ����Ա����
                if (count == length - 1) {
                    oneDegreeData.add(dataList.get(0));
                    // ����ֽ�����µ�1�Ȱ�����ô����ȥ����������ݰ������е�����
                    decodeHighDegreeData();
                } else if (count < dataList.size() - 1) {
                    // ��ǰ��û����Ľڵ���ӵ�δ������ݰ�������
                    highDegreeData.add(dataList);
                }
            }
            decodingRatio.put(++packageNum, oneDegreeData.size());
        }
       // decodeHighDegreeData();
        if (oneDegreeData.size() == maxDegree) {
            System.out.println("�����ĳɹ��ָ�Դ���ݣ���ǰ�յ����������Ϊ:"+packageNum);
            return true;
        }
        System.out.println("δ�����������յ������ݰ�����"+packageNum);
        return false;
    }

    // ��ͨLT���ڲ�εĽ��뷽��
    // ��Ϊ���ݰ����͵������ڵ��ʱ�����������͵ģ���˿���ֱ�ӱ������е����ڵ㣬���굱ǰ�ڵ��������������ݰ���ѡ����һ���ڵ�
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
            System.out.println("�����ĳɹ��ָ�Դ���ݣ���ǰ�յ����������Ϊ:"+packageNum);
            return true;
        }
        return false;
    }

    // ���ѡȡ�ڵ���з��ƻ�
    public void randomDestory() {
        Map<Integer, Node> nodes = simulator.getNodes();
        int totalCount = nodes.size();
        double destoryCount = totalCount * DESTORY_RATIO;
        int[] destoryNodes = Utils.getRandoms(1, 10000, (int) destoryCount);
        for (int i = 0; i < destoryNodes.length; i++) {
            nodes.get(i).setState(StateEnum.DIE);
        }
    }

    // �������ƻ�,��ĳ����Ϊ�뾶������Ϊ�뾶���ƻ����������ڵ����нڵ�
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
