package org.example;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.logging.Logger;

public class DataFeedNetworkNode<X,Y,Z> extends AbstractNetworkNode {

    private final Function<X,X> preProcessingFunction;
    private final Function<X,Z> dataTransferFunction;
    private final BiFunction<Z,Y,Y> aggregatingFunction;
    private final BiFunction<X,Y,Boolean> triggerFunction;
    private final Logger logger = Logger.getLogger(DataFeedNetworkNode.class.getName());

    DataFeedNetworkNode(Builder<X,Y,Z> builder) {
        super(builder.nodeId);
        this.preProcessingFunction = builder.preProcessingFunction;
        this.dataTransferFunction = builder.dataTransferFunction;
        this.aggregatingFunction = builder.aggregatingFunction;
        this.triggerFunction = builder.triggerFunction;
        logger.info("DataFeedNetworkNode " + builder.nodeId + " created.");
    }

    public X applyPreProcessingFunction(X input) {
        logger.info("Applying node " + getNodeId() + " pre-processing function.");
        return preProcessingFunction.apply(input);
    }

    public Z applyDataTransferFunction(X input) {
        logger.info("Applying node " + getNodeId() + " data transfer function.");
        return dataTransferFunction.apply(input);
    }

    public Y applyAggregatingFunction(Z value, Y aggregate) {
        logger.info("Applying node " + getNodeId() + " aggregating function.");
        return aggregatingFunction.apply(value, aggregate);
    }

    public DataFeedDataPacket<X,Y> processPacket(DataFeedDataPacket<X,Y> dataFeedDataPacket) {
        X value = dataFeedDataPacket.getValue();
        Y aggregate = dataFeedDataPacket.getAggregate();
        if (triggerFunction.apply(value, aggregate)) {
            X processedValue = applyPreProcessingFunction(value);
            Z transformedValue = applyDataTransferFunction(processedValue);
            Y aggregatedValue = applyAggregatingFunction(transformedValue, aggregate);
            dataFeedDataPacket.setValue(processedValue);
            dataFeedDataPacket.setAggregate(aggregatedValue);
            return dataFeedDataPacket;
        } else {
            logger.info("Node " + getNodeId() + " not triggered by data packet " + dataFeedDataPacket);
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataFeedNetworkNode<?,?,?> that = (DataFeedNetworkNode<?,?,?>) o;
        return getNodeId().equals(that.getNodeId())
                && preProcessingFunction.equals(that.preProcessingFunction)
                && dataTransferFunction.equals(that.dataTransferFunction)
                && aggregatingFunction.equals(that.aggregatingFunction)
                && triggerFunction.equals(that.triggerFunction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNodeId());
    }

    public static class Builder<X,Y,Z> {

        private final String nodeId;
        private final Function<X,X> preProcessingFunction;
        private final Function<X,Z> dataTransferFunction;
        private final BiFunction<Z,Y,Y> aggregatingFunction;
        private final BiFunction<X,Y,Boolean> triggerFunction;

        Builder(String nodeId, Function<X,X> preProcessingFunction, Function<X,Z> dataTransferFunction, BiFunction<Z,Y,Y> aggregatingFunction, BiFunction<X,Y,Boolean> triggerFunction) {
            this.nodeId = nodeId;
            this.preProcessingFunction = preProcessingFunction;
            this.dataTransferFunction = dataTransferFunction;
            this.aggregatingFunction = aggregatingFunction;
            this.triggerFunction = triggerFunction;
        }

        public DataFeedNetworkNode<X,Y,Z> build() {
            return new DataFeedNetworkNode<>(this);
        }
    }
}
