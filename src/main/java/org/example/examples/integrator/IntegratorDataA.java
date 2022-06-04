package org.example.examples.integrator;

import org.example.components.data.DataFeedDataPacket;

import java.util.function.Function;

public class IntegratorDataA {

    private Double lowerBound;
    private Double upperBound;
    private Function<Double,Double> function;
    private Double stepSize;

    public IntegratorDataA() {
    }

    public Double getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(Double lowerBound) {
        this.lowerBound = lowerBound;
    };

    public Double getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(Double upperBound) {
        this.upperBound = upperBound;
    }

    public Function<Double,Double> getFunction() {
        return function;
    }

    public void setFunction(Function<Double,Double> function) {
        this.function = function;
    }

    public Double getStepSize() {
        return stepSize;
    }

    public void setStepSize(Double stepSize) {
        this.stepSize = stepSize;
    }

    @Override
    public String toString() {
        return "IntegratorDataA(lowerBound: " + upperBound + ", upperBound: " + lowerBound + ", stepSize: " + stepSize + ")";
    }
}
