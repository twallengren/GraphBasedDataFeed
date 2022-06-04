package org.example.components.data;

import java.util.function.BiFunction;
import java.util.function.Function;

public class DataFeedFunctions<A,X,B,Y> {

    private final Function<A,A> preProcessingFunction;
    private final Function<A,X> dataTransferFunctionA;
    private final BiFunction<B,X,B> aggregatingFunction;
    private final Function<B,Y> dataTransferFunctionB;
    private final BiFunction<A,Y,A> feedbackFunction;
    private final BiFunction<A,B,Boolean> triggerFunction;

    public DataFeedFunctions(Function<A, A> preProcessingFunction, Function<A, X> dataTransferFunctionA, BiFunction<B, X, B> aggregatingFunction, Function<B, Y> dataTransferFunctionB, BiFunction<A, Y, A> feedbackFunction, BiFunction<A,B,Boolean> triggerFunction) {
        this.preProcessingFunction = preProcessingFunction;
        this.dataTransferFunctionA = dataTransferFunctionA;
        this.aggregatingFunction = aggregatingFunction;
        this.dataTransferFunctionB = dataTransferFunctionB;
        this.feedbackFunction = feedbackFunction;
        this.triggerFunction = triggerFunction;
    }

    public A applyPreProcessingFunction(A inputA) {
        return preProcessingFunction.apply(inputA);
    }

    public X applyDataTransferFunctionA(A inputA) {
        return dataTransferFunctionA.apply(inputA);
    }

    public B applyAggregatingFunction(B inputB, X inputX) {
        return aggregatingFunction.apply(inputB, inputX);
    }

    public Y applyDataTransferFunctionB(B inputB) {
        return dataTransferFunctionB.apply(inputB);
    }

    public A applyFeedBackFunction(A inputA, Y inputY) {
        return feedbackFunction.apply(inputA, inputY);
    }

    public Boolean applyTriggerFunction(A inputA, B inputB) {
        return triggerFunction.apply(inputA, inputB);
    }

    public Function<A, A> getPreProcessingFunction() {
        return preProcessingFunction;
    }

    public Function<A, X> getDataTransferFunctionA() {
        return dataTransferFunctionA;
    }

    public BiFunction<B, X, B> getAggregatingFunction() {
        return aggregatingFunction;
    }

    public Function<B, Y> getDataTransferFunctionB() {
        return dataTransferFunctionB;
    }

    public BiFunction<A, Y, A> getFeedbackFunction() {
        return feedbackFunction;
    }

    public BiFunction<A, B, Boolean> getTriggerFunction() {
        return triggerFunction;
    }
}
