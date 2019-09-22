package com.silaev.graphs.dao;

import com.silaev.graphs.model.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Data access object to manipulate a graph.
 *
 * @param <T> user defined type
 * @author Konstantin Silaev on 9/20/2019
 */
public class GraphDAOImpl<T> implements GraphDAO<T> {
    private final Map<T, Node<T>> nodes;

    public GraphDAOImpl() {
        this.nodes = new HashMap<>();
    }

    @Override
    public Optional<Node<T>> findByUserData(final T data) {
        return Optional.ofNullable(nodes.get(data));
    }

    @Override
    public void addNode(final Node<T> node) {
        nodes.put(node.getData(), node);
    }
}
