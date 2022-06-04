package org.example.components.network;

import org.example.components.edge.DataFeedNetworkEdge;
import org.example.components.node.DataFeedNetworkNode;
import org.example.components.topology.DataFeedNetworkTopology;
import org.example.components.data.DataFeedDataPacket;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.logging.Logger;

/**
 * @param <A> - preprocessing data type
 * @param <B> - aggregating data type
 */
public class DataFeedNetwork<A,B> extends AbstractNetwork {

    private final Map<String, DataFeedNetworkNode<A,?,B,?>> nodes;
    private final Map<String, DataFeedNetworkEdge> edges;
    private final DataFeedNetworkTopology networkTopology;
    private final Logger logger = Logger.getLogger(DataFeedNetwork.class.getName());

    protected DataFeedNetwork(Builder<A,B> builder) {
        super(builder.networkId);
        this.nodes = builder.idToNodeMap;
        this.edges = builder.idToEdgeMap;
        this.networkTopology = builder.networkTopology;
        logger.info("DataFeedNetwork " + builder.networkId + " created.");
    }

    @Override
    public DataFeedNetworkNode<A,?,B,?> getNode(String nodeId) {
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

    public DataFeedDataPacket<A,B> evaluatePath(String fromNode, String toNode, DataFeedDataPacket<A,B> dataFeedDataPacket) {
        logger.info("Evaluating path from " + fromNode + " to " + toNode);
        if (networkTopology.isProducerNode(fromNode)) {
            logger.info("Node " + fromNode + " input: " + dataFeedDataPacket);
            DataFeedNetworkNode<A,?,B,?> networkNode = getNode(fromNode);
            if (!networkNode.applyTriggerFunction(dataFeedDataPacket)) {
                logger.info("Node " + fromNode + " not triggered by input data.");
                return dataFeedDataPacket;
            }
            dataFeedDataPacket = networkNode.processPacket(dataFeedDataPacket);
            if (dataFeedDataPacket == null) {
                logger.info("Node " + fromNode + " output is null.");
                return null;
            }
            logger.info("Node " + fromNode + " output: " + dataFeedDataPacket);
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

    public static class Builder<A,B> {

        private final String networkId;
        private Map<String, DataFeedNetworkNode<A,?,B,?>> idToNodeMap = new HashMap<>();
        private Map<String, DataFeedNetworkEdge> idToEdgeMap = new HashMap<>();
        private final DataFeedNetworkTopology networkTopology;
        private final Logger logger = Logger.getLogger(Builder.class.getName());

        public Builder(String networkId) {
            this.networkId = networkId;
            this.networkTopology = new DataFeedNetworkTopology(networkId);
            logger.info("DataFeedNetwork " + networkId + " Builder created");
        }

        public <X,Y> Builder<A,B> addNode(
                String nodeId,
                Function<A,A> preProcessingFunction,
                Function<A,X> dataTransferFunctionA,
                BiFunction<B,X,B> aggregatingFunction,
                Function<B,Y> dataTransferFunctionB,
                BiFunction<A,Y,A> feedbackFunction,
                BiFunction<A,B,Boolean> triggerFunction) {
            if (idToNodeMap.containsKey(nodeId)) {
                logger.info("Node " + nodeId + " already exists.");
            } else {
                DataFeedNetworkNode<A,X,B,Y> networkNode;
                networkNode = new DataFeedNetworkNode.Builder<>(nodeId, preProcessingFunction, dataTransferFunctionA, aggregatingFunction, dataTransferFunctionB, feedbackFunction, triggerFunction).build();
                idToNodeMap.put(nodeId, networkNode);
                logger.info("Node "+ nodeId + " created.");
            }
            return this;
        }

        public Builder<A,B> addConnection(String fromNode, String toNode) {
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
            DataFeedNetworkNode<A,?,B,?> fromNetworkNode = idToNodeMap.get(fromNode);
            DataFeedNetworkNode<A,?,B,?> toNetworkNode = idToNodeMap.get(toNode);
            DataFeedNetworkEdge networkEdge = new DataFeedNetworkEdge.Builder(edgeId, fromNetworkNode, toNetworkNode).build();
            idToEdgeMap.put(edgeId, networkEdge);
            return this;
        }

        public DataFeedNetwork<A,B> build() {
            logger.info("Building DataFeedNetwork...");
            return new DataFeedNetwork<>(this);
        }

        public <X,Y> DataFeedNetwork<A,B> buildCyclicHomogeneousNetwork(
                Function<A,A> preProcessingFunction,
                Function<A,X> dataTransferFunctionA,
                BiFunction<B,X,B> aggregatingFunction,
                Function<B,Y> dataTransferFunctionB,
                BiFunction<A,Y,A> feedbackFunction,
                BiFunction<A,B,Boolean> triggerFunction) {
            addNode(networkId, preProcessingFunction, dataTransferFunctionA, aggregatingFunction, dataTransferFunctionB, feedbackFunction, triggerFunction);
            addConnection(networkId, networkId);
            return build();
        }

        protected String getNetworkId() {
            return networkId;
        }
    }
}
