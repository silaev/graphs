package com.silaev.graphs.service;

import com.silaev.graphs.model.Node;

/**
 * Provides an ability to mock for testing.
 *
 * @param <T> user defined type
 * @author Konstantin Silaev on 9/20/2019
 */
public class NodeServiceImpl<T> implements NodeService<T> {
    @Override
    public Node<T> getNode(final T vertex) {
        return new Node<>(vertex);
    }
}
