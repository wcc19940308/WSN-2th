package ExperimentCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartitionAndLayerSensorNode extends LTSensorNode {
    //int level; // 节点的层次
    //int region; // 节点所属区域
    public PartitionAndLayerSensorNode(double posX, double poxY, StateEnum state) {
        super(posX, poxY, state);
    }

}
