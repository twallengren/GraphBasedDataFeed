package org.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public class AbstractNetworkTopology implements NetworkTopology {

    private final String networkId;
    private final Map<NetworkNode, List<NetworkNode>> nodeAToNodeBMap;
    private final Logger logger = Logger.getLogger(AbstractNetworkTopology.class.getName());

    AbstractNetworkTopology(Builder builder) {
        this.networkId = builder.networkId;
        this.nodeAToNodeBMap = builder.nodeAToNodeBMap;
    }

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

    public String getNetworkId() { return networkId; }

    public Map<NetworkNode, List<NetworkNode>> getNodeAToNodeBMap() {
        return nodeAToNodeBMap;
    }

    public static class Builder {
        private final String networkId;
        private Map<NetworkNode, List<NetworkNode>> nodeAToNodeBMap;

        public Builder(String networkId, Map<NetworkNode, List<NetworkNode>> nodeAToNodeBMap) {
            this.networkId = networkId;
            this.nodeAToNodeBMap = nodeAToNodeBMap;
        }

        public AbstractNetworkTopology build() {
            return new AbstractNetworkTopology(this);
        }
    }
}
