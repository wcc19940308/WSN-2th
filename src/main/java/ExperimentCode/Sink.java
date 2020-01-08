package ExperimentCode;

import lombok.Getter;
import lombok.Setter;
import java.util.*;


@Getter
@Setter
public class Sink {
    // �Ѿ��������Դ����,��1�����ݰ�����
    Set<Integer> oneDegreeData = new TreeSet<Integer>();
    // ƾ��ǰ��֪��Ϣ�����ܽ�������ݼ��ϣ�������1�ȵ����ݰ�����,���ȴ�С�����˳������
    Set<List<Integer>> highDegreeData;
    List<Node> outerNodes = new ArrayList<Node>(); // ��Ե�ڵ�ڵ���б�
    LTSimulator simulator;
    Map<Integer, Integer> decodingRatio = new HashMap<Integer, Integer>(); // �ָ����ʱȣ����յ���������Ѿ�����Դ����֮��Ĺ�ϵ
    int packageNum = 0; // �Ѿ��յ��ı����������
    int decodingPackageNum = 0; // �������ݰ�������
    int accessNodeCount = 0; // �ɹ�������Ҫ���ʵĽڵ����

    int successDecodingPackageNumber = 10000; // �ɹ���������Ҫ�����ݰ�����
    boolean flag = false;

    public Sink(LTSimulator simulator) {
        this.simulator = simulator;
        // δ����ĸ߶����ݰ������նȴ�С�����˳��洢�ڻ�۽ڵ���
        highDegreeData = new TreeSet<List<Integer>>(new Comparator<List<Integer>>() {
            public int compare(List<Integer> o1, List<Integer> o2) {
                if (o1.size() == o2.size()) {
                    return o1.hashCode() - o2.hashCode();
                } else {
                    return o1.size() - o2.size();
                }
            }
        });
    }

    public boolean collectPackage(NodeTypeEnum type) {
        boolean flag = false;
        // �洢��һ��ʱ�䣬��Щ�ڵ��⵽�ƻ�
//        randomDestory();
        if (type == NodeTypeEnum.LAYER_LT || type == NodeTypeEnum.LAYER_AND_PARTITION_LT

                || type == NodeTypeEnum.MOWOELFC || type == NodeTypeEnum.MRFOELFC) { // OELFC

            boost();
            sendDecodingPackage();
            flag = layerDecode(type);
        } else if (type == NodeTypeEnum.NORMAL_BY_LAYER_LT

                || type == NodeTypeEnum.MOWELFC || type == NodeTypeEnum.MRFELFC) { // ELFC

            boost();
            sendRandomDecodingPackage();
            flag = normalLayerDecode(type);
        } else if (type == NodeTypeEnum.LT || type == NodeTypeEnum.PARTITION_LT
                || type == NodeTypeEnum.EDFC
                || type == NodeTypeEnum.MOW_LT || type == NodeTypeEnum.MRF_LT) {

            flag = normalDecode(type);
        }
        return flag;
    }

    // ģ���������еĽڵ㷢�ͼ����źţ���ʵ������൱�ڸ����Ϊ1�Ľڵ㽨����������ȶ���
    public void boost() {
        Map<Integer, Node> nodes = simulator.getNodes();
        for (int nodeId : nodes.keySet()) {
            Node curNode = nodes.get(nodeId);
            // ����������Ľڵ㣬��ô����Ҫ��һ���洢���ڲ�ڵ㴫�͹��������ڽ���׶ε����ݰ���Ϣ
            if (curNode.getLayer() == 1 && curNode.getState() == StateEnum.ALIVE) {
                curNode.cacheQueue = new LinkedList<DecodingPackage>();
                // ��Sink���б�����Ӳ���Ϊ1�Ľڵ�
                outerNodes.add(curNode);
            }
        }
        //System.out.println("һ����"+outerNodes.size()+"�������ڵ�");
    }


