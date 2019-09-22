package com.silaev.graphs.service;

import com.silaev.graphs.model.Node;

/**
 * Provides an ability to mock for testing.
 *
 * @param <T> user defined type
 * @author Konstantin Silaev on 9/20/2019
 */
public interface NodeService<T> {
    Node<T> getNode(T vertex);
}
