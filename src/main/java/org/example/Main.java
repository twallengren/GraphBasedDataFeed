package org.example;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        NetworkNode nodeA0 = new TriggerRuleNode("A0");
        NetworkNode nodeA1 = new TriggerRuleNode("A1");
        NetworkNode nodeB0 = new TriggerRuleNode("B0");
        NetworkNode nodeB1 = new TriggerRuleNode("B1");
        Map<NetworkNode, List<NetworkNode>> networkMap = Map.of(
                nodeA0, List.of(nodeA1, nodeB1),
                nodeB0, List.of(nodeA1, nodeB1)
        );
        NetworkTopology networkTopology = new DataFeedNetworkTopology.Builder("0", networkMap).build();
        System.out.println(networkTopology.toString());
    }
}