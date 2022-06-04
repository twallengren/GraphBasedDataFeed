package org.example.examples.collatz;

public class CollatzDataB {

    private String aggregate = "";

    CollatzDataB(String initialValue) {
        aggregate = initialValue;
    }

    public String getAggregate() {
        return aggregate;
    }

    public void setAggregate(String aggregate) {
        this.aggregate = aggregate;
    }

    @Override
    public String toString() {
        return aggregate;
    }
}
