package com.silaev.graphs.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Immutable class representing a node in a graph.
 *
 * @param <T> user defined type
 * @author Konstantin Silaev on 9/20/2019
 */
public class Node<T> {
    private final String id;
    private final ArrayList<Node<T>> children;
    private final T data;

    public Node(T data) {
        this.id = UUID.randomUUID().toString();
        this.data = data;
        this.children = new ArrayList<>();
    }

    public T getData() {
        return data;
    }

    public void addChild(Node<T> node) {
        children.add(node);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        Node<?> node = (Node<?>) o;
        return id.equals(node.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public List<Node<T>> getChildren() {
        return Collections.unmodifiableList(
            new ArrayList<>(children)
        );
    }

    @Override
    public String toString() {
        return "Node{" +
            "data=" + data +
            '}';
    }
}
