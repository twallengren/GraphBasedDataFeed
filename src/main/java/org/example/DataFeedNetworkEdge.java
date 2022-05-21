package org.example;

import java.util.List;
import java.util.Map;

public class DataFeedNetworkEdge extends AbstractNetworkEdge {

    DataFeedNetworkEdge(Builder builder) {
        super(builder.edgeId, builder.nodeA, builder.nodeB, builder.directed);
    }

    public static class Builder {
        private final String edgeId;
        private final NetworkNode nodeA;
        private final NetworkNode nodeB;
        private final boolean directed;

        public Builder(String edgeId, NetworkNode nodeA, NetworkNode nodeB) {
            this.edgeId = edgeId;
            this.nodeA = nodeA;
            this.nodeB = nodeB;
            this.directed = true;
        }

        public DataFeedNetworkEdge build() {
            return new DataFeedNetworkEdge(this);
        }
    }
}
