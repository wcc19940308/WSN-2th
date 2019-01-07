package ExperimentCode;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
// 解码数据包，在数据收集阶段需要传送给最外层节点的数据包
public class DecodingPackage {
    int degree; // 当前数据包的度数
    int data; // 当前数据包中蕴含的信息
    List<Integer> dataList; // data中的值是由哪些sensor的数据组成的
    int curId; // 当前数据包走到哪了

    public DecodingPackage(int degree, int data, int curId,List<Integer> list) {
        this.degree = degree;
        this.data = data;
        this.curId = curId;
        this.dataList = list;
    }
}
