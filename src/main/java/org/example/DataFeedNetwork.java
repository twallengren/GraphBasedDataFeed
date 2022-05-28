package org.example;

import java.util.*;
import java.util.logging.Logger;

public class DataFeedNetwork extends AbstractNetwork {

    private final Map<String, DataFeedNetworkNode> nodes;
    private final Map<String, DataFeedNetworkEdge> edges;
    private final DataFeedNetworkTopology networkTopology;
    private final Logger logger = Logger.getLogger(DataFeedNetwork.class.getName());

    DataFeedNetwork(Builder builder) {
        super(builder.networkId);
        this.nodes = builder.idToNodeMap;
        this.edges = builder.idToEdgeMap;
        this.networkTopology = builder.networkTopology;
        logger.info("DataFeedNetwork " + builder.networkId + " created.");
    }

    @Override
    public DataFeedNetworkNode getNode(String nodeId) {
        if (nodes.containsKey(nodeId)) {
            return nodes.get(nodeId);
        } else {
            logger.info("Node " + nodeId + " does not exist in network " + getNetworkId());
        }
        return null;
    }

    @Override
    public DataFeedNetworkEdge getEdge(String edgeId) {
        if (edges.containsKey(edgeId)) {
            return edges.get(edgeId);
        } else {
            logger.info("Edge " + edgeId + " does not exist in network " + getNetworkId());
        }
        return null;
    }

    @Override
    public DataFeedNetworkTopology getNetworkTopology() {
        return networkTopology;
    }

    public static class Builder {

        private final String networkId;
        private Map<String, DataFeedNetworkNode> idToNodeMap = new HashMap<>();
        private Map<String, DataFeedNetworkEdge> idToEdgeMap = new HashMap<>();
        private DataFeedNetworkTopology networkTopology;
        private final Logger logger = Logger.getLogger(Builder.class.getName());

        Builder(String networkId) {
            this.networkId = networkId;
            this.networkTopology = new DataFeedNetworkTopology(networkId);
            logger.info("DataFeedNetwork Builder created");
        }

        public Builder addTriggerRuleNode(String nodeId) {
            addNodeToMap(nodeId, NodeType.TRIGGER_NODE);
            return this;
        }

        public Builder addProcessingRuleNode(String nodeId) {
            addNodeToMap(nodeId, NodeType.PROCESSING_NODE);
            return this;
        }

        private void addNodeToMap(String nodeId, NodeType nodeType) {
            if (!idToEdgeMap.containsKey(nodeId)) {
                DataFeedNetworkNode networkNode = new DataFeedNetworkNode.Builder(nodeId, nodeType).build();
                idToNodeMap.put(nodeId, networkNode);
                logger.info("Node (" + nodeId + ", " + nodeType.name() + ") created.");
            } else {
                logger.info("Node (" + nodeId + ", " + nodeType.name() + ") already exists.");
            }
        }

        public Builder addConnection(String fromNode, String toNode) {
            if (!idToNodeMap.containsKey(fromNode)) {
                logger.info("Node " + fromNode + " is not recognized. No connection created.");
                return this;
            }
            if (!idToNodeMap.containsKey(toNode)) {
                logger.info("Node " + toNode + " is not recognized. No connection created.");
                return this;
            }
            String edgeId = fromNode + "_" + toNode;
            if (idToEdgeMap.containsKey(edgeId)) {
                logger.info("Edge " + " already exists. No connection created.");
            }
            networkTopology.addEdge(fromNode, toNode);
            DataFeedNetworkNode fromNetworkNode = idToNodeMap.get(fromNode);
            DataFeedNetworkNode toNetworkNode = idToNodeMap.get(toNode);
            DataFeedNetworkEdge networkEdge = new DataFeedNetworkEdge.Builder(edgeId, fromNetworkNode, toNetworkNode).build();
            idToEdgeMap.put(edgeId, networkEdge);
            return this;
        }

        public DataFeedNetwork build() {
            logger.info("Building DataFeedNetwork...");
            return new DataFeedNetwork(this);
        }
    }
}
