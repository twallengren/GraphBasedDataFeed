package org.example;

/**
 * Represents edge in a graph. Can be directed or undirected. If directed, nodeA -> nodeB.
 */
public interface NetworkEdge {

    String edgeId = null;
    NetworkNode nodeA = null;
    NetworkNode nodeB = null;
    boolean isDirected = true;
}
