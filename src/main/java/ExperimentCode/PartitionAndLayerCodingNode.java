package ExperimentCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartitionAndLayerCodingNode extends LTCodingNode{
    //int level; // 节点的层次
    //int region; // 节点所属区域
    public PartitionAndLayerCodingNode(double posX, double posY, StateEnum state) {
        super(posX, posY, state);
    }


}
