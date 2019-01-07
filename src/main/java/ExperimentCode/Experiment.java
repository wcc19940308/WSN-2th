package ExperimentCode;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Getter
@Setter
public class Experiment {
     double experimentRadiu; // 实验区域半径
     double experimentHeight; // 高度
     double experimentWidth; // 宽度
     double damageInterval; // 节点损坏的周期时长
     int sensorCount; // 感知节点数量
     int totalCount; // 总节点的数量
     double communicateRadiu; // 节点的通信半径
     Map<Integer,Node> nodes; // 用于存放所有节点信息的哈希表

     // 矩阵区域
     public Experiment(double experimentHeight, double experimentWidth, double damageInterval,
                       int sensorCount, int totalCount, double communicateRadiu) {
          this.experimentHeight = experimentHeight;
          this.experimentWidth = experimentWidth;
          this.damageInterval = damageInterval;
          this.sensorCount = sensorCount;
          this.totalCount = totalCount;
          this.communicateRadiu = communicateRadiu;
          nodes = new HashMap<Integer, Node>();
     }

     // 圆形区域
     public Experiment(double experimentRadiu, double damageInterval,
                       int sensorCount, int totalCount, double communicateRadiu) {
          this.experimentRadiu = experimentRadiu;
          this.damageInterval = damageInterval;
          this.sensorCount = sensorCount;
          this.totalCount = totalCount;
          this.communicateRadiu = communicateRadiu;
          nodes = new HashMap<Integer, Node>();
     }
}
