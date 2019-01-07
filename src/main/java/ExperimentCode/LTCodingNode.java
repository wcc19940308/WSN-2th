package ExperimentCode;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class LTCodingNode extends Node{

    public LTCodingNode(double posX, double posY, StateEnum state) {
        this.posX = posX;
        this.posY = posY;
        this.state = StateEnum.ALIVE;
    }
}
