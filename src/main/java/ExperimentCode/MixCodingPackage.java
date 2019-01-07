package ExperimentCode;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

// 第二轮混合编码使用的数据包,直接使用NRW算法，每个邻居的概率为1/（邻居节点数）,以此来减少自转发的概率
// 一旦找到其他区的最高度数据包就直接进行编码存储
@Getter
@Setter
public class MixCodingPackage {
    int curId; // 数据包当前所处的位置处的节点ID，用于标识当前数据包所在的位置
    int regionIndex = 0; // 数据包所属分区
    int walkLength; // 随机游走步长设置
    int step; // 已经走了的步数
    List<Integer> dataList; // 用于存储当前尖峰值节点中的数据信息
    StateEnum state; // 是否还在网络中存活

    public MixCodingPackage(int curId, int regionIndex, int walkLength, int step,
                            List<Integer> packList, StateEnum state) {
        this.curId = curId;
        this.regionIndex = regionIndex;
        this.walkLength = walkLength;
        this.step = step;
        dataList = packList;
        this.state = state;
    }

    @Override
    public String toString() {
        return "MixCodingPackage{" +
                "curId=" + curId +
                ", regionIndex=" + regionIndex +
                ", walkLength=" + walkLength +
                ", step=" + step +
                ", state=" + state +
                '}';
    }
}
