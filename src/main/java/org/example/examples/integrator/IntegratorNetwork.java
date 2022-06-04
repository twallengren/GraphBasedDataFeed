package org.example.examples.integrator;

import org.example.components.data.DataFeedDataPacket;
import org.example.components.network.DataFeedNetwork;

import java.util.function.BiFunction;
import java.util.function.Function;

public class IntegratorNetwork extends DataFeedNetwork<IntegratorDataA,IntegratorDataB> {

    private IntegratorDataA integratorDataA = new IntegratorDataA();
    private IntegratorDataB integratorDataB = new IntegratorDataB(0.0, 0.0);

    IntegratorNetwork(Builder builder) {
        super(builder);
    }

    public void setLowerBound(Double lowerBound) {
        integratorDataA.setLowerBound(lowerBound);
    }

    public void setUpperBound(Double upperBound) {
        integratorDataA.setUpperBound(upperBound);
    }

    public void setFunction(Function<Double,Double> function) {
        integratorDataA.setFunction(function);
    }

    public void setStepSize(Double stepSize) {
        integratorDataA.setStepSize(stepSize);
    }

    public void setIntegralOffset(Double offset) {
        integratorDataB.setValue(offset);
    }

    private void resetIntegralAggregate() {
        integratorDataB.setAggregate(0.0);
    }

    public Double integrate(Function<Double,Double> function, Double lowerBound, Double upperBound, Double stepSize) {
        setFunction(function);
        setLowerBound(lowerBound);
        setUpperBound(upperBound);
        setStepSize(stepSize);
        setIntegralOffset(0.0);
        resetIntegralAggregate();
        IntegratorDataPacket inputData = new IntegratorDataPacket(this.integratorDataA, this.integratorDataB);
        IntegratorDataPacket outputData = (IntegratorDataPacket) evaluatePath(getNetworkId(), "-" + getNetworkId(), inputData);
        return outputData.getIntegralAggregate();
    }

    public static class Builder extends DataFeedNetwork.Builder<IntegratorDataA,IntegratorDataB> {

        private final Function<IntegratorDataA,IntegratorDataA> preProcessingFunction = Function.identity();
        private final Function<IntegratorDataA,Double> dataTransferFunctionA = integratorDataA -> {
            Double value = integratorDataA.getLowerBound();
            Double stepSize = integratorDataA.getStepSize();
            Function<Double,Double> function = integratorDataA.getFunction();
            return stepSize*(function.apply(value + stepSize) + function.apply(value))/2;
        };
        private final BiFunction<IntegratorDataB,Double,IntegratorDataB> aggregatingFunction = (integratorDataB, dF) -> {
            integratorDataB.setAggregate(integratorDataB.getAggregate() + dF);
            return integratorDataB;
        };
        private final Function<IntegratorDataB,Double> dataTransferFunctionB = integratorDataB -> null;
        private final BiFunction<IntegratorDataA,Double,IntegratorDataA> feedbackFunction = (integratorDataA, feedbackValue) -> {
            integratorDataA.setLowerBound(integratorDataA.getLowerBound() + integratorDataA.getStepSize());
            return integratorDataA;
        };
        private final BiFunction<IntegratorDataA,IntegratorDataB,Boolean> triggerFunction = (integratorDataA, integratorDataB) -> {
            return integratorDataA.getLowerBound() < integratorDataA.getUpperBound();
        };

        public Builder(String networkId) {
            super(networkId);
        }

        public IntegratorNetwork build() {
            addNode(getNetworkId(), preProcessingFunction, dataTransferFunctionA, aggregatingFunction, dataTransferFunctionB, feedbackFunction, triggerFunction);
            addConnection(getNetworkId(), getNetworkId());
            return new IntegratorNetwork(this);
        }
    }
}
