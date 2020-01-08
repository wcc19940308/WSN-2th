package ExperimentCode;

// 枚举类型，普通LT，分层LT，分区LT，分层分区LT，使用分层方式收集但是不按度从小到大的普通LT,以及大论文需要进行对比的EDFC
public enum NodeTypeEnum {
    LT,
    LAYER_LT,
    PARTITION_LT,
    LAYER_AND_PARTITION_LT,
    NORMAL_BY_LAYER_LT,
    EDFC, // 单播+普通LT收集方式

    // 两种策略,单向随机和二次转发在使用主动访问节点的收集方式下的情况
    MOW_LT,MRF_LT,

    // 四种策略
    MOWELFC,MOWOELFC, // 单向随机游走
    MRFELFC,MRFOELFC, // 二次转发随机游走


}
