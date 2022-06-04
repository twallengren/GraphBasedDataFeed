package org.example.examples.integrator;

import org.example.components.data.DataFeedDataPacket;

public class IntegratorDataB extends DataFeedDataPacket<Double,Double> {

    public IntegratorDataB(Double value, Double aggregate) {
        super(value, aggregate);
    }
}
