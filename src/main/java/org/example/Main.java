package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Main {
    public static void main(String[] args) {
        int numOfNodes = 500;
        DataFeedNetwork<Integer,String> network = getCollatzNetwork("0", 100);
        DataFeedDataPacket<Integer,String> dataFeedDataPacket = new DataFeedDataPacket<>(27, "27");
        DataFeedDataPacket<Integer,String> test = network.evaluatePath("0", "" + (numOfNodes - 1), dataFeedDataPacket);
        System.out.println("Final value is: " + test);
    }

    private static DataFeedNetwork<Integer,String> getCollatzNetwork(String networkId, int numOfNodes) {
        Function<Integer,Integer> preProcessingRule = X -> {
            if (X % 2 == 0) {
                return X/2;
            }
            return 3*X+1;
        };
        Function<Integer,String> dataTransferRule = String::valueOf;
        BiFunction<String,String,String> aggregatingRule = (Z,Y) -> {
            return Y + " " + Z;
        };
        BiFunction<Integer,String,Boolean> triggerRule = (X,Y) -> X != 1;
        return getHomogeneousNetworkFromFunctions(networkId, numOfNodes, preProcessingRule, dataTransferRule, aggregatingRule, triggerRule);
    }

    private static <X,Y,Z> DataFeedNetwork<X,Y> getHomogeneousNetworkFromFunctions(
            String networkId,
            int numOfNodes,
            Function<X,X> preProcessingRule,
            Function<X,Z> dataTransferRule,
            BiFunction<Z,Y,Y> aggregatingRule,
            BiFunction<X,Y,Boolean> triggerRule) {

        List<String> nodeIds = new ArrayList<>();
        for (int n = 0; n < numOfNodes; n++) {
            nodeIds.add(String.valueOf(n));
        }
        DataFeedNetwork.Builder<X,Y> networkBuilder = new DataFeedNetwork.Builder<>(networkId);
        for (int n = 0; n < numOfNodes; n++) {
            networkBuilder.addNode(nodeIds.get(n), preProcessingRule, dataTransferRule, aggregatingRule, triggerRule);
        }
        for (int n = 0; n < numOfNodes-1; n++) {
            networkBuilder.addConnection(String.valueOf(n), String.valueOf(n+1));
        }
        return networkBuilder.build();
    }
}