package ExperimentCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 编码数据包，用于编码阶段传递的数据包
// 对于数据包自身的编号，记录在LTSimulator中的packageInfo信息中
public class CodingPackage {
    int sensorId; // 数据包中携带的源信息
    int curId; // 标识当前数据包所在的位置！
    int regionIndex; // 数据包所属的区
    int walkLength; // 随机游走步长设定
    int step; // 已经走的步数
    StateEnum state; // 是否还在网络中存活

    public CodingPackage(int sensorId, int curId, int regionIndex, int walkLength, int step, StateEnum state) {
        this.sensorId = sensorId;
        this.curId = curId;
        this.regionIndex = regionIndex;
        this.walkLength = walkLength;
        this.step = step;
        this.state = state;
    }

    @Override
    public String toString() {
        return "CodingPackage{" +
                "sensorId=" + sensorId +
                ", curId=" + curId +
                ", walkLength=" + walkLength +
                ", step=" + step +
                ", state=" + state +
                '}';
    }
}
