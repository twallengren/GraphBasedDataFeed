package org.example.examples.integrator;

import org.example.components.network.DataFeedNetwork;

import java.util.function.Function;

public class IntegratorNetwork extends DataFeedNetwork<IntegratorDataA,IntegratorDataB> {

    private IntegratorDataA integratorDataA = new IntegratorDataA();
    private IntegratorDataB integratorDataB = new IntegratorDataB();

    IntegratorNetwork(Builder builder) {
        super(builder);
    }

    private void setLowerBound(Double lowerBound) {
        integratorDataA.setLowerBound(lowerBound);
    }

    private void setUpperBound(Double upperBound) {
        integratorDataA.setUpperBound(upperBound);
    }

    private void setFunction(Function<Double,Double> function) {
        integratorDataA.setFunction(function);
    }

    private void setStepSize(Double stepSize) {
        integratorDataA.setStepSize(stepSize);
    }

    private void resetIntegral() {
        integratorDataB.setIntegralValue(0d);
    }

    public Double integrate(Function<Double,Double> function, Double lowerBound, Double upperBound, Double stepSize) {
        setFunction(function);
        setLowerBound(lowerBound);
        setUpperBound(upperBound);
        setStepSize(stepSize);
        resetIntegral();
        IntegratorDataPacket inputData = new IntegratorDataPacket(this.integratorDataA, this.integratorDataB);
        IntegratorDataPacket outputData = (IntegratorDataPacket) evaluatePath(getNetworkId(), "-" + getNetworkId(), inputData);
        return outputData.getIntegralValue();
    }

    public static class Builder extends DataFeedNetwork.Builder<IntegratorDataA,IntegratorDataB> {

        private final IntegratorFunctions functions = new IntegratorFunctions();

        public Builder(String networkId) {
            super(networkId);
        }

        public IntegratorNetwork build() {
            addNode(getNetworkId(), functions);
            addConnection(getNetworkId(), getNetworkId());
            return new IntegratorNetwork(this);
        }
    }
}
