package ExperimentCode;

// 可能需要的一些配置信息
public class Config {
    public static int PARTITION_NUM = 0; // 分区数量
    public static int WALK_LENGTH = 600; // 数据包的一次步长 原先是600
    public static double DESTORY_RATIO = 0; // 随机破坏的百分比
    public static final double DESTORY_RADIU = 100; // 集中破坏的区域半径
    static final int MIX_WALK_LENGTH = 600; // 二次步长设置
    static final double OFFSET = 0.1; // 节点的尖峰值度的正负偏移量百分比
    public static double REDUNDANCY = 1; // 冗余系数(多发的那些数据包)

    public static int N = 10000;
    public static int K = 5000;
}
