package ExperimentCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartitionCodingNode extends LTCodingNode {
    // int region; // �ڵ���������
    public PartitionCodingNode(double posX, double posY, StateEnum state) {
        super(posX, posY, state);
    }


}
