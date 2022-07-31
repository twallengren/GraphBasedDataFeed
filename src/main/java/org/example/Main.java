package org.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.example.examples.collatz.CollatzNetwork;
import org.example.examples.integrator.IntegratorNetwork;

import java.io.FileReader;
import java.io.Reader;
import java.util.logging.Logger;

public class Main {

    private static final CollatzNetwork collatzNetwork = new CollatzNetwork.Builder("0").build();
    private static final IntegratorNetwork integratorNetwork = new IntegratorNetwork.Builder("0").build();
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

//        // collatz network
//        String aggregate = collatzNetwork.startFromValue(25);
//        logger.info("Collatz aggregate: " + aggregate);
//
//        // integrator network
//        Double integral = integratorNetwork.integrate(Function.identity(), 0.0, 1.0, 0.1);
//        logger.info("Integrator value: " + integral);

        testReader();
    }

    public static void testReader() {
        try {
            Reader reader = new FileReader("src/main/java/org/example/examples/csv/data/RawData - Sheet1.csv");
            logger.info("reader gr8");

            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
            for (CSVRecord record : records) {
                logger.info("record " + record);
            }
        } catch (Exception e) {
            logger.info("aw fuck m8");
        } finally {
            logger.info("finally");
        }
        logger.info("we out");
    }
}