package org.example.components.topology;

import java.util.*;
import java.util.logging.Logger;

abstract class AbstractNetworkTopology implements NetworkTopology {

    private final String networkId;
    private Set<String> nodes;
    private Map<String, Set<String>> nodeAToNodeBMap;
    private Set<List<String>> paths;
    private final Boolean directed;

    private final Logger logger = Logger.getLogger(AbstractNetworkTopology.class.getName());

    AbstractNetworkTopology(String networkId, boolean directed) {
        this.networkId = networkId;
        this.nodes = new HashSet<>();
        this.nodeAToNodeBMap = new HashMap<>();
        this.paths = new HashSet<>();
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

        // if fromNode is already connected to other nodes, we are either duplicating an edge or creating a new branch
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
    }
}
