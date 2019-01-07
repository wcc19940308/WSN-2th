package ExperimentCode;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Getter
@Setter
public class Experiment {
     double experimentRadiu; // ʵ������뾶
     double experimentHeight; // �߶�
     double experimentWidth; // ���
     double damageInterval; // �ڵ��𻵵�����ʱ��
     int sensorCount; // ��֪�ڵ�����
     int totalCount; // �ܽڵ������
     double communicateRadiu; // �ڵ��ͨ�Ű뾶
     Map<Integer,Node> nodes; // ���ڴ�����нڵ���Ϣ�Ĺ�ϣ��

     // ��������
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

     // Բ������
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
