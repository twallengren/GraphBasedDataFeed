package org.example.examples.collatz;

import org.example.components.network.DataFeedNetwork;

public class CollatzNetwork extends DataFeedNetwork<CollatzDataA,CollatzDataB> {

    protected CollatzNetwork(Builder builder) {
        super(builder);
    }

    public String startFromValue(int value) {
        CollatzDataA collatzDataA = new CollatzDataA(value);
        CollatzDataB collatzDataB = new CollatzDataB(Integer.toString(value));
        CollatzDataPacket inputData = new CollatzDataPacket(collatzDataA, collatzDataB);
        CollatzDataPacket outputData = (CollatzDataPacket) evaluatePath(getNetworkId(), "-" + getNetworkId(), inputData);
        return outputData.getValueB().getAggregate();
    }

    public static class Builder extends DataFeedNetwork.Builder<CollatzDataA,CollatzDataB> {

        private CollatzFunctions functions = new CollatzFunctions();

        public Builder(String networkId) {
            super(networkId);
        }

        public CollatzNetwork build() {
            addNode(getNetworkId(),
                    functions.getPreProcessingFunction(),
                    functions.getDataTransferFunctionA(),
                    functions.getAggregatingFunction(),
                    functions.getDataTransferFunctionB(),
                    functions.getFeedbackFunction(),
                    functions.getTriggerFunction()
            );
            addConnection(getNetworkId(), getNetworkId());
            return new CollatzNetwork(this);
        }
    }
}
