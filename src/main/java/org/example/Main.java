package org.example;

import org.example.components.data.DataFeedDataPacket;
import org.example.components.network.DataFeedNetwork;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Main {
    public static void main(String[] args) {
        int numOfNodes = 5;
        DataFeedNetwork<Integer,String> collatzNetwork = getCollatzNetwork("0");
        DataFeedDataPacket<Integer,String> collatzDataFeedDataPacket = new DataFeedDataPacket<>(27, "27");
        DataFeedDataPacket<Integer,String> testCollatz = collatzNetwork.evaluatePath("0", "-1", collatzDataFeedDataPacket);
        System.out.println("Final value is: " + testCollatz);

        DataFeedNetwork<DataFeedDataPacket<Double,Double>,Double> integratorNetwork = integrator("0", Math::cos, 0.1);
        DataFeedDataPacket<DataFeedDataPacket<Double,Double>,Double> integratorDataFeedDataPacket =
                new DataFeedDataPacket<>(new DataFeedDataPacket<>(0.0, 10.0), 0.0);
        DataFeedDataPacket<DataFeedDataPacket<Double,Double>,Double> testIntegrator = integratorNetwork.evaluatePath("0", "-1", integratorDataFeedDataPacket);
        System.out.println("Final value is: " + testIntegrator);
    }

    private static DataFeedNetwork<DataFeedDataPacket<Double,Double>,Double> integrator(String networkId, Function<Double,Double> function, Double dA) {
        Function<DataFeedDataPacket<Double,Double>,DataFeedDataPacket<Double,Double>> preProcessingRule = Function.identity();
        Function<DataFeedDataPacket<Double,Double>,Double> dataTransferRuleA = A -> {
            // integrate using trapezoidal rule
            return dA*(function.apply(A.getValue()+dA) + function.apply(A.getValue()))/2;
        };
        BiFunction<Double,Double,Double> aggregatingRule = Double::sum;
        Function<Double,Double> dataTransferRuleB = Function.identity();
        BiFunction<DataFeedDataPacket<Double,Double>,Double,DataFeedDataPacket<Double,Double>> feedbackFunction = (A,Y) -> {
            A.setValue(A.getValue()+dA);
            return A;
        };
        BiFunction<DataFeedDataPacket<Double,Double>,Double,Boolean> triggerRule = (A,B) -> {
            return A.getValue() < A.getAggregate();
        };
        return getCyclicHomogeneousNetwork(networkId, preProcessingRule, dataTransferRuleA, aggregatingRule, dataTransferRuleB, feedbackFunction, triggerRule);
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
        return getCyclicHomogeneousNetwork(networkId, preProcessingRule, dataTransferRuleA, aggregatingRule,dataTransferRuleB, feedbackRule, triggerRule);
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
        networkBuilder.addNode(networkId, preProcessingRule, dataTransferRuleA, aggregatingRule, dataTransferRuleB, feedbackRule, triggerRule);
        networkBuilder.addConnection(networkId, networkId);
        return networkBuilder.build();
    }
}