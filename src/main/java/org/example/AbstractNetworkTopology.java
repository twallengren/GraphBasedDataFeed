package org.example;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public class AbstractNetworkTopology implements NetworkTopology {

    private final String networkId;
    private Map<NetworkNode, List<NetworkNode>> nodeAToNodeBMap = Map.of();
    private final Logger logger = Logger.getLogger(AbstractNetworkTopology.class.getName());

    AbstractNetworkTopology(String networkId) {
        this.networkId = networkId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractNetworkTopology that = (AbstractNetworkTopology) o;
        return nodeAToNodeBMap.equals(that.nodeAToNodeBMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeAToNodeBMap);
    }

    public Map<NetworkNode, List<NetworkNode>> getNodeAToNodeBMap() {
        return nodeAToNodeBMap;
    }

    public void setNodeAToNodeBMap(Map<NetworkNode, List<NetworkNode>> nodeAToNodeBMap) {
        this.nodeAToNodeBMap = nodeAToNodeBMap;
    }

    @Override
    public List<NetworkNode> getNodeConnections(NetworkNode node) {
        return this.nodeAToNodeBMap.get(node);
    }

    @Override
    public void addNodeConnection(NetworkNode nodeA, NetworkNode nodeB) {
        if (this.nodeAToNodeBMap.containsKey(nodeA)) {
            List<NetworkNode> toNodes = this.nodeAToNodeBMap.get(nodeA);
            if (!toNodes.contains(nodeB)) {
                toNodes.add(nodeB);
            } else {
                logger.info("Node " + nodeB.getNodeId() + " already exists in network " + this.networkId);
            }
        }
    }
}
