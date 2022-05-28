package org.example;

import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;

public class DataFeedNetwork<X> extends AbstractNetwork {

    private final Map<String, DataFeedNetworkNode<X>> nodes;
    private final Map<String, DataFeedNetworkEdge> edges;
    private final DataFeedNetworkTopology networkTopology;
    private final Logger logger = Logger.getLogger(DataFeedNetwork.class.getName());

    DataFeedNetwork(Builder<X> builder) {
        super(builder.networkId);
        this.nodes = builder.idToNodeMap;
        this.edges = builder.idToEdgeMap;
        this.networkTopology = builder.networkTopology;
        logger.info("DataFeedNetwork " + builder.networkId + " created.");
    }

    @Override
    public DataFeedNetworkNode<X> getNode(String nodeId) {
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

    public X evaluatePath(String fromNode, String toNode, X toInput) {
        logger.info("Evaluating path from " + fromNode + " to " + toNode);
        if (networkTopology.isProducerNode(fromNode)) {
            logger.info("Node " + fromNode + " input: " + toInput.toString());
            X fromNodeOutput = applyTransferFunction(toInput, fromNode);
            if (fromNodeOutput == null) {
                logger.info("Node " + fromNode + " not triggered at value " + toInput + ". Cannot finish path evaluation.");
                return null;
            }
            logger.info("Node " + fromNode + " output: " + fromNodeOutput);
            for (String nodeId : networkTopology.getNodesListeningTo(fromNode)) {
                if (toNode.equals(nodeId)) {
                    logger.info("Applying final transfer function at node " + toNode);
                    logger.info("Node " + toNode + " input: " + fromNodeOutput);
                    X toNodeOutput = applyTransferFunction(fromNodeOutput, toNode);
                    if (toNodeOutput == null) {
                        logger.info("Node " + toNode + " not triggered at value " + fromNodeOutput + ". Cannot finish path evaluation.");
                        return null;
                    }
                    logger.info("Node " + toNode + " output: " + toNodeOutput);
                    return toNodeOutput;
                } else {
                    return evaluatePath(nodeId, toNode, fromNodeOutput);
                }
            }
        } else {
            logger.info("Node " + fromNode + " is not a producer. No path exists.");
        }
        return null;
    }

    public X applyTransferFunction(X toInput, String nodeId) {
        if (nodes.containsKey(nodeId)) {
            logger.info("Applying node " + nodeId + " transfer function.");
            return nodes.get(nodeId).applyTransferFunction(toInput);
        } else {
            logger.info("Node " + nodeId + " does not exist in network " + getNetworkId() + ". Transfer function not applied.");
            return null;
        }
    }

    public static class Builder<X> {

        private final String networkId;
        private Map<String, DataFeedNetworkNode<X>> idToNodeMap = new HashMap<>();
        private Map<String, DataFeedNetworkEdge> idToEdgeMap = new HashMap<>();
        private final DataFeedNetworkTopology networkTopology;
        private final Logger logger = Logger.getLogger(Builder.class.getName());

        Builder(String networkId) {
            this.networkId = networkId;
            this.networkTopology = new DataFeedNetworkTopology(networkId);
            logger.info("DataFeedNetwork " + networkId + " Builder created");
        }

        public Builder<X> addNode(String nodeId, Function<X,X> transferFunction, Function<X,Boolean> triggerFunction) {
            if (idToNodeMap.containsKey(nodeId)) {
                logger.info("Node " + nodeId + " already exists.");
            } else {
                DataFeedNetworkNode<X> networkNode = new DataFeedNetworkNode.Builder<>(nodeId, transferFunction, triggerFunction).build();
                idToNodeMap.put(nodeId, networkNode);
                logger.info("Node "+ nodeId + " created.");
            }
            return this;
        }

        public Builder<X> addConnection(String fromNode, String toNode) {
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
                return this;
            }
            networkTopology.addEdge(fromNode, toNode);
            DataFeedNetworkNode<X> fromNetworkNode = idToNodeMap.get(fromNode);
            DataFeedNetworkNode<X> toNetworkNode = idToNodeMap.get(toNode);
            DataFeedNetworkEdge networkEdge = new DataFeedNetworkEdge.Builder(edgeId, fromNetworkNode, toNetworkNode).build();
            idToEdgeMap.put(edgeId, networkEdge);
            return this;
        }

        public DataFeedNetwork<X> build() {
            logger.info("Building DataFeedNetwork...");
            return new DataFeedNetwork<>(this);
        }
    }
}
