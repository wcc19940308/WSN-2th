package ExperimentCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartitionAndLayerSensorNode extends LTSensorNode {
    //int level; // �ڵ�Ĳ��
    //int region; // �ڵ���������
    public PartitionAndLayerSensorNode(double posX, double poxY, StateEnum state) {
        super(posX, poxY, state);
    }

}
