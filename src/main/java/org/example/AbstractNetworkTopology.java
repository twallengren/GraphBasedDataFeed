package org.example;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class AbstractNetworkTopology implements NetworkTopology {

    private Map<NetworkNode, Set<NetworkNode>> nodeAToNodeBMap;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractNetworkTopology that = (AbstractNetworkTopology) o;
        return nodeAToNodeBMap.equals(that.nodeAToNodeBMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeAToNodeBMap);
    }
}
