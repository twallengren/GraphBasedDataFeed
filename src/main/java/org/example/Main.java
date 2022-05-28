package org.example;

import java.util.function.Function;

public class Main {
    public static void main(String[] args) {

        Function<Double, Double> processingRule = X -> -X/2;
        Function<Double, Boolean> triggerRule = X -> true;
        DataFeedNetwork<Double> network = new DataFeedNetwork.Builder<Double>("0")
                .addNode("A", processingRule, triggerRule)
                .addNode("B", processingRule, triggerRule)
                .addNode("C", processingRule, triggerRule)
                .addNode("D", processingRule, triggerRule)
                .addConnection("A", "B")
                .addConnection("B", "C")
                .addConnection("C", "D")
                .build();
        Double test = network.evaluatePath("A", "D", 10d);
        System.out.println("boobies");
    }
}