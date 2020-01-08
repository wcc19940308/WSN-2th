package ExperimentCode;

import com.google.common.collect.Range;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Getter
@Setter
// ģ�⿪ʼ�ı�����Ϊ
public class LTSimulator {
    SpaceHelper spaceHelper;
    Map<Integer, CodingPackage> packageInfo; // ���������������ݰ����,��¼���ݰ���ź����ݰ�������Ϣ
    Map<Integer, MixCodingPackage> mixPackageInfo; // ���������ڶ��α�������ݰ������
    Map<Integer, Node> nodes;
    public double walkLength = 0;
    // ����ת������
    public double REDUNDANCY_WALK_LENGTH = 0;
    // �����ʼ�������Ĳ���
    public LTSimulator(SpaceHelper spaceHelper) {
        this.spaceHelper = spaceHelper;
        this.packageInfo = new HashMap<>();
        this.mixPackageInfo = new HashMap<>();
        this.nodes = spaceHelper.getExperiment().getNodes(); // ʵ�鳡���и����ڵ����Ϣ���

        init(spaceHelper.getType());
    }
    // ���ÿ�ʼ����ǰ������׼��
    public void init(NodeTypeEnum type) {
        HashMap<Integer, Node> nodes = spaceHelper.uniformGenerateNodes();
        // ��ÿ���ڵ㰴�նȷֲ�������Ӧ�Ķ�
        spaceHelper.initDegree(nodes);
        // ��ʼ��ÿ���ڵ���ھ�
        spaceHelper.initAddNeighbors(nodes);
        // ��̬�ֲ�
        spaceHelper.initSteadyState(nodes,type);
        // ����������Ҫ���е�һ�β���,Ӧ�����������������ɵı������ݰ������� * ÿ���������ݰ��Ĳ����趨
        walkLength = spaceHelper.getIntialWalkLength(nodes, type);

        // �ֲ�
        if (spaceHelper.getType() == NodeTypeEnum.LAYER_LT || spaceHelper.getType() == NodeTypeEnum.LAYER_AND_PARTITION_LT || spaceHelper.getType() == NodeTypeEnum.NORMAL_BY_LAYER_LT

                || spaceHelper.getType() == NodeTypeEnum.MOWELFC || spaceHelper.getType() == NodeTypeEnum.MOWOELFC
                || spaceHelper.getType() == NodeTypeEnum.MRFELFC || spaceHelper.getType() == NodeTypeEnum.MRFOELFC) {

            spaceHelper.initLayer(nodes);
        }
        // �������ݰ�
        generatePackage();
        // TODO ���������������ڱ�������л�Ӧ��Ҫʱ������
        // TODO ת�Ʊ���������Ҫ��������ʵ��ĵط�����Ϊ���ɵ�ת�Ʊ���ܲ�������ʵ���������ܸ��ʿ��ܴ���1
        // ��ʼ������ˢ��ת�Ʊ��Լ���������
        initOrRefreshForwardingTable(type);
        initOrRefreshForwardingTableProbInterval();
        // ���ݰ��������߱���洢
        if (type == NodeTypeEnum.EDFC

                || type == NodeTypeEnum.MOWELFC || type == NodeTypeEnum.MOWOELFC
                || type == NodeTypeEnum.MOW_LT ) {

            EDFCSingleCastEncoding2();
        } else {
            singlecastEncoding();
        }
    }

