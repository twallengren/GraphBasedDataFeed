package org.example;

import org.example.components.network.DataFeedNetwork;
import org.example.examples.integrator.IntegratorNetwork;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Main {

    private static final IntegratorNetwork integratorNetwork = new IntegratorNetwork.Builder("0").build();

    public static void main(String[] args) {
        Double integral = integratorNetwork.integrate(x -> x*x, 0.0, 1.0, 0.1);
        System.out.println("Final value is: " + integral);
    }

    private static DataFeedNetwork<Integer,String> getCollatzNetwork(String networkId) {
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
        return getCyclicHomogeneousNetworkNoFeedback(networkId, preProcessingRule, dataTransferRuleA, aggregatingRule, triggerRule);
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

    private static <A,X,B> DataFeedNetwork<A,B> getCyclicHomogeneousNetworkNoFeedback(
            String networkId,
            Function<A,A> preProcessingRule,
            Function<A,X> dataTransferRuleA,
            BiFunction<B,X,B> aggregatingRule,
            BiFunction<A,B,Boolean> triggerRule) {
        Function<B,B> dataTransferRuleB = Function.identity();
        BiFunction<A,B,A> feedbackRule = (A,B) -> A;
        return getCyclicHomogeneousNetwork(networkId, preProcessingRule, dataTransferRuleA, aggregatingRule, dataTransferRuleB, feedbackRule, triggerRule);
    }

    private static <A,X,B,Y> DataFeedNetwork<A,B> getCyclicHomogeneousNetwork(
            String networkId,
            Function<A,A> preProcessingRule,
            Function<A,X> dataTransferRuleA,
            BiFunction<B,X,B> aggregatingRule,
            Function<B,Y> dataTransferRuleB,
            BiFunction<A,Y,A> feedbackRule,
            BiFunction<A,B,Boolean> triggerRule) {
        DataFeedNetwork.Builder<A,B> networkBuilder = new DataFeedNetwork.Builder<>(networkId);
        return networkBuilder.buildCyclicHomogeneousNetwork(preProcessingRule, dataTransferRuleA, aggregatingRule, dataTransferRuleB, feedbackRule, triggerRule);
    }
}