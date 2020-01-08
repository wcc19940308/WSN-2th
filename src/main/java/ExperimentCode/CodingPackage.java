package ExperimentCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// �������ݰ������ڱ���׶δ��ݵ����ݰ�
// �������ݰ�����ı�ţ���¼��LTSimulator�е�packageInfo��Ϣ��
public class CodingPackage {
    int sensorId; // ���ݰ���Я����Դ��Ϣ
    int curId; // ��ʶ��ǰ���ݰ����ڵ�λ�ã�
    int regionIndex; // ���ݰ���������
    int walkLength; // ������߲����趨
    int step; // �Ѿ��ߵĲ���
    StateEnum state; // �Ƿ��������д��

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
