package org.example;

import org.example.examples.collatz.CollatzNetwork;
import org.example.examples.integrator.IntegratorNetwork;

import java.util.function.Function;
import java.util.logging.Logger;

public class Main {

    private static final CollatzNetwork collatzNetwork = new CollatzNetwork.Builder("0").build();
    private static final IntegratorNetwork integratorNetwork = new IntegratorNetwork.Builder("0").build();
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

        // collatz network
        String aggregate = collatzNetwork.startFromValue(25);
        logger.info("Collatz aggregate: " + aggregate);

        // integrator network
        Double integral = integratorNetwork.integrate(Function.identity(), 0.0, 1.0, 0.1);
        logger.info("Integrator value: " + integral);
    }
}