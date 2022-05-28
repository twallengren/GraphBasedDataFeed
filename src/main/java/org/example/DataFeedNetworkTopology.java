package org.example;

import java.util.*;
import java.util.logging.Logger;

public class DataFeedNetworkTopology extends AbstractNetworkTopology {

    private final Logger logger = Logger.getLogger(DataFeedNetworkTopology.class.getName());

    DataFeedNetworkTopology(String networkId) {
        super(networkId, true);
        logger.info("DataFeedNetworkTopology " + networkId + " created.");
    }

    public boolean isProducerNode(String nodeId) {
        return getNodeAToNodeBMap().containsKey(nodeId);
    }

    public Set<String> getNodesListeningTo(String nodeId) {
        if (isProducerNode(nodeId)) {
            return getNodeAToNodeBMap().get(nodeId);
        } else {
            logger.info("Node " + nodeId + " is not a producer.");
            return null;
        }
    }
}
