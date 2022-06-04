package org.example.examples.collatz;

import org.example.components.data.DataFeedDataPacket;

public class CollatzDataPacket extends DataFeedDataPacket<CollatzDataA,CollatzDataB> {

    public CollatzDataPacket(CollatzDataA valueA, CollatzDataB valueB) {
        super(valueA, valueB);
    }
}
