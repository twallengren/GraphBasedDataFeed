package org.example.components.node;

import org.example.components.data.DataFeedDataPacket;
import org.example.components.data.DataFeedFunctions;

import java.util.Objects;
import java.util.logging.Logger;

public class DataFeedNetworkNode<A,X,B,Y> extends AbstractNetworkNode {
    private final DataFeedFunctions<A,X,B,Y> functions;
    private final Logger logger = Logger.getLogger(DataFeedNetworkNode.class.getName());

    DataFeedNetworkNode(Builder<A,X,B,Y> builder) {
        super(builder.nodeId);
        this.functions = builder.functions;
        logger.info("DataFeedNetworkNode " + builder.nodeId + " created.");
    }

    public A applyPreProcessingFunction(A inputA) {
        logger.info("Applying node " + getNodeId() + " pre-processing function.");
        return functions.applyPreProcessingFunction(inputA);
    }

    public X applyDataTransferFunctionA(A inputA) {
        logger.info("Applying node " + getNodeId() + " data transfer function A.");
        return functions.applyDataTransferFunctionA(inputA);
    }

    public B applyAggregatingFunction(B inputB, X inputX) {
        logger.info("Applying node " + getNodeId() + " aggregating function.");
        return functions.applyAggregatingFunction(inputB, inputX);
    }

    public Y applyDataTransferFunctionB(B inputB) {
        logger.info("Applying node " + getNodeId() + " data transfer function B.");
        return functions.applyDataTransferFunctionB(inputB);
    }

    public A applyFeedbackFunction(A inputA, Y inputY) {
        logger.info("Applying node " + getNodeId() + " feedback function.");
        return functions.applyFeedBackFunction(inputA, inputY);
    }

    public Boolean applyTriggerFunction(DataFeedDataPacket<A,B> dataFeedDataPacket) {
        logger.info("Applying node " + getNodeId() + " trigger function.");
        return functions.applyTriggerFunction(dataFeedDataPacket.getValueA(), dataFeedDataPacket.getValueB());
    }

    public DataFeedDataPacket<A,B> processPacket(DataFeedDataPacket<A,B> dataFeedDataPacket) {
        if (dataFeedDataPacket == null) {
            logger.info("Data feed packet null. Nothing processed.");
            return null;
        }
        if (applyTriggerFunction(dataFeedDataPacket)) {
            A value = dataFeedDataPacket.getValueA();
            B aggregate = dataFeedDataPacket.getValueB();
            A processedValue = applyPreProcessingFunction(value);
            X transformedValueA = applyDataTransferFunctionA(processedValue);
            B aggregatedValue = applyAggregatingFunction(aggregate, transformedValueA);
            Y transformedValueB = applyDataTransferFunctionB(aggregatedValue);
            A feedbackValue = applyFeedbackFunction(processedValue, transformedValueB);
            dataFeedDataPacket.setValueA(feedbackValue);
            dataFeedDataPacket.setValueB(aggregatedValue);
        } else {
            logger.info("Node " + getNodeId() + " not triggered by data packet " + dataFeedDataPacket);
        }
        return dataFeedDataPacket;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataFeedNetworkNode<?,?,?,?> that = (DataFeedNetworkNode<?,?,?,?>) o;
        return getNodeId().equals(that.getNodeId())
                && functions.equals(that.functions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNodeId());
    }

    public static class Builder<A,X,B,Y> {

        private final String nodeId;
        private final DataFeedFunctions<A,X,B,Y> functions;

        public Builder(String nodeId, DataFeedFunctions<A,X,B,Y> functions) {
            this.nodeId = nodeId;
            this.functions = functions;
        }

        public DataFeedNetworkNode<A,X,B,Y> build() {
            return new DataFeedNetworkNode<>(this);
        }
    }
}
