package ExperimentCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartitionAndLayerCodingNode extends LTCodingNode{
    //int level; // �ڵ�Ĳ��
    //int region; // �ڵ���������
    public PartitionAndLayerCodingNode(double posX, double posY, StateEnum state) {
        super(posX, posY, state);
    }


}
