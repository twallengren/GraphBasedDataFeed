package org.example;

import java.util.Objects;

public class DataFeedNetworkNode extends AbstractNetworkNode {

    private final NodeType nodeType;

    DataFeedNetworkNode(Builder builder) {
        super(builder.nodeId);
        this.nodeType = builder.nodeType;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataFeedNetworkNode that = (DataFeedNetworkNode) o;
        return this.getNodeId().equals(that.getNodeId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getNodeId(), nodeType);
    }

    public static class Builder {

        String nodeId;
        NodeType nodeType;

        Builder(String nodeId, NodeType nodeType) {
            this.nodeId = nodeId;
            this.nodeType = nodeType;
        }

        public DataFeedNetworkNode build() {
            return new DataFeedNetworkNode(this);
        }
    }
}
