package GraduationPaper.PartTwo;

import ExperimentCode.*;

import java.util.Map;

// RSD和ERSD成功编码所需要的发送的编码符号数量
public class EncodingNumberCompare {
	public static void main(String[] args) {
		double x = 9.99989814e-01, y = 1.00000002e-05;
		DegreeDistribution LTdegreeDistribution
				= new LTDegreeDistribution(0.01,0.05,5000);
		DegreeDistribution ERSDdegreeDistribution
				= new NewDegreeDistribution(0.01, 0.05, 5000, x, y);

		DegreeDistribution d
				= new LTDegreeDistribution(0.01,0.05,5000);

		double RSDNumber = getEncodingNumber(LTdegreeDistribution);
		double ERSDNumber = getEncodingNumber(ERSDdegreeDistribution);
		double dNumber = getEncodingNumber(d);

		System.out.println(RSDNumber + " " + ERSDNumber + " " + d);
	}

	public static double getEncodingNumber(DegreeDistribution degreeDistribution) {
		Map<Double, Double> robustSolitonDistribution =
				degreeDistribution.getRobustSolitonDistribution();
		int totalCount = Config.N;
		int sensorCount = Config.K;

		double sum = 0;
		for (double index : robustSolitonDistribution.keySet()) {
			Double prob = robustSolitonDistribution.get(index);
			sum += index * prob;
		}
		sum *= totalCount;
		return sum;
	}
}
