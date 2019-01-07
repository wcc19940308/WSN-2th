package ExperimentCode;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartitionSensorNode extends LTSensorNode {
    //int region; // 节点所属区域
    public PartitionSensorNode(double posX, double poxY, StateEnum state) {
        super(posX, poxY, state);
    }

}
