package org.example;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.logging.Logger;

public class DataFeedNetworkNode<A,X,B,Y> extends AbstractNetworkNode {

    private final Function<A,A> preProcessingFunction;
    private final Function<A,X> dataTransferFunctionA;
    private final BiFunction<B,X,B> aggregatingFunction;
    private final Function<B,Y> dataTransferFunctionB;
    private final BiFunction<A,Y,A> feedbackFunction;
    private final BiFunction<A,B,Boolean> triggerFunction;
    private final Logger logger = Logger.getLogger(DataFeedNetworkNode.class.getName());

    DataFeedNetworkNode(Builder<A,X,B,Y> builder) {
        super(builder.nodeId);
        this.preProcessingFunction = builder.preProcessingFunction;
        this.dataTransferFunctionA = builder.dataTransferFunctionA;
        this.aggregatingFunction = builder.aggregatingFunction;
        this.dataTransferFunctionB = builder.dataTransferFunctionB;
        this.feedbackFunction = builder.feedbackFunction;
        this.triggerFunction = builder.triggerFunction;
        logger.info("DataFeedNetworkNode " + builder.nodeId + " created.");
    }

    public A applyPreProcessingFunction(A input) {
        logger.info("Applying node " + getNodeId() + " pre-processing function.");
        return preProcessingFunction.apply(input);
    }

    public X applyDataTransferFunctionA(A input) {
        logger.info("Applying node " + getNodeId() + " data transfer function A.");
        return dataTransferFunctionA.apply(input);
    }

    public B applyAggregatingFunction(B aggregate, X value) {
        logger.info("Applying node " + getNodeId() + " aggregating function.");
        return aggregatingFunction.apply(aggregate, value);
    }

    public Y applyDataTransferFunctionB(B input) {
        logger.info("Applying node " + getNodeId() + " data transfer function B.");
        return dataTransferFunctionB.apply(input);
    }

    public A applyFeedbackFunction(A input, Y feedback) {
        logger.info("Applying node " + getNodeId() + " feedback function.");
        return feedbackFunction.apply(input, feedback);
    }

    public DataFeedDataPacket<A,B> processPacket(DataFeedDataPacket<A,B> dataFeedDataPacket) {
        if (dataFeedDataPacket == null) {
            logger.info("Data feed packet null. Nothing processed.");
            return null;
        }
        A value = dataFeedDataPacket.getValue();
        B aggregate = dataFeedDataPacket.getAggregate();
        if (triggerFunction.apply(value, aggregate)) {
            A processedValue = applyPreProcessingFunction(value);
            X transformedValueA = applyDataTransferFunctionA(processedValue);
            B aggregatedValue = applyAggregatingFunction(aggregate, transformedValueA);
            Y transformedValueB = applyDataTransferFunctionB(aggregatedValue);
            A feedbackValue = applyFeedbackFunction(processedValue, transformedValueB);
            dataFeedDataPacket.setValue(feedbackValue);
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
        DataFeedNetworkNode<?,?,?,?> that = (DataFeedNetworkNode<?,?,?,?>) o;
        return getNodeId().equals(that.getNodeId())
                && preProcessingFunction.equals(that.preProcessingFunction)
                && dataTransferFunctionA.equals(that.dataTransferFunctionA)
                && aggregatingFunction.equals(that.aggregatingFunction)
                && dataTransferFunctionB.equals(that.dataTransferFunctionB)
                && feedbackFunction.equals(that.feedbackFunction)
                && triggerFunction.equals(that.triggerFunction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNodeId());
    }

    public static class Builder<A,X,B,Y> {

        private final String nodeId;
        private final Function<A,A> preProcessingFunction;
        private final Function<A,X> dataTransferFunctionA;
        private final BiFunction<B,X,B> aggregatingFunction;
        private final Function<B,Y> dataTransferFunctionB;
        private final BiFunction<A,Y,A> feedbackFunction;
        private final BiFunction<A,B,Boolean> triggerFunction;

        Builder(String nodeId, Function<A, A> preProcessingFunction, Function<A, X> dataTransferFunctionA, BiFunction<B,X,B> aggregatingFunction, Function<B,Y> dataTransferFunctionB, BiFunction<A,Y,A> feedbackFunction, BiFunction<A,B,Boolean> triggerFunction) {
            this.nodeId = nodeId;
            this.preProcessingFunction = preProcessingFunction;
            this.dataTransferFunctionA = dataTransferFunctionA;
            this.aggregatingFunction = aggregatingFunction;
            this.dataTransferFunctionB = dataTransferFunctionB;
            this.feedbackFunction = feedbackFunction;
            this.triggerFunction = triggerFunction;
        }

        public DataFeedNetworkNode<A,X,B,Y> build() {
            return new DataFeedNetworkNode<>(this);
        }
    }
}
