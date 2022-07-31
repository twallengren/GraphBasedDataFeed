package org.example.examples.csv.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;

public class Csv {

    private List<Row> rows;

    Csv(List<Row> rows) {
        this.rows = rows;
    }

    Csv(String name) throws FileNotFoundException {
        Reader reader = new FileReader("../data/RawData - Sheet1.csv");
    }
}
