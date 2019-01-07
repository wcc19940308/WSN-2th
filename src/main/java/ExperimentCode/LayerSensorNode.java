package ExperimentCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LayerSensorNode extends LTSensorNode {
    //int layer; // ½ÚµãËùÊô²ã
    public LayerSensorNode(double posX, double poxY, StateEnum state) {
        super(posX, poxY, state);
    }

}
