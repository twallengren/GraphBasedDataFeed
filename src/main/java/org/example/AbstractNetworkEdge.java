package org.example;

import java.util.Objects;

abstract class AbstractNetworkEdge implements NetworkEdge {

    private final String edgeId;
    private final NetworkNode nodeA;
    private final NetworkNode nodeB;
    private final boolean directed;

    AbstractNetworkEdge(String edgeId, NetworkNode nodeA, NetworkNode nodeB, boolean directed) {
        this.edgeId = edgeId;
        this.nodeA = nodeA;
        this.nodeB = nodeB;
        this.directed = directed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractNetworkEdge that = (AbstractNetworkEdge) o;
        return edgeId.equals(that.edgeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(edgeId, nodeA, nodeB, directed);
    }

    @Override
    public String getEdgeId() {
        return edgeId;
    }

    @Override
    public NetworkNode getNodeA() {
        return nodeA;
    }

    @Override
    public NetworkNode getNodeB() {
        return nodeB;
    }
}
