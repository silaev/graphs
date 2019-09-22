package com.silaev.graphs.service;

import com.silaev.graphs.model.Edge;

import java.util.List;

/**
 * Provides basic operations on a graph.
 *
 * @param <T> user defined type
 * @author Konstantin Silaev on 9/20/2019
 */
public interface GraphOperationService<T> {
    void addVertex(T vertex);

    List<Edge<T>> getPath(final T startVertex, final T targetVertex);

    void addEdge(Edge<T> edge);
}
