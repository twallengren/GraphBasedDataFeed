package org.example.components.data;

public class DataFeedDataPacket<A,B> {

    A valueA;
    B valueB;

    public DataFeedDataPacket(A value, B aggregate) {
        this.valueA = value;
        this.valueB = aggregate;
    }

    public A getValueA() {
        return valueA;
    }

    public void setValueA(A valueA) {
        this.valueA = valueA;
    }

    public B getValueB() {
        return valueB;
    }

    public void setValueB(B valueB) {
        this.valueB = valueB;
    }

    @Override
    public String toString() {
        return "DataFeedDataPacket(valueA: " + valueA + ", valueB: " + valueB + ")";
    }
}
