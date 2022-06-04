package org.example.examples.collatz;

import org.example.components.network.DataFeedNetwork;

import java.util.logging.Logger;

public class CollatzNetwork extends DataFeedNetwork<CollatzDataA,CollatzDataB> {

    private static final Logger logger = Logger.getLogger(CollatzNetwork.class.getName());

    protected CollatzNetwork(Builder builder) {
        super(builder);
        logger.info("Collatz network " + getNetworkId() + " constructed.");
    }

    public String startFromValue(int value) {
        logger.info("Computing Collatz cycle starting at integer: " + value);
        CollatzDataA collatzDataA = new CollatzDataA(value);
        CollatzDataB collatzDataB = new CollatzDataB(Integer.toString(value));
        CollatzDataPacket inputData = new CollatzDataPacket(collatzDataA, collatzDataB);
        CollatzDataPacket outputData = (CollatzDataPacket) evaluatePath(getNetworkId(), "-" + getNetworkId(), inputData);
        logger.info("Collatz cycle has reached terminating condition.");
        return outputData.getValueB().getAggregate();
    }

    public static class Builder extends DataFeedNetwork.Builder<CollatzDataA,CollatzDataB> {

        private final CollatzFunctions functions = new CollatzFunctions();

        public Builder(String networkId) {
            super(networkId);
        }

        public CollatzNetwork build() {
            addNode(getNetworkId(), functions);
            addConnection(getNetworkId(), getNetworkId());
            return new CollatzNetwork(this);
        }
    }
}
