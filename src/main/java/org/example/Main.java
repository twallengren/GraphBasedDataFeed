package org.example;

public class Main {
    public static void main(String[] args) {
        DataFeedNetwork network = new DataFeedNetwork.Builder("0")
                .addTriggerRuleNode("A0")
                .addTriggerRuleNode("B0")
                .addProcessingRuleNode("A1")
                .addProcessingRuleNode("B1")
                .addConnection("A0", "B0")
                .addConnection("A0", "A1")
                .addConnection("A0", "B1")
                .addConnection("B0", "A1")
                .addConnection("B0", "B1")
                .build();
        System.out.println("boobies");
    }
}