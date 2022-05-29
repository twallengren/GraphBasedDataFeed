package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Main {
    public static void main(String[] args) {

        Function<Integer,Integer> preProcessingRule = X -> {
            if (X % 2 == 0) {
                return -X/2;
            }
            return 3*X+1;
        };
        Function<Integer,String> dataTransferRule = String::valueOf;
        BiFunction<String,String,String> aggregatingRule = (Z,Y) -> {
            return Y + " " + Z;
        };
        BiFunction<Integer,String,Boolean> triggerRule = (X,Y) -> true;

        int numOfNodes = 10;
        List<String> nodeIds = new ArrayList<>();
        for (int n = 0; n < numOfNodes; n++) {
            nodeIds.add(String.valueOf(n));
        }
        DataFeedNetwork.Builder<Integer,String,String> networkBuilder = new DataFeedNetwork.Builder<>("0");
        for (int n = 0; n < numOfNodes; n++) {
            networkBuilder.addNode(nodeIds.get(n), preProcessingRule, dataTransferRule, aggregatingRule, triggerRule);
        }
        for (int n = 0; n < numOfNodes-1; n++) {
            networkBuilder.addConnection(String.valueOf(n), String.valueOf(n+1));
        }
        DataFeedNetwork<Integer,String,String> network = networkBuilder.build();
        DataFeedDataPacket<Integer,String> dataFeedDataPacket = new DataFeedDataPacket<>(27, "27");
        DataFeedDataPacket<Integer,String> test = network.evaluatePath(nodeIds.get(0), nodeIds.get(numOfNodes-1), dataFeedDataPacket);
        System.out.println("Final value is: " + test);
    }
}