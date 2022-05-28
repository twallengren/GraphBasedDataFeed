package org.example;

import java.util.Map;

public interface Network {

    String getNetworkId();

    NetworkNode getNode(String nodeId);

    NetworkEdge getEdge(String edgeId);

    NetworkTopology getNetworkTopology();
}
