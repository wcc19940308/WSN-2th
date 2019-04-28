package DegreeDistributionOptimization;

import ExperimentCode.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;

// LT和5个新度分布函数在0.1-0.5灾难破坏场景下的数据恢复情况
public class NewDistributionDestroyExp {

    public static void main(String[] args) throws IOException {
        File file = new File("G:/lab/LabData/NewDistribution/50次LT+新的5个度分布函数半径为30破坏率从0到0.2的解码情况.txt");
        file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        Config.DESTORY_RATIO = 0.0;
        for (int i = 1; i <= 20; i++) {
            Config.DESTORY_RATIO += 0.01;
            for (int j = 0; j < 50; j++)
                init(Config.DESTORY_RATIO, out);
        }
        out.flush();
        out.close();
    }

    // 稠密网络受到破坏的影响会相对小一点，因此测试了半径为31和50两种情况下不同破坏率的影响
    public static void init(double destoryRatio, BufferedWriter out) throws IOException {
        Experiment Experiment1 = new Experiment(1000, 1000, 0,
                5000, 10000, 30);
        Experiment Experiment2 = new Experiment(1000, 1000, 0,
                5000, 10000, 30);
        Experiment Experiment3 = new Experiment(1000, 1000, 0,
                5000, 10000, 30);
        Experiment Experiment4 = new Experiment(1000, 1000, 0,
                5000, 10000, 30);
        Experiment Experiment5 = new Experiment(1000, 1000, 0,
                5000, 10000, 30);
        Experiment LTExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 30);

        double a1 = 0.38920795976855915, b1 = 0.4108600993677495;
        double a2 =0.784499337781702, b2 = 0.2603566604258498;
        double a3 = 1.7128947521680336, b3 = 0.011768445451988518;
        double a4 = 0.8013792412144722, b4 = 0.0657077185758227;
        double a5 = 0.3503100729837485, b5 = 0.04592954906681446;

        DegreeDistribution degreeDistribution1 = new NewDegreeDistribution(0.01, 0.05, 5000, a1, b1);
        DegreeDistribution degreeDistribution2 = new NewDegreeDistribution(0.01, 0.05, 5000, a2, b2);
        DegreeDistribution degreeDistribution3 = new NewDegreeDistribution(0.01, 0.05, 5000, a3, b3);
        DegreeDistribution degreeDistribution4 = new NewDegreeDistribution(0.01, 0.05, 5000, a4, b4);
        DegreeDistribution degreeDistribution5 = new NewDegreeDistribution(0.01, 0.05, 5000, a5, b5);
        DegreeDistribution LTdegreeDistribution = new LTDegreeDistribution(0.01,0.05,5000);

        LTSimulator LTSimulator1 = new LTSimulator(new SpaceHelper(Experiment1, degreeDistribution1, NodeTypeEnum.LT));
        LTSimulator LTSimulator2 = new LTSimulator(new SpaceHelper(Experiment2, degreeDistribution2, NodeTypeEnum.LT));
        LTSimulator LTSimulator3 = new LTSimulator(new SpaceHelper(Experiment3, degreeDistribution3, NodeTypeEnum.LT));
        LTSimulator LTSimulator4 = new LTSimulator(new SpaceHelper(Experiment4, degreeDistribution4, NodeTypeEnum.LT));
        LTSimulator LTSimulator5 = new LTSimulator(new SpaceHelper(Experiment5, degreeDistribution5, NodeTypeEnum.LT));
        LTSimulator LTSimulator = new LTSimulator(new SpaceHelper(LTExperiment, LTdegreeDistribution, NodeTypeEnum.LT));

        Sink Sink1 = new Sink(LTSimulator1);
        Sink Sink2 = new Sink(LTSimulator2);
        Sink Sink3 = new Sink(LTSimulator3);
        Sink Sink4 = new Sink(LTSimulator4);
        Sink Sink5 = new Sink(LTSimulator5);
        Sink LTSink = new Sink(LTSimulator);

        boolean LT1 = Sink1.collectPackage(NodeTypeEnum.LT);
        boolean LT2 = Sink2.collectPackage(NodeTypeEnum.LT);
        boolean LT3 = Sink3.collectPackage(NodeTypeEnum.LT);
        boolean LT4 = Sink4.collectPackage(NodeTypeEnum.LT);
        boolean LT5 = Sink5.collectPackage(NodeTypeEnum.LT);
        boolean LT = LTSink.collectPackage(NodeTypeEnum.LT);

        System.out.println(LT1 + " " + LT2 + " " + LT3 + " " + LT4 + " " + LT5 + " " + LT);


        System.out.println("---开始写文件啦---");

        int codeNum = 5000;

        int LTSize1 = Sink1.getOneDegreeData().size();
        int LTSize2 = Sink2.getOneDegreeData().size();
        int LTSize3 = Sink3.getOneDegreeData().size();
        int LTSize4 = Sink4.getOneDegreeData().size();
        int LTSize5 = Sink5.getOneDegreeData().size();
        int LTSize = LTSink.getOneDegreeData().size();


        NumberFormat instance = NumberFormat.getInstance();
        instance.setMaximumFractionDigits(2);

        String LTResult1 = instance.format((float) LTSize1 / (float) codeNum);
        String LTResult2 = instance.format((float) LTSize2 / (float) codeNum);
        String LTResult3 = instance.format((float) LTSize3 / (float) codeNum);
        String LTResult4 = instance.format((float) LTSize4 / (float) codeNum);
        String LTResult5 = instance.format((float) LTSize5 / (float) codeNum);
        String LTResult = instance.format((float) LTSize / (float) codeNum);

        out.write(Config.DESTORY_RATIO + " " + LTResult1 + " " + LTResult2 + " " + LTResult3 + " " + LTResult4 + " " + LTResult5 + " "
                + LTResult + "\r\n");
        out.flush();
    }
}
