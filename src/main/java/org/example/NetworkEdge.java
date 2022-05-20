package org.example;

/**
 * Represents edge in a graph. Can be directed or undirected. If directed, nodeA -> nodeB.
 */
public interface NetworkEdge {

    String edgeId = null;
    boolean isDirected = true;
    NetworkNode nodeA = null;
    NetworkNode nodeB = null;
}