package org.example.examples.collatz;

import org.example.components.network.DataFeedNetwork;

import java.util.function.BiFunction;
import java.util.function.Function;

public class CollatzNetwork extends DataFeedNetwork<CollatzDataA,CollatzDataB> {

    protected CollatzNetwork(Builder builder) {
        super(builder);
    }

    public String startFromValue(int value) {
        CollatzDataA collatzDataA = new CollatzDataA(value);
        CollatzDataB collatzDataB = new CollatzDataB(Integer.toString(value));
        CollatzDataPacket inputData = new CollatzDataPacket(collatzDataA, collatzDataB);
        CollatzDataPacket outputData = (CollatzDataPacket) evaluatePath(getNetworkId(), "-" + getNetworkId(), inputData);
        return outputData.getAggregate().getAggregate();
    }

    public static class Builder extends DataFeedNetwork.Builder<CollatzDataA,CollatzDataB> {

        private final Function<CollatzDataA,CollatzDataA> preProcessingFunction = collatzDataA -> {
            if (collatzDataA.getValue() % 2 == 0) {
                collatzDataA.setValue(collatzDataA.getValue()/2);
            } else {
                collatzDataA.setValue(3*collatzDataA.getValue()+1);
            }
            return collatzDataA;
        };
        private final Function<CollatzDataA,String> dataTransferFunctionA = String::valueOf;
        BiFunction<CollatzDataB,String,CollatzDataB> aggregatingFunction = (collatzDataB, stringValue) -> {
            collatzDataB.setAggregate(collatzDataB.getAggregate() + " " + stringValue);
            return collatzDataB;
        };
        private final Function<CollatzDataB,CollatzDataB> dataTransferFunctionB = Function.identity();
        private final BiFunction<CollatzDataA,CollatzDataB,CollatzDataA> feedbackFunction = (A,B) -> A;
        private final BiFunction<CollatzDataA,CollatzDataB,Boolean> triggerFunction = (A,B) -> A.getValue() != 1;

        public Builder(String networkId) {
            super(networkId);
        }

        public CollatzNetwork build() {
            addNode(getNetworkId(), preProcessingFunction, dataTransferFunctionA, aggregatingFunction, dataTransferFunctionB, feedbackFunction, triggerFunction);
            addConnection(getNetworkId(), getNetworkId());
            return new CollatzNetwork(this);
        }
    }
}
