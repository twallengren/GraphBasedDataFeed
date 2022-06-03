package org.example.components.topology;

import java.util.*;
import java.util.logging.Logger;

abstract class AbstractNetworkTopology implements NetworkTopology {

    private final String networkId;
    private Set<String> nodes;
    private Map<String, Set<String>> nodeAToNodeBMap;
    private final Boolean directed;

    private final Logger logger = Logger.getLogger(AbstractNetworkTopology.class.getName());

    AbstractNetworkTopology(String networkId, boolean directed) {
        this.networkId = networkId;
        this.nodes = new HashSet<>();
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
    public void addEdge(String fromNode, String toNode) {
        if (nodeAToNodeBMap.containsKey(fromNode)) {
            Set<String> toNodes = nodeAToNodeBMap.get(fromNode);
            if (toNodes.contains(toNode)) {
                logger.info("Edge from " + fromNode + " to " + toNode + " already exists.");
            } else {
                toNodes.add(toNode);
                logger.info("Edge added from " + fromNode + " to " + toNode + ".");
            }
        } else {
            // if node has no outgoing connections, this is a new edge
            nodeAToNodeBMap.put(fromNode, new HashSet<>(Collections.singleton(toNode)));
            logger.info("Edge added from " + fromNode + " to " + toNode + ".");
        }
        nodes.add(fromNode);
        nodes.add(toNode);
    }
}
