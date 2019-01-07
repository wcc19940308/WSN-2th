package ExperimentCode;

// 枚举类型，普通LT，分层LT，分区LT，分层分区LT，使用分层方式收集但是不按度从小到大的普通LT
public enum NodeTypeEnum {
    LT,LAYER_LT,PARTITION_LT,LAYER_AND_PARTITION_LT,
    NORMAL_BY_LAYER_LT
}
