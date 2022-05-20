package org.example;

import java.util.List;

/**
 * Represents node in a graph (directed or undirected).
 */
public interface NetworkNode {

    String nodeId = null;
    List<NetworkEdge> edges = null;

    String getNodeId();

    void setNodeId(String nodeId);

    List<NetworkEdge> getEdges();

    void setEdges(List<NetworkEdge> edges);

    void addEdge(NetworkEdge edge);

    void removeEdge(NetworkEdge edge);

    void removeEdge(String edgeId);
}
