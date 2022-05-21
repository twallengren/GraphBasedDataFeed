package org.example;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface NetworkTopology {

    String networkId = null;
    Map<NetworkNode, List<NetworkNode>> nodeAToNodeBMap = null;
    Boolean directed = null;
    Set<NetworkEdge> edges = null;

    String getNetworkId();

    Map<NetworkNode, List<NetworkNode>> getNodeAToNodeBMap();

    boolean isDirected();

    Set<NetworkEdge> getEdges();

}
