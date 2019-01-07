package ExperimentCode;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

// �ڶ��ֻ�ϱ���ʹ�õ����ݰ�,ֱ��ʹ��NRW�㷨��ÿ���ھӵĸ���Ϊ1/���ھӽڵ�����,�Դ���������ת���ĸ���
// һ���ҵ�����������߶����ݰ���ֱ�ӽ��б���洢
@Getter
@Setter
public class MixCodingPackage {
    int curId; // ���ݰ���ǰ������λ�ô��Ľڵ�ID�����ڱ�ʶ��ǰ���ݰ����ڵ�λ��
    int regionIndex = 0; // ���ݰ���������
    int walkLength; // ������߲�������
    int step; // �Ѿ����˵Ĳ���
    List<Integer> dataList; // ���ڴ洢��ǰ���ֵ�ڵ��е�������Ϣ
    StateEnum state; // �Ƿ��������д��

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
