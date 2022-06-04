package org.example.examples.collatz;

import org.example.components.data.DataFeedDataPacket;

public class CollatzDataPacket extends DataFeedDataPacket<CollatzDataA,CollatzDataB> {

    public CollatzDataPacket(CollatzDataA value, CollatzDataB aggregate) {
        super(value, aggregate);
    }
}
