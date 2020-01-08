package PartitionTest;

import ExperimentCode.*;

public class RegionTest {
    public static void main(String[] args) {
        Experiment regionExperiment = new Experiment(4, 4, 0,
                8, 16, 2);

        DegreeDistribution regionDegreeDistribution = new LTDegreeDistribution(0.01,0.05,8);

        LTSimulator regionLTSimulator = new LTSimulator(
                new SpaceHelper(regionExperiment, regionDegreeDistribution, NodeTypeEnum.PARTITION_LT));
        Sink regionSink = new Sink(regionLTSimulator);
    }
}
