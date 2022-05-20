package org.example;

import java.util.List;
import java.util.Map;

public interface NetworkTopology {

    String networkId = null;
    Map<NetworkNode, List<NetworkNode>> nodeAToNodeBMap = null;

    List<NetworkNode> getNodeConnections(NetworkNode node);

    void addNodeConnection(NetworkNode nodeA, NetworkNode nodeB);
}
