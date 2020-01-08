package PartitionTest;

import ExperimentCode.*;
import org.junit.Test;

public class DistrubutionTest {
    public static void main(String[] args) {
        LTDegreeDistribution degreeDistribution = new LTDegreeDistribution(0.2, 0.05, 10000);
        System.out.println(degreeDistribution.getPeakProb());
    }

    @Test
    public void NumberOfbTest() {

        Experiment normalExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 40);
        Experiment regionExperiment = new Experiment(1000, 1000, 0,
                5000, 10000, 40);

        DegreeDistribution degreeDistribution = new LTDegreeDistribution(0.01, 0.05, 5000);
        DegreeDistribution regionDegreeDistribution = new LTDegreeDistribution(0.01, 0.05, 1250);


        SpaceHelper spaceHelper = new SpaceHelper(normalExperiment, degreeDistribution, NodeTypeEnum.LT);
        spaceHelper.uniformGenerateNodes();

        SpaceHelper spaceHelper1 = new SpaceHelper(regionExperiment, regionDegreeDistribution, NodeTypeEnum.PARTITION_LT);
        spaceHelper1.uniformGenerateNodes() ;
    }
}
