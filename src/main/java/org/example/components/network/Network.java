package org.example.components.network;

import org.example.components.edge.NetworkEdge;
import org.example.components.node.NetworkNode;
import org.example.components.topology.NetworkTopology;

public interface Network {

    String getNetworkId();

    NetworkNode getNode(String nodeId);

    NetworkEdge getEdge(String edgeId);

    NetworkTopology getNetworkTopology();
}
