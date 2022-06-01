package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Main {
    public static void main(String[] args) {
        int numOfNodes = 100;
        DataFeedNetwork<Integer,String> collatzNetwork = getCollatzNetwork("0", numOfNodes);
        DataFeedDataPacket<Integer,String> collatzDataFeedDataPacket = new DataFeedDataPacket<>(27, "27");
        DataFeedDataPacket<Integer,String> testCollatz = collatzNetwork.evaluatePath("0", "" + (numOfNodes - 1), collatzDataFeedDataPacket);
        System.out.println("Final value is: " + testCollatz);

        DataFeedNetwork<Double,Double> integratorNetwork = integrator("1", Math::cos, 0.1, numOfNodes);
        DataFeedDataPacket<Double,Double> integratorDataFeedDataPacket = new DataFeedDataPacket<>(0.0, 0.0);
        DataFeedDataPacket<Double,Double> testIntegrator = integratorNetwork.evaluatePath("0", "" + (numOfNodes - 1), integratorDataFeedDataPacket);
        System.out.println("Final value is: " + testIntegrator);
    }

    private static DataFeedNetwork<Double,Double> integrator(String networkId, Function<Double,Double> function, Double dA, int numOfSteps) {
        Function<Double,Double> preProcessingRule = A -> A+dA;
        Function<Double,Double> dataTransferRuleA = A -> dA*(function.apply(A-dA) + function.apply(A))/2;
        BiFunction<Double,Double,Double> aggregatingRule = Double::sum;
        BiFunction<Double,Double,Boolean> triggerRule = (A,B) -> true;
        return getHomogeneousNetworkNoFeedback(networkId, numOfSteps, preProcessingRule, dataTransferRuleA, aggregatingRule, triggerRule);
    }

    private static DataFeedNetwork<Integer,String> getCollatzNetwork(String networkId, int numOfNodes) {
        Function<Integer,Integer> preProcessingRule = A -> {
            if (A % 2 == 0) {
                return A/2;
            }
            return 3*A+1;
        };
        Function<Integer,String> dataTransferRuleA = String::valueOf;
        BiFunction<String,String,String> aggregatingRule = (B,X) -> {
            return B + " " + X;
        };
        BiFunction<Integer,String,Boolean> triggerRule = (A,B) -> A != 1;
        return getHomogeneousNetworkNoFeedback(networkId, numOfNodes, preProcessingRule, dataTransferRuleA, aggregatingRule, triggerRule);
    }

    private static <A,X,B> DataFeedNetwork<A,B> getHomogeneousNetworkNoFeedback(
            String networkId,
            int numOfNodes,
            Function<A,A> preProcessingRule,
            Function<A,X> dataTransferRuleA,
            BiFunction<B,X,B> aggregatingRule,
            BiFunction<A,B,Boolean> triggerRule
    ) {
        Function<B,B> dataTransferRuleB = Function.identity();
        BiFunction<A,B,A> feedbackRule = (A,B) -> A;
        return getHomogeneousNetworkFromFunctions(networkId, numOfNodes, preProcessingRule, dataTransferRuleA, aggregatingRule, dataTransferRuleB, feedbackRule, triggerRule);
    }

    private static <A,X,B,Y> DataFeedNetwork<A,B> getHomogeneousNetworkFromFunctions(
            String networkId,
            int numOfNodes,
            Function<A,A> preProcessingRule,
            Function<A,X> dataTransferRuleA,
            BiFunction<B,X,B> aggregatingRule,
            Function<B,Y> dataTransferRuleB,
            BiFunction<A,Y,A> feedbackRule,
            BiFunction<A,B,Boolean> triggerRule) {

        List<String> nodeIds = new ArrayList<>();
        for (int n = 0; n < numOfNodes; n++) {
            nodeIds.add(String.valueOf(n));
        }
        DataFeedNetwork.Builder<A,B> networkBuilder = new DataFeedNetwork.Builder<>(networkId);
        for (int n = 0; n < numOfNodes; n++) {
            networkBuilder.addNode(nodeIds.get(n), preProcessingRule, dataTransferRuleA, aggregatingRule, dataTransferRuleB, feedbackRule, triggerRule);
        }
        for (int n = 0; n < numOfNodes-1; n++) {
            networkBuilder.addConnection(String.valueOf(n), String.valueOf(n+1));
        }
        return networkBuilder.build();
    }
}