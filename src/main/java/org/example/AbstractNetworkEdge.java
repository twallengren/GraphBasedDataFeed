package org.example;

import java.util.Objects;

public class AbstractNetworkEdge implements NetworkEdge {

    private String edgeId;
    private NetworkNode nodeA;
    private NetworkNode nodeB;
    private boolean isDirected;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractNetworkEdge that = (AbstractNetworkEdge) o;
        return isDirected == that.isDirected && edgeId.equals(that.edgeId) && nodeA.equals(that.nodeA) && nodeB.equals(that.nodeB);
    }

    @Override
    public int hashCode() {
        return Objects.hash(edgeId, nodeA, nodeB, isDirected);
    }

    @Override
    public String getEdgeId() {
        return null;
    }

    @Override
    public String getNodeA() {
        return null;
    }
}
