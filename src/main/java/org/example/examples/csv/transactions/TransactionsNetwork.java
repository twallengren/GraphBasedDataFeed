package org.example.examples.csv.transactions;

import org.example.components.network.DataFeedNetwork;

public class TransactionsNetwork extends DataFeedNetwork<TransactionsDataA,TransactionsDataB> {

    protected TransactionsNetwork(Builder<TransactionsDataA,TransactionsDataB> builder) {
        super(builder);
    }
}
