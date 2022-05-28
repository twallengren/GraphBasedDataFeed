package org.example;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface NetworkTopology {

    String getNetworkId();

    Map<String, Set<String>> getNodeAToNodeBMap();

    Boolean isDirected();

    void addEdge(String nodeA, String nodeB);

}
