package org.example;

import java.util.List;

/**
 * Represents node in a graph (directed or undirected).
 */
public interface NetworkNode {

    String nodeId = null;
    List<NetworkEdge> edges = null;
}
