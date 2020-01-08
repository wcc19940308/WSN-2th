package ExperimentCode;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
// �������ݰ����������ռ��׶���Ҫ���͸������ڵ�����ݰ�
public class DecodingPackage {
    int degree; // ��ǰ���ݰ��Ķ���
    int data; // ��ǰ���ݰ����̺�����Ϣ
    int curId; // ��ǰ���ݰ��ߵ�����
    List<Integer> dataList; // data�е�ֵ������Щsensor��������ɵ�


    public DecodingPackage(int degree, int data, int curId,List<Integer> list) {
        this.degree = degree;
        this.data = data;
        this.curId = curId;
        this.dataList = list;
    }
}
