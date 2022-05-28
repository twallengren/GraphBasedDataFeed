package org.example;

import java.util.logging.Logger;

abstract class AbstractNetworkNode implements NetworkNode {

    private String nodeId;
    private final Logger logger = Logger.getLogger(AbstractNetworkNode.class.getName());

    AbstractNetworkNode(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeId() {
        return nodeId;
    }
}
