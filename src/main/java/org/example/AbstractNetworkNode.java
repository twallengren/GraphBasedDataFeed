package org.example;

import java.util.List;
import java.util.Objects;

abstract class AbstractNetworkNode implements NetworkNode {

    private String nodeId;
    private List<NetworkEdge> edges;

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
}
