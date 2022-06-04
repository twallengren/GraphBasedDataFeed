package org.example.examples.integrator;

import org.example.components.network.DataFeedNetwork;

import java.util.function.BiFunction;
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

        private final Function<IntegratorDataA,IntegratorDataA> preProcessingFunction = Function.identity();
        private final Function<IntegratorDataA,Double> dataTransferFunctionA = integratorDataA -> {
            Double value = integratorDataA.getLowerBound();
            Double stepSize = integratorDataA.getStepSize();
            Function<Double,Double> function = integratorDataA.getFunction();
            return stepSize*(function.apply(value + stepSize) + function.apply(value))/2;
        };
        private final BiFunction<IntegratorDataB,Double,IntegratorDataB> aggregatingFunction = (integratorDataB, dF) -> {
            integratorDataB.setIntegralValue(integratorDataB.getIntegralValue() + dF);
            return integratorDataB;
        };
        private final Function<IntegratorDataB,Double> dataTransferFunctionB = integratorDataB -> null;
        private final BiFunction<IntegratorDataA,Double,IntegratorDataA> feedbackFunction = (integratorDataA, feedbackValue) -> {
            integratorDataA.setLowerBound(integratorDataA.getLowerBound() + integratorDataA.getStepSize());
            return integratorDataA;
        };
        private final BiFunction<IntegratorDataA,IntegratorDataB,Boolean> triggerFunction = (integratorDataA, integratorDataB) -> integratorDataA.getLowerBound()+ integratorDataA.getStepSize() <= integratorDataA.getUpperBound();

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
