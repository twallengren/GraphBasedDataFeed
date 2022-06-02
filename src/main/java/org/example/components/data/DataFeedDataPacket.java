package org.example.components.data;

public class DataFeedDataPacket<X,Y> {

    X value;
    Y aggregate;

    public DataFeedDataPacket(X value, Y aggregate) {
        this.value = value;
        this.aggregate = aggregate;
    }

    public X getValue() {
        return value;
    }

    public void setValue(X value) {
        this.value = value;
    }

    public Y getAggregate() {
        return aggregate;
    }

    public void setAggregate(Y aggregate) {
        this.aggregate = aggregate;
    }

    @Override
    public String toString() {
        return "DataFeedDataPacket(value: " + value + ", aggregate: " + aggregate + ")";
    }
}
