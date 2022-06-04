package org.example.examples.integrator;

import org.example.components.data.DataFeedFunctions;

import java.util.function.BiFunction;
import java.util.function.Function;

public class IntegratorFunctions extends DataFeedFunctions<IntegratorDataA,Double,IntegratorDataB,Double> {

    private static final Function<IntegratorDataA,IntegratorDataA> preProcessingFunction = Function.identity();
    private static final Function<IntegratorDataA,Double> dataTransferFunctionA = integratorDataA -> {
        Double value = integratorDataA.getLowerBound();
        Double stepSize = integratorDataA.getStepSize();
        Function<Double,Double> function = integratorDataA.getFunction();
        return stepSize*(function.apply(value + stepSize) + function.apply(value))/2;
    };
    private static final BiFunction<IntegratorDataB,Double,IntegratorDataB> aggregatingFunction = (integratorDataB, dF) -> {
        integratorDataB.setIntegralValue(integratorDataB.getIntegralValue() + dF);
        return integratorDataB;
    };
    private static final Function<IntegratorDataB,Double> dataTransferFunctionB = integratorDataB -> null;
    private static final BiFunction<IntegratorDataA,Double,IntegratorDataA> feedbackFunction = (integratorDataA, feedbackValue) -> {
        integratorDataA.setLowerBound(integratorDataA.getLowerBound() + integratorDataA.getStepSize());
        return integratorDataA;
    };
    private static final BiFunction<IntegratorDataA,IntegratorDataB,Boolean> triggerFunction = (integratorDataA, integratorDataB) -> integratorDataA.getLowerBound()+ integratorDataA.getStepSize() <= integratorDataA.getUpperBound();

    public IntegratorFunctions() {
        super(preProcessingFunction, dataTransferFunctionA, aggregatingFunction, dataTransferFunctionB, feedbackFunction, triggerFunction);
    }
}
