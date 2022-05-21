package org.example;

import java.util.*;

public class DataFeedNetworkTopology extends AbstractNetworkTopology {

    private final Map<NetworkNode, Set<NetworkEdge>> incomingEdgesMap;
    private final Map<NetworkNode, Set<NetworkEdge>> outgoingEdgesMap;

    DataFeedNetworkTopology(Builder builder) {
        super(builder.networkId, builder.nodeAToNodeBMap, builder.directed, builder.edges);
        incomingEdgesMap = builder.incomingEdgesMap;
        outgoingEdgesMap = builder.outgoingEdgesMap;
    }

    public static class Builder {
        private final String networkId;
        private final Map<NetworkNode, List<NetworkNode>> nodeAToNodeBMap;
        private final boolean directed;
        private final Set<NetworkEdge> edges;
        private final Map<NetworkNode, Set<NetworkEdge>> incomingEdgesMap;
        private final Map<NetworkNode, Set<NetworkEdge>> outgoingEdgesMap;

        public Builder(String networkId, Map<NetworkNode, List<NetworkNode>> nodeAToNodeBMap) {

            Set<NetworkEdge> edges = new HashSet<>();
            Map<NetworkNode, Set<NetworkEdge>> incomingEdgesMap = new HashMap<>();
            Map<NetworkNode, Set<NetworkEdge>> outgoingEdgesMap = new HashMap<>();
            for (NetworkNode nodeA : nodeAToNodeBMap.keySet()) {
                Set<NetworkEdge> incomingEdges = new HashSet<>();
                Set<NetworkEdge> outgoingEdges = new HashSet<>();
                for (NetworkNode nodeB : nodeAToNodeBMap.get(nodeA)) {
                    String incomingEdgeId = nodeB.getNodeId() + "_" + nodeA.getNodeId();
                    String outgoingEdgeId = nodeA.getNodeId() + "_" + nodeB.getNodeId();
                    NetworkEdge incomingEdge = new DataFeedNetworkEdge.Builder(incomingEdgeId, nodeB, nodeA).build();
                    NetworkEdge outgoingEdge = new DataFeedNetworkEdge.Builder(outgoingEdgeId, nodeA, nodeB).build();
                    edges.add(incomingEdge);

                    if (incomingEdgesMap.containsKey(nodeA)) {
                        incomingEdges = incomingEdgesMap.get(nodeA);
                    }
                    incomingEdges.add(incomingEdge);
                    incomingEdgesMap.put(nodeA, incomingEdges);

                    if (outgoingEdgesMap.containsKey(nodeA)) {
                        outgoingEdges = outgoingEdgesMap.get(nodeA);
                    }
                    outgoingEdges.add(outgoingEdge);
                    outgoingEdgesMap.put(nodeA, outgoingEdges);

                    if (incomingEdgesMap.containsKey(nodeB)) {
                        outgoingEdges = incomingEdgesMap.get(nodeB);
                    }
                    outgoingEdges.add(outgoingEdge);
                    incomingEdgesMap.put(nodeB, outgoingEdges);

                    if (outgoingEdgesMap.containsKey(nodeB)) {
                        incomingEdges = outgoingEdgesMap.get(nodeB);
                    }
                    incomingEdges.add(incomingEdge);
                    incomingEdgesMap.put(nodeB, incomingEdges);

                }
            }

            this.networkId = networkId;
            this.nodeAToNodeBMap = nodeAToNodeBMap;
            this.directed = true;
            this.edges = edges;
            this.incomingEdgesMap = incomingEdgesMap;
            this.outgoingEdgesMap = outgoingEdgesMap;
        }

        public DataFeedNetworkTopology build() {
            return new DataFeedNetworkTopology(this);
        }
    }
}
