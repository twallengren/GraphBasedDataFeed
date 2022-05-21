package org.example;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

abstract class AbstractNetworkTopology implements NetworkTopology {

    private final String networkId;
    private final Map<NetworkNode, List<NetworkNode>> nodeAToNodeBMap;

    private final boolean directed;
    private final Set<NetworkEdge> edges;

    AbstractNetworkTopology(String networkId, Map<NetworkNode, List<NetworkNode>> nodeAToNodeBMap,
                            boolean directed, Set<NetworkEdge> edges) {
        this.networkId = networkId;
        this.nodeAToNodeBMap = nodeAToNodeBMap;
        this.directed = directed;
        this.edges = edges;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractNetworkTopology that = (AbstractNetworkTopology) o;
        return networkId.equals(that.networkId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(networkId);
    }

    public String getNetworkId() { return networkId; }

    public Map<NetworkNode, List<NetworkNode>> getNodeAToNodeBMap() {
        return nodeAToNodeBMap;
    }

    public boolean isDirected() { return directed; }

    public Set<NetworkEdge> getEdges() { return edges; }
}