    // �Ӹ�֪�ڵ�������ݰ�
    public void generatePackage() {
        // ���ݰ���Ŵ�1��ʼ
        int packageId = 1;
        int sensorCount = 0;
        // ����������ÿ����֪�ڵ����b�����ݰ�
        for (int nodeId : nodes.keySet()) {
            // ����Ǹ�֪�ڵ�,��ô���������֪�ڵ����b�����ݰ�,���Ҹ�����Ӧ�ķ�����
            if (nodes.get(nodeId) instanceof LTSensorNode) {
                sensorCount++;
                // ע���������ݲ�ͬ�ı�����Բ�����ͬ�����ı������ݰ�
                int copyNum = ((LTSensorNode) nodes.get(nodeId)).getCopyNum();
                int regionIndex = (nodes.get(nodeId)).getRegion();
                for (int i = 0; i < copyNum; i++) {
                    CodingPackage pack = new CodingPackage(nodeId, nodeId, regionIndex
                            , Config.WALK_LENGTH, 0, StateEnum.ALIVE);
                    packageInfo.put(packageId++, pack);
                }
            }
        }
        System.out.println("һ����" + sensorCount + "����֪�ڵ����ڲ������ݰ�");
        System.out.println("һ����" + packageInfo.size() + "���������ݰ�");
    }

