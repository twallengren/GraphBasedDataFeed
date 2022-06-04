package org.example.examples.collatz;

public class CollatzDataA {

    private int value;

    CollatzDataA(int initialValue) {
        value = initialValue;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
