package org.example.examples.integrator;

import org.example.components.data.DataFeedDataPacket;

public class IntegratorDataPacket extends DataFeedDataPacket<IntegratorDataA,IntegratorDataB> {

    public IntegratorDataPacket(IntegratorDataA integratorDataA, IntegratorDataB integratorDataB) {
        super(integratorDataA, integratorDataB);
    }

    private Double getLowerBound() {
        return getValueA().getLowerBound();
    }

    private Double getUpperBound() {
        return getValueA().getUpperBound();
    }

    public Double getIntegralValue() {
        return getValueB().getIntegralValue();
    }

    @Override
    public String toString() {
        return "IntegratorDataPacket(lowerBound: " + getLowerBound() + ", upperBound: " + getUpperBound() + ", integralValue: " + getIntegralValue() + ")";
    }
}
