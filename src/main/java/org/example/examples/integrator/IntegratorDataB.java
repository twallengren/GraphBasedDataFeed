package org.example.examples.integrator;

import org.example.components.data.DataFeedDataPacket;

public class IntegratorDataB {

    private Double integralValue;

    public IntegratorDataB() {
        integralValue = 0.0;
    }

    public void setIntegralValue(Double integralValue) {
        this.integralValue = integralValue;
    }

    public Double getIntegralValue() {
        return integralValue;
    }
}
