package org.example;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Main {
    public static void main(String[] args) {

        Function<Double, Double> processingRule = X -> -X/2;
        BiFunction<Double, Double, Double> aggregatingRule = Double::sum;
        Function<Double, Boolean> triggerRule = X -> true;
        DataFeedNetwork<Double, Double> network = new DataFeedNetwork.Builder<Double, Double>("0")
                .addNode("A", processingRule, aggregatingRule, triggerRule)
                .addNode("B", processingRule, aggregatingRule, triggerRule)
                .addNode("C", processingRule, aggregatingRule, triggerRule)
                .addNode("D", processingRule, aggregatingRule, triggerRule)
                .addConnection("A", "B")
                .addConnection("B", "C")
                .addConnection("C", "D")
                .build();
        Double test = network.evaluatePath("A", "G", 10d, 0d);
        System.out.println("boobies");
    }
}