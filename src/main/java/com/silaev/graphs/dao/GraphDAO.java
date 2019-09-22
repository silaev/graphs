package com.silaev.graphs.dao;

import com.silaev.graphs.model.Node;

import java.util.Optional;

/**
 * Data access object to manipulate a graph.
 *
 * @param <T> user defined type
 * @author Konstantin Silaev on 9/20/2019
 */
public interface GraphDAO<T> {
    Optional<Node<T>> findByUserData(final T data);

    void addNode(final Node<T> node);
}
