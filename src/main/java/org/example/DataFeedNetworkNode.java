package org.example;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.logging.Logger;

public class DataFeedNetworkNode<X,Y> extends AbstractNetworkNode {

    private final Function<X,X> transferFunction;
    private final BiFunction<X,Y,Y> aggregatingFunction;
    private final Function<X,Boolean> triggerFunction;
    private final Logger logger = Logger.getLogger(DataFeedNetworkNode.class.getName());

    DataFeedNetworkNode(Builder<X,Y> builder) {
        super(builder.nodeId);
        this.transferFunction = builder.transferFunction;
        this.aggregatingFunction = builder.aggregatingFunction;
        this.triggerFunction = builder.triggerFunction;
        logger.info("DataFeedNetworkNode " + builder.nodeId + " created.");
    }

    public X applyTransferFunction(X toInput) {
        if (triggerFunction.apply(toInput)) {
            logger.info("Applying node " + getNodeId() + " transfer function.");
            return transferFunction.apply(toInput);
        } else {
            logger.info("Node " + getNodeId() + " transfer function not triggered.");
            return null;
        }
    }

    public Y applyAggregatingFunction(X value, Y aggregate) {
        if (triggerFunction.apply(value)) {
            logger.info("Applying node " + getNodeId() + " aggregating function.");
            return aggregatingFunction.apply(value, aggregate);
        } else {
            logger.info("Node " + getNodeId() + " aggregating function not triggered.");
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataFeedNetworkNode<?,?> that = (DataFeedNetworkNode<?,?>) o;
        return getNodeId().equals(that.getNodeId())
                && transferFunction.equals(that.transferFunction)
                && triggerFunction.equals(that.triggerFunction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNodeId());
    }

    public static class Builder<X,Y> {

        private final String nodeId;
        private final Function<X,X> transferFunction;
        private final BiFunction<X,Y,Y> aggregatingFunction;
        private final Function<X,Boolean> triggerFunction;

        Builder(String nodeId, Function<X,X> transferFunction, BiFunction<X,Y,Y> aggregatingFunction, Function<X,Boolean> triggerFunction) {
            this.nodeId = nodeId;
            this.transferFunction = transferFunction;
            this.aggregatingFunction = aggregatingFunction;
            this.triggerFunction = triggerFunction;
        }

        public DataFeedNetworkNode<X,Y> build() {
            return new DataFeedNetworkNode<>(this);
        }
    }
}