    /***
     * ����׶�
     * �Ƚ�ȫ���Ľڵ��ϴ洢����Ϣ�����ݰ�����ʽ���͵������ڵ���
     * Ȼ����sink�ڵ�������ڵ����ռ���Ӧ�����ݰ�
     */

    // ��ȫ���ڵ��ϴ洢����Ϣ�ԵͶȰ��߶ȵ����ݰ�����ʽ���͵������ڵ���
    public void sendDecodingPackage() {
        //System.out.println("======= begin sendDecodingPackage =======");
        Map<Integer, Node> nodes = simulator.getNodes();
        int maxDegree = simulator.getSpaceHelper().getExperiment().getSensorCount();
        // ���ȴӵ͵��߱������нڵ㣬����˳�����ݰ����ݵ������ڵ���
        for (int i = 1; i <= maxDegree; i++) {
            for (int nodeId : nodes.keySet()) {
                Node curNode = nodes.get(nodeId);
                /*
                 * �����ǰ�ڵ�洢�Ķ�Ϊ��Ҫ�Ķȣ����������������ݰ���ʽ�ز������ڵ㴫��
                 * ����Ķ��ǽڵ���ʵ�Ķȣ����յ������ݰ�������
                 * ���ﻹҪ���ڵ��Ƿ���
                 */
                if (curNode.getState() == StateEnum.ALIVE) {
                    if (curNode.getPackList().size() == i) {
                        // �������Ҫ�Ķȣ��������Ҫ���͸������ڵ�Ľ������ݰ�,�������ݰ���data����ֱ������node�Ķ��оͿ�����
                        DecodingPackage decodingPackage =
                                new DecodingPackage(i, curNode.getData(), curNode.getId(), curNode.getPackList());
                        decodingPackageNum++;
                        // ֻҪ��ǰ���ݰ������Ľڵ��λ�õĲ�����Ϊ1����ôһֱ����㴫��ֱ����������Ϊ1�Ľڵ���Ϊֹ
                        while (nodes.get(decodingPackage.getCurId()).layer != 1) {
                            Node currentNode = nodes.get(decodingPackage.getCurId());
                            // ��⻹����ŵ��ھ�
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
                                // �����Ѿ�������ת�����Լ��Ŀ���,��Ϊ����ת������ű��Լ�С��
                                if (neighborNode.getLayer() == currentNode.getLayer() - 1) {
                                    decodingPackage.setCurId(neighborNode.getId());
                                    currentNode.setLoadBalanceIndex(loadBalanceIndex);
                                    break;
                                }
                                count++;
                            }
                            // ִ�е���������Ϊ���ھ����Ѿ��Ҳ������Լ���κŵ͵��ˣ���ô��ʾ���ݰ�������û�취���⴫����
                            if (count == size) {
                                break;
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
        //System.out.println("======= end sendDecodingPackage =======");

    }

    // ���ڵ��ϵ����ݰ�������������ڵ�
    public void sendRandomDecodingPackage() {
//        System.out.println("======= begin sendRandomDecodingPackage =======");
        Map<Integer, Node> nodes = simulator.getNodes();
        for (int nodeId : nodes.keySet()) {
            Node curNode = nodes.get(nodeId);
            if (curNode.getState() == StateEnum.ALIVE) {
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
                    if (count == size) {
                        break;
                    }
                }
                // �����ǰ�ڵ��Ѿ��������Ľڵ���,��ôֱ�Ӱ����ݰ����¼���
                if (nodes.get(decodingPackage.getCurId()).layer == 1) {
                    nodes.get(decodingPackage.getCurId()).getCacheQueue().offer(decodingPackage);
                }
            }
        }
//        System.out.println("======= end sendRandomDecodingPackage =======");
    }

    // ����׶�,�����Ƿ����ɹ�,�˷������ڶ��ڷֲ�Ľ���
    public boolean layerDecode(NodeTypeEnum type) {
        // ��������ڵ��д洢�ı�����������������ᷢ�͵��ܵ����ݰ�����
        int num = 0;
        for (Node node : outerNodes) {
            num += node.getCacheQueue().size();
        }
        //System.out.println("�ڽ���֮ǰ�������ڵ���һ���洢��"+num+"�����ݰ�");
        //System.out.println("һ����"+decodingPackageNum+"�������õı������ݰ�");

        int maxDegree = simulator.getSpaceHelper().getExperiment().getSensorCount();
        //int index = 1;
        // ��1�Ȱ���߶Ȱ�һ������
        for (int i = 1; i <= maxDegree; i++) {
            for (Node curNode : outerNodes) {
                Queue<DecodingPackage> cacheQueue = curNode.getCacheQueue();
                // ������Ҫ��Ϊ�˷�ֹ�����ڵ����û�д洢���ݶ������쳣
                if (cacheQueue.size() == 0) {
                    continue;
                }
                if (oneDegreeData.size() != maxDegree) accessNodeCount++;
                // ��Ϊ��ʼ���ݵ�ʱ���ǰ��Ͷȵ��߶ȵģ���˶����еͶȵ�����һ�����ڶ���ǰ�˵�
                while ((cacheQueue.size() != 0) && (cacheQueue.peek().getDegree() == i)) {
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
                            // ���countС��n-1�����ʾ�����ݰ��ⲻ��
                            // ������֪���ݶ����ݰ��������⣬�ټ���������ݰ�����
                        } else if (count < dataList.size() - 1) {
                            // ��ǰ��û����Ľڵ���ӵ�δ������ݰ�������
                            highDegreeData.add(dataList);
                        }
                    }
                    decodeHighDegreeData();
                    decodingRatio.put(++packageNum, oneDegreeData.size());
                    if (oneDegreeData.size() == maxDegree && !flag) {
                        successDecodingPackageNumber = packageNum;
                        flag = true;
                    }
                }
                if (oneDegreeData.size() == maxDegree && !flag) {
                    successDecodingPackageNumber = packageNum;
                    flag = true;
                }
            }
            if (oneDegreeData.size() == maxDegree && !flag) {
                successDecodingPackageNumber = packageNum;
                flag = true;
            }
        }
        decodeHighDegreeData();
        if (type == NodeTypeEnum.MOWOELFC) {
            if (oneDegreeData.size() == maxDegree) {
                System.err.println("MOW_OELFC---�ɹ�---�ָ�Դ���ݣ���ǰ�յ����������Ϊ:" + packageNum + "�ָ������ݰ�����" + oneDegreeData.size());
                return true;
            } else {
                System.err.println("MOW_OELFC---ʧ��---�ָ�Դ���ݣ���ǰ�յ����������Ϊ:" + packageNum + "�ָ������ݰ�����" + oneDegreeData.size());
                return false;
            }
        } else if (type == NodeTypeEnum.MRFOELFC) {
            if (oneDegreeData.size() == maxDegree) {
                System.err.println("MRF_OELFC---�ɹ�---�ָ�Դ���ݣ���ǰ�յ����������Ϊ:" + packageNum + "�ָ������ݰ�����" + oneDegreeData.size());
                return true;
            } else {
                System.err.println("MRF_OELFC---ʧ��---�ָ�Դ���ݣ���ǰ�յ����������Ϊ:" + packageNum + "�ָ������ݰ�����" + oneDegreeData.size());
                return false;
            }
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
    public boolean normalDecode(NodeTypeEnum type) {
        int zeroDegreeNodeCount = 0;
        int oneDegreeNodeCount = 0;
        int oneDegreeNode = 0;
        Map<Integer, Node> nodes = simulator.getNodes();
        int maxDegree = simulator.getSpaceHelper().getExperiment().getSensorCount();
        int nodesNum = nodes.size();
        // �����еĽڵ��в���������У�����ģ��������ʽڵ�
        int[] nodeArr = Utils.getRandoms(1, nodesNum, nodesNum);
        for (int i = 0; i < nodeArr.length; i++) {
            if (oneDegreeData.size() != maxDegree) accessNodeCount++; // ֻҪ��û�гɹ�����ͼ������ʽڵ�
            Node curNode = nodes.get(nodeArr[i]);
            // System.out.println(nodeArr[i] + " " + curNode.getData());
            // ���Ľڵ���ܹ����н���
            if (curNode.getState() == StateEnum.ALIVE) {
                // �洢1����Ϣ�Ľڵ�����
                if (type == NodeTypeEnum.EDFC && curNode.getPackList().size() == 1) {
                    oneDegreeNodeCount++;
                }
                // Ԥ���趨Ϊ1�ȵĽڵ�����
                if (type == NodeTypeEnum.EDFC && curNode.getDegree() == 1) {
                    oneDegreeNode++;
                }
                // �մ洢���ڵ����û���ô洢�κ����ݰ�
                if (curNode.getPackList().size() == 0) {
                    decodingRatio.put(++packageNum, oneDegreeData.size());
                    zeroDegreeNodeCount++;
                    continue;
                }
                // �����1�Ȱ���ôֱ�Ӿͽ�����ˣ�������Ǹ߶Ȱ���
                if (curNode.getPackList().size() == 1) {
                    oneDegreeData.add(curNode.getPackList().get(0));
                } else {
                    // ����Ǹ߶Ȱ���ֱ�Ӽ���߶ȴ��⼯�ϼ���
                    List<Integer> dataList = curNode.getPackList();
                    highDegreeData.add(dataList);
                }
                decodeHighDegreeData();
                decodingRatio.put(++packageNum, oneDegreeData.size());
                if (oneDegreeData.size() == maxDegree && !flag) {
                    successDecodingPackageNumber = packageNum;
                    flag = true;
                }
            }
            if (oneDegreeData.size() == maxDegree && !flag) {
                successDecodingPackageNumber = packageNum;
                flag = true;
            }
        }
        decodeHighDegreeData();
        if (type == NodeTypeEnum.MOW_LT) {
            if (oneDegreeData.size() == maxDegree) {
                System.err.println("MOW_LT�ɹ��ָ�Դ���ݣ���ǰ�յ����������Ϊ:" + packageNum + "�ָ������ݰ�����" + oneDegreeData.size());
                return true;
            } else {
                System.err.println("MOW_LTʧ�������յ������ݰ�����" + packageNum + "�ָ������ݰ�����" + oneDegreeData.size());
                return false;
            }
        } else if (type == NodeTypeEnum.MRF_LT) {
            if (oneDegreeData.size() == maxDegree) {
                System.err.println("MRF_LT�ɹ��ָ�Դ���ݣ���ǰ�յ����������Ϊ:" + packageNum + "�ָ������ݰ�����" + oneDegreeData.size());
                return true;
            } else {
                System.err.println("MRF_LTʧ�������յ������ݰ�����" + packageNum + "�ָ������ݰ�����" + oneDegreeData.size());
                return false;
            }
        }
//        if (oneDegreeData.size() == maxDegree) {
//            if (type == NodeTypeEnum.LT) {
//                System.err.println("LT0�����ݰ�����Ϊ" + zeroDegreeNodeCount);
//                System.err.println("LT1�����ݰ�����Ϊ" + oneDegreeNodeCount);
//                System.err.println("��ͨLT---�ɹ�---�ָ�Դ���ݣ���ǰ�յ����������Ϊ:" + packageNum
//                        + " �ָ���ԭʼ���ݰ�����Ϊ:" + oneDegreeData.size() + " ��ǰ���ƻ���Ϊ:" + Config.DESTORY_RATIO);
//            } else if (type == NodeTypeEnum.EDFC) {
//                System.err.println("EDFC0�����ݰ�����Ϊ" + zeroDegreeNodeCount);
//                System.err.println("EDFC1�����ݰ�����Ϊ" + oneDegreeNodeCount);
//                System.err.println("EDFC1�Ƚڵ�����Ϊ" + oneDegreeNode);
//                System.err.println("EDFC---�ɹ�---�ָ�Դ���ݣ���ǰ�յ����������Ϊ:" + packageNum
//                        + " �ָ���ԭʼ���ݰ�����Ϊ:" + oneDegreeData.size() + " ��ǰ���ƻ���Ϊ:" + Config.DESTORY_RATIO);
//            }
//            return true;
//        } else {
//            if (type == NodeTypeEnum.LT) {
//                System.err.println("LT1�����ݰ�����Ϊ" + oneDegreeNodeCount);
//                System.err.println("��ͨLT---ʧ��---�����յ������ݰ�����" + packageNum + "�ָ������ݰ�����" + oneDegreeData.size());
//            } else if (type == NodeTypeEnum.EDFC) {
//                System.err.println("EDFC0�����ݰ�����Ϊ" + zeroDegreeNodeCount);
//                System.err.println("EDFC1�����ݰ�����Ϊ" + oneDegreeNodeCount);
//                System.err.println("EDFC1�Ƚڵ�Ϊ" + oneDegreeNode);
//                System.err.println("EDFC---ʧ��---�����յ������ݰ�����" + packageNum + "�ָ������ݰ�����" + oneDegreeData.size());
//            }
//        }
        return false;
    }

    // ��ͨLT���ڲ�εĽ��뷽��
    // ��Ϊ���ݰ����͵������ڵ��ʱ�����������͵ģ���˿���ֱ�ӱ������е����ڵ㣬���굱ǰ�ڵ��������������ݰ���ѡ����һ���ڵ�
    public boolean normalLayerDecode(NodeTypeEnum type) {
        int maxDegree = simulator.getSpaceHelper().getExperiment().getSensorCount();
        for (Node curNode : outerNodes) {
            if (oneDegreeData.size() != maxDegree) accessNodeCount++;
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

                if (!flag && oneDegreeData.size() == maxDegree) {
                    successDecodingPackageNumber = packageNum;
                    flag = true;
                }
            }
            if (oneDegreeData.size() == maxDegree && !flag) {
                successDecodingPackageNumber = packageNum;
                flag = true;
            }
        }
        if (type == NodeTypeEnum.MOWELFC) {
            if (oneDegreeData.size() == maxDegree) {
                System.err.println("MOW_EDFC�ɹ��ָ�Դ���ݣ���ǰ�յ����������Ϊ:" + packageNum + "�ָ������ݰ�����" + oneDegreeData.size());
                return true;
            } else {
                System.err.println("MOW_EDFCʧ�������յ������ݰ�����" + packageNum + "�ָ������ݰ�����" + oneDegreeData.size());
                return false;
            }
        } else if (type == NodeTypeEnum.MRFELFC) {
            if (oneDegreeData.size() == maxDegree) {
                System.err.println("MRF_EDFC�ɹ��ָ�Դ���ݣ���ǰ�յ����������Ϊ:" + packageNum + "�ָ������ݰ�����" + oneDegreeData.size());
                return true;
            } else {
                System.err.println("MRF_EDFCʧ�������յ������ݰ�����" + packageNum + "�ָ������ݰ�����" + oneDegreeData.size());
                return false;
            }
        }
        return false;
    }

    // ���ѡȡ�ڵ���з��ƻ�
    public void randomDestory() {
        Map<Integer, Node> nodes = simulator.getNodes();
        int totalCount = nodes.size();
        double destoryCount = totalCount * Config.DESTORY_RATIO;
        int[] destoryNodes = Utils.getRandoms(1, 10000, (int) destoryCount);
        for (int i = 0; i < destoryNodes.length; i++) {
            nodes.get(destoryNodes[i]).setState(StateEnum.DIE);
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
