package org.example.examples.collatz;

import org.example.components.data.DataFeedFunctions;

import java.util.function.BiFunction;
import java.util.function.Function;

public class CollatzFunctions extends DataFeedFunctions<CollatzDataA,String,CollatzDataB,CollatzDataB> {

    private static final Function<CollatzDataA,CollatzDataA> preProcessingFunction = collatzDataA -> {
        if (collatzDataA.getValue() % 2 == 0) {
            collatzDataA.setValue(collatzDataA.getValue()/2);
        } else {
            collatzDataA.setValue(3*collatzDataA.getValue()+1);
        }
        return collatzDataA;
    };
    private static final Function<CollatzDataA,String> dataTransferFunctionA = String::valueOf;
    private static final BiFunction<CollatzDataB,String,CollatzDataB> aggregatingFunction = (collatzDataB, stringValue) -> {
        collatzDataB.setAggregate(collatzDataB.getAggregate() + " " + stringValue);
        return collatzDataB;
    };
    private static final Function<CollatzDataB,CollatzDataB> dataTransferFunctionB = Function.identity();
    private static final BiFunction<CollatzDataA,CollatzDataB,CollatzDataA> feedbackFunction = (A,B) -> A;
    private static final BiFunction<CollatzDataA,CollatzDataB,Boolean> triggerFunction = (A,B) -> A.getValue() != 1;

    public CollatzFunctions() {
        super(preProcessingFunction, dataTransferFunctionA, aggregatingFunction, dataTransferFunctionB, feedbackFunction, triggerFunction);
    }
}
