package org.example.components.node;

abstract class AbstractNetworkNode implements NetworkNode {

    private String nodeId;

    AbstractNetworkNode(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeId() {
        return nodeId;
    }
}
