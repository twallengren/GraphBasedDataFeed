package org.example;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

abstract class AbstractNetworkNode implements NetworkNode {

    private String nodeId;
    private List<NetworkEdge> edges;
    private final Logger logger = Logger.getLogger(AbstractNetworkNode.class.getName());

    AbstractNetworkNode(String nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractNetworkNode that = (AbstractNetworkNode) o;
        return nodeId.equals(that.nodeId) && edges.equals(that.edges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeId, edges);
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public List<NetworkEdge> getEdges() {
        return edges;
    }

    public void setEdges(List<NetworkEdge> edges) {
        this.edges = edges;
    }

    public void addEdge(NetworkEdge edge) {
        this.edges.add(edge);
    }

    public void removeEdge(NetworkEdge edge) {
        this.edges.remove(edge);
    }

    public void removeEdge(String edgeId) {
        int edgeIndex = findEdgeIndex(edgeId);
        if (edgeIndex >= 0) {
            this.edges.remove(edgeIndex);
        }
    }

    private int findEdgeIndex(String edgeIdToFind) {
        for (int edgeIndex = 0; edgeIndex < this.edges.size(); edgeIndex++) {
            NetworkEdge edge = this.edges.get(edgeIndex);
            if (edge.getEdgeId().equals(edgeIdToFind)) {
                return edgeIndex;
            }
        }
        logger.info("No edge found with edge ID: " + edgeIdToFind);
        return -1;
    }
}
