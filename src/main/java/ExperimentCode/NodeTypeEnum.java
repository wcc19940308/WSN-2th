package ExperimentCode;

// ö�����ͣ���ͨLT���ֲ�LT������LT���ֲ����LT��ʹ�÷ֲ㷽ʽ�ռ����ǲ����ȴ�С�������ͨLT,�Լ���������Ҫ���жԱȵ�EDFC
public enum NodeTypeEnum {
    LT,
    LAYER_LT,
    PARTITION_LT,
    LAYER_AND_PARTITION_LT,
    NORMAL_BY_LAYER_LT,
    EDFC, // ����+��ͨLT�ռ���ʽ

    // ���ֲ���,��������Ͷ���ת����ʹ���������ʽڵ���ռ���ʽ�µ����
    MOW_LT,MRF_LT,

    // ���ֲ���
    MOWELFC,MOWOELFC, // �����������
    MRFELFC,MRFOELFC, // ����ת���������


}
