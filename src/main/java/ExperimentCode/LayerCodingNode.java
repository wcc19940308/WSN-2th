package ExperimentCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LayerCodingNode extends LTCodingNode {
    //int layer; // ½ÚµãËùÊô²ã
    public LayerCodingNode(double posX, double posY, StateEnum state) {
        super(posX, posY, state);
    }



}