    // ��ʼ��(����)���нڵ���Ե�ת�Ʊ�͸�������
    public void initOrRefreshForwardingTable(NodeTypeEnum type) {
        for (int nodeId : nodes.keySet()) {
            Node curNode = nodes.get(nodeId);
            // ���ڴ��Ľڵ�Ÿ���ת�Ʊ�
            if (curNode.state == StateEnum.ALIVE) {
                int curId = curNode.getId();
                // ����Լ���ת�Ʊ�
                Map<Integer, Double> forwardingTable = curNode.getForwardingTable();
                // ע��������һ�����µĲ���,�����ڴ���̽�������еĽڵ������������ת�Ʊ�
                forwardingTable.clear();
                // ��ʼ����ʱ����̽��һ���Լ����ھ�
                curNode.detectNeighbors();
                List<Node> neighbors = curNode.getNeighbors();
                // �ȼ�������еķ�����ת�Ƶĸ���
                double notSelfProb = 0;
                for (Node neighbor : neighbors) {
                    int neighborId = neighbor.getId();
                    // ��������ǵ������ת��
                    if (neighborId != curId) {
                        double prob =  Math.min(1, neighbor.getSteadyState() / curNode.getSteadyState()) / curNode.getM();
//                        if (type == NodeTypeEnum.MOWOELFC || type == NodeTypeEnum.MOWELFC || type == NodeTypeEnum.MOW_LT) {
//                            // ����ת�Ʊ���趨
//                            prob = Math.min(1, neighbor.getSteadyState() / curNode.getSteadyState()) / 5000;
//                        } else if (type == NodeTypeEnum.MRFELFC || type == NodeTypeEnum.MRFOELFC || type == NodeTypeEnum.MRF_LT) {
//                            prob = Math.min(1, neighbor.getSteadyState() / curNode.getSteadyState()) / curNode.getM();
//                        }
//                        double prob = Math.min(1 / curNode.getDegree(), neighbor.getSteadyState() / (neighbor.getDegree() * curNode.getSteadyState()));
                        notSelfProb += prob;
                        forwardingTable.put(neighborId, prob);
                    }
                }
                // TODO �����������Ӧ�������notSelfProb��ֵ����1�����Ʒ���������ת�Ʊ�
                // ����ǵ������ת��
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
            // ���ڴ��Ľڵ�Ÿ���ת�Ʊ��������
            if (curNode.state == StateEnum.ALIVE) {
                // ����Լ���ת�Ʊ��ת�Ʊ��������
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

    // ��ʼ������ˢ�����нڵ�Ļ�ϱ����ת�Ʊ�͸�������
    public void initOrRefreshMixForwardingTable() {
        for (int nodeId : nodes.keySet()) {
            Node curNode = nodes.get(nodeId);
            // ���ڴ��Ľڵ�Ÿ���ת�Ʊ�
            if (curNode.state == StateEnum.ALIVE) {
                int curId = curNode.getId();
                // ����Լ���ת�Ʊ�
                Map<Integer, Double> mixForwardingTable = curNode.getMixForwardingTable();
                // ע��������һ�����µĲ���,�����ڴ���̽�������еĽڵ������������ת�Ʊ�
                mixForwardingTable.clear();
                // ��ʼ����ʱ����̽��һ���Լ����ھ�
                curNode.detectNeighbors();
                List<Node> neighbors = curNode.getNeighbors();
                // �ȼ�������еķ�����ת�Ƶĸ���
                double notSelfProb = 0;
                for (Node neighbor : neighbors) {
                    int neighborId = neighbor.getId();
                    // ��������ǵ������ת��
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
            // ���ڴ��Ľڵ�Ÿ���ת�Ʊ��������
            if (curNode.state == StateEnum.ALIVE) {
                // ����Լ���ת�Ʊ��ת�Ʊ��������
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

    // ����������ߵ�һ�ֱ���
    public void singlecastEncoding() {
        // ����ÿ�����ݰ�����������ߣ�ֱ���ľ�����
        for (int packId : packageInfo.keySet()) {
            CodingPackage curCodingPackage = packageInfo.get(packId);
            int step = curCodingPackage.getStep();
            int walkLength = curCodingPackage.getWalkLength();
            // �����û���߹��趨�Ĳ���
            while (step < walkLength && curCodingPackage.getState() == StateEnum.ALIVE) {
                // �õ����ݰ���ǰ����λ��
                int curId = curCodingPackage.getCurId();
                Node curNode = nodes.get(curId);
                Map<Integer, Range<Double>> forwardingTableProbInterval
                        = curNode.getForwardingTableProbInterval();
                // �������������ѡ����Ҫ�������ݰ��Ľڵ�
                double random = Math.random() * 100;
                for (int neighborId : forwardingTableProbInterval.keySet()) {
                    Range<Double> range = forwardingTableProbInterval.get(neighborId);
                    if (range.contains(random)){
                        // ��ѡ����ھӵ���Ϣ�������ݰ�
                        curCodingPackage.setCurId(neighborId);
                        curCodingPackage.setStep(++step);
                        // ��������Ѿ��þ��ˣ���ôֱ��������ھ��Ͻ����ݰ��洢����
                        if (step == walkLength) {
                            Node node = nodes.get(neighborId);
                            List<Integer> packList = node.getPackList();
                            // �ȿ�����������ڵ�ķ����Ƿ�������ݰ���Ҫ��
                            // ����ǵĻ����ٿ����ͣ��������ڵ��Ƿ��Ѿ��յ���������ݰ�����Ϣ
                            // ����ǵĻ��ٽ���ת�͸��ھӣ�����step+1�����������߼���,��������Ϊ��������ݰ���������
                            if (curCodingPackage.getRegionIndex() == node.getRegion()) {
                                //for (int i = 0; i < packList.size(); i++) {
                                if (packList.contains(curCodingPackage.getSensorId())) {
                                    REDUNDANCY_WALK_LENGTH++;
                                    --step;
                                    break;
                                }
                                // �������ڵ㼴û�дﵽָ���Ķȣ���û���յ���������ݰ�����ô����������ݰ�
                                // ������Ҫ�ٴ��ж�step��ֵ����Ϊǰ����ܼ�����
                                if (step == walkLength) {
                                    // ���û���յ���������ݰ�����Ϣ����ܸ����ݰ������б���
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

    // EDFC�ĵ���������߱������
    public void EDFCSingleCastEncoding() {
        // ����ÿ�����ݰ�����������ߣ�ֱ���ľ�����
        for (int packId : packageInfo.keySet()) {
            CodingPackage curCodingPackage = packageInfo.get(packId);
            int step = curCodingPackage.getStep();
            int walkLength = curCodingPackage.getWalkLength();
            // �����û���߹��趨�Ĳ���
            while (step < walkLength && curCodingPackage.getState() == StateEnum.ALIVE) {
                // ���ݰ���ǰλ��
                int curId = curCodingPackage.getCurId();
                Node curNode = nodes.get(curId);
                Map<Integer, Range<Double>> forwardingTableProbInterval
                        = curNode.getForwardingTableProbInterval();
                // �������������ѡ����Ҫ�������ݰ��Ľڵ�
                double random = Math.random() * 100;
                for (int neighborId : forwardingTableProbInterval.keySet()) {
                    Range<Double> range = forwardingTableProbInterval.get(neighborId);
                    if (range.contains(random)) {
                        // ��ѡ����ھӵ���Ϣ�������ݰ�
                        curCodingPackage.setCurId(neighborId);
                        curCodingPackage.setStep(++step);
                        // ��������Ѿ��þ��ˣ���ôֱ��������ھ��Ͻ����ݰ��洢����
                        if (step == walkLength) {
                            Node node = nodes.get(neighborId);
                            List<Integer> packList = node.getPackList();
                            // ������ȵ�Ҫ����֮ǰû�н��չ����Ž��б��롣���Ǵ��Ƕ�Ҫ���
                            if (!checkDegree(node) && !packList.contains(curCodingPackage.getSensorId())) {
                                node.setData(node.getData() ^ curCodingPackage.getSensorId());
                            }
                            // ��Ϊ��EDFC���������ݰ�ֱ��ͣ��������
                            packList.add(curCodingPackage.getSensorId());
                        }
                        // ע�⣡�������break��Ϊ�˱�֤����ҵ��˺��ʵ��ھӽڵ��ܹ��������ھӽڵ��б����ѯ
                        break;
                    }
                }
            }
        }
    }

    // EDFC�ĵ���������߱������2
    public void EDFCSingleCastEncoding2() {
        // ����ÿ�����ݰ�����������ߣ�ֱ���ľ�����
        for (int packId : packageInfo.keySet()) {
            CodingPackage curCodingPackage = packageInfo.get(packId);
            int step = curCodingPackage.getStep();
            int walkLength = curCodingPackage.getWalkLength();
            // �����û���߹��趨�Ĳ���
            while (step < walkLength && curCodingPackage.getState() == StateEnum.ALIVE) {
                // ���ݰ���ǰλ��
                int curId = curCodingPackage.getCurId();
                Node curNode = nodes.get(curId);
                Map<Integer, Range<Double>> forwardingTableProbInterval
                        = curNode.getForwardingTableProbInterval();
                // �������������ѡ����Ҫ�������ݰ��Ľڵ�
                double random = Math.random() * 100;
                for (int neighborId : forwardingTableProbInterval.keySet()) {
                    Range<Double> range = forwardingTableProbInterval.get(neighborId);
                    if (range.contains(random)) {
                        // ��ѡ����ھӵ���Ϣ�������ݰ�
                        curCodingPackage.setCurId(neighborId);
                        curCodingPackage.setStep(++step);
                        // ��������Ѿ��þ��ˣ���ôֱ��������ھ��Ͻ����ݰ��洢����
                        if (step == walkLength) {
                            Node node = nodes.get(neighborId);
                            if (checkDegree(node)) {
                                curCodingPackage.setState(StateEnum.DIE);
                                break;
                            } else {
                                // ����ýڵ��Ѿ�����������ݰ�����Ϣ�ˣ���ô�������ݰ�
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
                        // Ϊ�˱�֤ѡ����һ���ھӺ�ֱ�Ӽ�ʱ�˳�
                        break;
                    }
                }
            }
        }
    }


    // ����ĵ���������ߵ�һ�ֱ���
    public void redundancySingleCastCoding() {
        for (int packId : packageInfo.keySet()) {
            CodingPackage curCodingPackage = packageInfo.get(packId);
            int step = curCodingPackage.getStep();
            int walkLength = curCodingPackage.getWalkLength();
            while (step < walkLength && curCodingPackage.getState() == StateEnum.ALIVE) {
                // �õ����ݰ���ǰ����λ��
                int curId = curCodingPackage.getCurId();
                Node curNode = nodes.get(curId);
                Map<Integer, Range<Double>> forwardingTableProbInterval
                        = curNode.getForwardingTableProbInterval();
                // �������������ѡ����Ҫ���ͱ������ݰ����ھӽڵ�
                double random = Math.random() * 100;
                for (int neighborId : forwardingTableProbInterval.keySet()) {
                    Range<Double> range = forwardingTableProbInterval.get(neighborId);
                    if (range.contains(random)) {
                        // ��ѡ����ھӵ���Ϣ�������ݰ�
                        curCodingPackage.setCurId(neighborId);
                        curCodingPackage.setStep(++step);
                        Node node = nodes.get(neighborId);
                        if (checkDegree(node)) {
                            curCodingPackage.setState(StateEnum.DIE);
                        } else {
                            // ����ýڵ��Ѿ�����������ݰ�����Ϣ�ˣ���ô�������ݰ�
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

    // ���ڵ��Ƿ��Ѿ������ָ���Ķȣ�����ǣ�����true�����򷵻�false
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

    // ����������ߵڶ��ֻ�ϱ���,ѡ�������߶ȵĽڵ�������ݰ����б��룬�Ա�֤���������е�k/R���м�����
    public void mixCoding (Map<Integer,Node> nodes){
        // ��÷�����
        int partitionNum = Config.PARTITION_NUM;
        // ��÷����еļ��ֵ
        double peak = spaceHelper.getDegreeDistribution().getPeak();
        double peakProb = spaceHelper.getDegreeDistribution().getPeakProb();
        // ���ÿ��������Ҫ���ڱ���ļ��ֵ�ڵ�ĸ���
        int peakNum = (int) (spaceHelper.getExperiment().getTotalCount() * peakProb) / 2;
        // ������ϱ����
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
                        // ������ݰ��ķ�����ڵ�ķ�����ͬ����������ڵ�û���յ���������ݰ�
                        if (neighborNode.getRegion() != curMixCodingPackage.getRegionIndex()
                                && (neighborNode.getPackList().size() == peak)) {
                            // �����ݰ��е���Ϣ�浽�ڵ���
                            List<Integer> packList = neighborNode.getPackList();
                            packList.addAll(curMixCodingPackage.getDataList());
                            // ���ݰ��Ѿ���������
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
        // ���ڼ�¼�����ļ��ڵ��Ƿ��Ѿ�ȡ����
        int[] packCount = new int[Config.PARTITION_NUM + 1];
        // �����нڵ��в��ظ����ѡ��
        int[] randoms = Utils.getRandoms(1, nodes.size(), nodes.size());
        int packageId = 1;
        for (int i = 0; i < randoms.length; i++) {
            // ��������������ݰ����Ѿ�ȫ�������ˣ���ô����
            if (packCount[1] == peakNum && packCount[2] == peakNum
                    && packCount[3] == peakNum && packCount[4] == peakNum) {
                return;
            }
            Node curNode = nodes.get(randoms[i]);
            // �����ǰ�ڵ�Ķ��Ƿ�ֵ��(���������ƫ��peakֵ������)
            //if (curNode.getDegree() == peak) {
            if (Math.abs(peak - curNode.getDegree()) <= Config.OFFSET * peak) {
                int regionIndex = curNode.getRegion();
                if (regionIndex >= 1 && regionIndex <= Config.PARTITION_NUM && packCount[regionIndex] != peakNum) {
                    // �������ǶԽڵ��packList�����ã�����ڵ㱻�ƻ��ˣ�������ܾ���null��
                    MixCodingPackage mixCodingPackage = new MixCodingPackage(curNode.getId(), regionIndex,
                            Config.MIX_WALK_LENGTH, 0, curNode.getPackList(), StateEnum.ALIVE);
                    mixPackageInfo.put(packageId++, mixCodingPackage);
                }
            }
        }
    }

}
