package ExperimentCode;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartitionSensorNode extends LTSensorNode {
    //int region; // �ڵ���������
    public PartitionSensorNode(double posX, double poxY, StateEnum state) {
        super(posX, poxY, state);
    }

}
