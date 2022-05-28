package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Main {
    public static void main(String[] args) {

        Function<Integer, Integer> processingRule = X -> {
            if (X % 2 == 0) {
                return -X/2;
            }
            return 3*X+1;
        };
        BiFunction<Integer, Integer, Integer> aggregatingRule = Integer::sum;
        BiFunction<Integer, Integer, Boolean> triggerRule = (X,Y) -> X != 1;

        int numOfNodes = 10;
        List<String> nodeIds = new ArrayList<>();
        for (int n = 0; n < numOfNodes; n++) {
            nodeIds.add(String.valueOf(n));
        }
        DataFeedNetwork.Builder<Integer, Integer> networkBuilder = new DataFeedNetwork.Builder<>("0");
        for (int n = 0; n < numOfNodes; n++) {
            networkBuilder.addNode(nodeIds.get(n), processingRule, aggregatingRule, triggerRule);
        }
        for (int n = 0; n < numOfNodes-1; n++) {
            networkBuilder.addConnection(String.valueOf(n), String.valueOf(n+1));
        }
        DataFeedNetwork<Integer,Integer> network = networkBuilder.build();
        DataFeedDataPacket<Integer,Integer> dataFeedDataPacket = new DataFeedDataPacket<>(27, 0);
        DataFeedDataPacket<Integer,Integer> test = network.evaluatePath(nodeIds.get(0), nodeIds.get(numOfNodes-1), dataFeedDataPacket);
        System.out.println("Final value is: " + test);
    }
}