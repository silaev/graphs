package com.silaev.graphs.model;

import java.util.Objects;

/**
 * Immutable class representing an edge in a graph.
 * Note that startVertex must have a directs connection to endVertex
 * without any omission. For example, a graph A-&#60;B-&#60;C should have the
 * following edges: Edge(A,B), Edge(B,C).
 *
 * @param <T> user defined type
 * @author Konstantin Silaev on 9/20/2019
 */
public class Edge<T> {
    private final T startVertex;
    private final T endVertex;

    public Edge(T startVertex, T endVertex) {
        this.startVertex = startVertex;
        this.endVertex = endVertex;
    }

    public T getStartVertex() {
        return startVertex;
    }

    public T getEndVertex() {
        return endVertex;
    }

    @Override
    public String toString() {
        return "Edge{" +
            "startVertex=" + startVertex +
            ", endVertex=" + endVertex +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge)) return false;
        Edge<?> edge = (Edge<?>) o;
        return getStartVertex().equals(edge.getStartVertex()) &&
            getEndVertex().equals(edge.getEndVertex());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStartVertex(), getEndVertex());
    }
}
