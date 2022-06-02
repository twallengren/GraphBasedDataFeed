package org.example.components.edge;

import org.example.components.node.NetworkNode;

/**
 * Represents edge in a graph. Can be directed or undirected. If directed, nodeA -> nodeB.
 */
public interface NetworkEdge {

    String getEdgeId();

    NetworkNode getNodeA();

    NetworkNode getNodeB();
}
