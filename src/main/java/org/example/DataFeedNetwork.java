package org.example;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.logging.Logger;

public class DataFeedNetwork<X,Y,Z> extends AbstractNetwork {

    private final Map<String, DataFeedNetworkNode<X,Y,Z>> nodes;
    private final Map<String, DataFeedNetworkEdge> edges;
    private final DataFeedNetworkTopology networkTopology;
    private final Logger logger = Logger.getLogger(DataFeedNetwork.class.getName());

    DataFeedNetwork(Builder<X,Y,Z> builder) {
        super(builder.networkId);
        this.nodes = builder.idToNodeMap;
        this.edges = builder.idToEdgeMap;
        this.networkTopology = builder.networkTopology;
        logger.info("DataFeedNetwork " + builder.networkId + " created.");
    }

    @Override
    public DataFeedNetworkNode<X,Y,Z> getNode(String nodeId) {
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

    public DataFeedDataPacket<X,Y> evaluatePath(String fromNode, String toNode, DataFeedDataPacket<X,Y> dataFeedDataPacket) {
        logger.info("Evaluating path from " + fromNode + " to " + toNode);
        if (networkTopology.isProducerNode(fromNode)) {
            logger.info("Node " + fromNode + " input: " + dataFeedDataPacket);
            DataFeedNetworkNode<X,Y,?> networkNode = getNode(fromNode);
            dataFeedDataPacket = networkNode.processPacket(dataFeedDataPacket);
            if (dataFeedDataPacket == null) {
                logger.info("Data feed packet not processed by node " + fromNode);
                return null;
            }
            for (String nodeId : networkTopology.getNodesListeningTo(fromNode)) {
                if (nodeId.equals(toNode)) {
                    networkNode = getNode(nodeId);
                    logger.info("Node " + nodeId + " input: " + dataFeedDataPacket);
                    return networkNode.processPacket(dataFeedDataPacket);
                }
                return evaluatePath(nodeId, toNode, dataFeedDataPacket);
            }
        } else {
            logger.info("Node " + fromNode + " is not a producer. No path exists.");
            return null;
        }
        return dataFeedDataPacket;
    }

    public static class Builder<X,Y,Z> {

        private final String networkId;
        private Map<String, DataFeedNetworkNode<X,Y,Z>> idToNodeMap = new HashMap<>();
        private Map<String, DataFeedNetworkEdge> idToEdgeMap = new HashMap<>();
        private final DataFeedNetworkTopology networkTopology;
        private final Logger logger = Logger.getLogger(Builder.class.getName());

        Builder(String networkId) {
            this.networkId = networkId;
            this.networkTopology = new DataFeedNetworkTopology(networkId);
            logger.info("DataFeedNetwork " + networkId + " Builder created");
        }

        public Builder<X,Y,Z> addNode(String nodeId, Function<X,X> preProcessingFunction, Function<X,Z> dataTransferFunction, BiFunction<Z,Y,Y> aggregatingFunction, BiFunction<X,Y,Boolean> triggerFunction) {
            if (idToNodeMap.containsKey(nodeId)) {
                logger.info("Node " + nodeId + " already exists.");
            } else {
                DataFeedNetworkNode<X,Y,Z> networkNode;
                networkNode = new DataFeedNetworkNode.Builder<>(nodeId, preProcessingFunction, dataTransferFunction, aggregatingFunction, triggerFunction).build();
                idToNodeMap.put(nodeId, networkNode);
                logger.info("Node "+ nodeId + " created.");
            }
            return this;
        }

        public Builder<X,Y,Z> addConnection(String fromNode, String toNode) {
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
            DataFeedNetworkNode<X,Y,Z> fromNetworkNode = idToNodeMap.get(fromNode);
            DataFeedNetworkNode<X,Y,Z> toNetworkNode = idToNodeMap.get(toNode);
            DataFeedNetworkEdge networkEdge = new DataFeedNetworkEdge.Builder(edgeId, fromNetworkNode, toNetworkNode).build();
            idToEdgeMap.put(edgeId, networkEdge);
            return this;
        }

        public DataFeedNetwork<X,Y,Z> build() {
            logger.info("Building DataFeedNetwork...");
            return new DataFeedNetwork<>(this);
        }
    }
}
