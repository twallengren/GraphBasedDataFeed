package org.example.examples.integrator;

import org.example.components.data.DataFeedDataPacket;

import java.util.function.Function;

public class IntegratorDataA extends DataFeedDataPacket<DataFeedDataPacket<Double,Double>,DataFeedDataPacket<Function<Double,Double>,Double>> {

    public IntegratorDataA() {
        super(new DataFeedDataPacket<>(0.0, 0.0), new DataFeedDataPacket<>(Function.identity(), 1d));
    }

    public Double getLowerBound() {
        return getValue().getValue();
    }

    public void setLowerBound(Double lowerBound) {
        getValue().setValue(lowerBound);
    };

    public Double getUpperBound() {
        return getValue().getAggregate();
    }

    public void setUpperBound(Double upperBound) {
        getValue().setAggregate(upperBound);
    }

    public Function<Double,Double> getFunction() {
        return getAggregate().getValue();
    }

    public void setFunction(Function<Double,Double> function) {
        getAggregate().setValue(function);
    }

    public Double getStepSize() {
        return getAggregate().getAggregate();
    }

    public void setStepSize(Double stepSize) {
        getAggregate().setAggregate(stepSize);
    }

    @Override
    public String toString() {
        return "IntegratorDataA(lowerBound: " + getLowerBound() + ", upperBound: " + getUpperBound() + ")";
    }
}
