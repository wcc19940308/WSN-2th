package ExperimentCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartitionCodingNode extends LTCodingNode {
    // int region; // 节点所属区域
    public PartitionCodingNode(double posX, double posY, StateEnum state) {
        super(posX, posY, state);
    }


}
