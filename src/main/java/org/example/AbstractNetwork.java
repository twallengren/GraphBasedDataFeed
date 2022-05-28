package org.example;

import java.util.Map;

public abstract class AbstractNetwork implements Network {

    private String networkId;

    AbstractNetwork(String networkId) {
        this.networkId = networkId;
    }

    @Override
    public String getNetworkId() {
        return networkId;
    }

    @Override
    public NetworkNode getNode(String nodeId) {
        return null;
    }

    @Override
    public NetworkEdge getEdge(String nodeId) {
        return null;
    }

    @Override
    public NetworkTopology getNetworkTopology() {
        return null;
    }
}
