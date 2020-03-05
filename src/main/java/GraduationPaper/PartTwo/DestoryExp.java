package GraduationPaper.PartTwo;


import ExperimentCode.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;

/**
 * ERSD和RSD的灾难性破坏实验，分别进行主动收集方式下10%,20%,30%的破坏性实验
 * 每个重复20次
 */

public class DestoryExp {
	public static void main(String[] args) throws IOException {
		File file = new File("G:/lab/GraduationPaper/PartTwo/RSD和ERSD30-40破坏实验.txt");
		file.createNewFile();
		BufferedWriter out = new BufferedWriter(new FileWriter(file));
		Config.DESTORY_RATIO = 0.2;
		for (int i = 1; i <= 30; i++) {
			Config.DESTORY_RATIO += 0.01;
			for (int j = 0; j <= 20; j++)
				init(Config.DESTORY_RATIO, out);
		}
		out.flush();
		out.close();
	}

	// 稠密网络受到破坏的影响会相对小一点，因此测试了半径为31和50两种情况下不同破坏率的影响
	public static void init(double destoryRatio, BufferedWriter out) throws IOException {
		Experiment LTExperiment = new Experiment(1000, 1000, 0,
				5000, 10000, 50);
		Experiment ELFCExperiment = new Experiment(1000, 1000, 0,
				5000, 10000, 50);
		double x = 9.99989814e-01, y = 1.00000002e-05;
		DegreeDistribution LTdegreeDistribution = new LTDegreeDistribution(0.01,0.05,5000);
		DegreeDistribution ELFCdegreeDistribution
				= new NewDegreeDistribution(0.01, 0.05, 5000, x, y);

		// 开始进行编码操作
		LTSimulator LTSimulator = new LTSimulator(
				new SpaceHelper(LTExperiment, LTdegreeDistribution, NodeTypeEnum.MRF_LT));
		LTSimulator ELFCSimulator = new LTSimulator(
				new SpaceHelper(ELFCExperiment, ELFCdegreeDistribution, NodeTypeEnum.MRF_LT));

		Sink LTSink = new Sink(LTSimulator);
		Sink ELFCSink = new Sink(ELFCSimulator);

		boolean LT = LTSink.collectPackage(NodeTypeEnum.MRF_LT);
		boolean ELFC = ELFCSink.collectPackage(NodeTypeEnum.MRF_LT);
		System.out.println("LT:" + LT + " ELFC:" + ELFC);


		System.out.println("---开始写文件啦---");

		int codeNum = 5000;

		int LTSize = LTSink.getOneDegreeData().size();
		int ELFCSize = ELFCSink.getOneDegreeData().size();
		System.out.println("LTSize:" + LTSize + " ELFCSize:" + ELFCSize);

		NumberFormat instance = NumberFormat.getInstance();
		instance.setMaximumFractionDigits(2);

		String LTResult = instance.format((float) LTSize / (float) codeNum);
		String ELFCResult = instance.format((float) ELFCSize / (float) codeNum);

		out.write(Config.DESTORY_RATIO + " " + LTResult + " " + ELFCResult + "\r\n");
		out.flush();
	}
}
