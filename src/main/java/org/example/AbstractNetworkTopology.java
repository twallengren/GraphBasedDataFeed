package org.example;

import java.util.*;
import java.util.logging.Logger;

abstract class AbstractNetworkTopology implements NetworkTopology {

    private final String networkId;
    private Map<String, Set<String>> nodeAToNodeBMap;
    private final Boolean directed;

    private final Logger logger = Logger.getLogger(AbstractNetworkTopology.class.getName());

    AbstractNetworkTopology(String networkId) {
        this.networkId = networkId;
        this.nodeAToNodeBMap = new HashMap<>();
        this.directed = null;
    }

    AbstractNetworkTopology(String networkId, boolean directed) {
        this.networkId = networkId;
        this.nodeAToNodeBMap = new HashMap<>();
        this.directed = directed;
    }

    @Override
    public String getNetworkId() { return networkId; }

    @Override
    public Map<String, Set<String>> getNodeAToNodeBMap() {
        return nodeAToNodeBMap;
    }

    @Override
    public Boolean isDirected() { return directed; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractNetworkTopology that = (AbstractNetworkTopology) o;
        return networkId.equals(that.networkId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(networkId);
    }

    @Override
    public void addEdge(String fromNode, String toNode) {
        if (nodeAToNodeBMap.containsKey(fromNode)) {
            Set<String> toNodes = nodeAToNodeBMap.get(fromNode);
            if (toNodes.contains(toNode)) {
                logger.info("Edge from " + fromNode + " to " + toNode + " already exists.");
            } else {
                toNodes.add(toNode);
                nodeAToNodeBMap.put(fromNode, toNodes);
                logger.info("Edge added from " + fromNode + " to " + toNode + ".");
            }
        } else {
            nodeAToNodeBMap.put(fromNode, new HashSet<>(Collections.singleton(toNode)));
            logger.info("Edge added from " + fromNode + " to " + toNode + ".");
        }
    }
}
