package org.example;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Main {
    public static void main(String[] args) {

        Function<Integer, Integer> processingRule = X -> {
            if (X % 2 == 0) {
                return X/2;
            }
            return 3*X+1;
        };
        BiFunction<Integer, Integer, Integer> aggregatingRule = Integer::sum;
        BiFunction<Integer, Integer, Boolean> triggerRule = (X,Y) -> true;
        DataFeedNetwork<Integer, Integer> network = new DataFeedNetwork.Builder<Integer, Integer>("0")
                .addNode("A", processingRule, aggregatingRule, triggerRule)
                .addNode("B", processingRule, aggregatingRule, triggerRule)
                .addNode("C", processingRule, aggregatingRule, triggerRule)
                .addNode("D", processingRule, aggregatingRule, triggerRule)
                .addConnection("A", "B")
                .addConnection("B", "C")
                .addConnection("C", "D")
                .build();
        Integer test = network.evaluatePath("A", "D", 4, 0);
        System.out.println("boobies");
    }
}