package com.silaev.graphs.service;

import com.silaev.graphs.dao.GraphDAO;
import com.silaev.graphs.dao.GraphDAOImpl;
import com.silaev.graphs.exception.DuplicateUserDataException;
import com.silaev.graphs.exception.UserDataNotFoundException;
import com.silaev.graphs.model.Edge;
import com.silaev.graphs.model.Node;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Provides basic operations on a graph.
 *
 * @param <T> user defined type
 * @author Konstantin Silaev on 9/20/2019
 */
public class GraphOperationServiceImpl<T> implements GraphOperationService<T> {
    private final GraphDAO<T> graphDAO;
    private final NodeService<T> nodeService;

    public GraphOperationServiceImpl() {
        this.graphDAO = new GraphDAOImpl<>();
        this.nodeService = new NodeServiceImpl<>();
    }

    public GraphOperationServiceImpl(
        final GraphDAO<T> graphDAO,
        final NodeService<T> nodeService
    ) {
        this.graphDAO = graphDAO;
        this.nodeService = nodeService;
    }

    private boolean isVisited(
        final Node<T> node,
        final Set<Node<T>> visited
    ) {
        if (visited.contains(node)) {
            return true;
        }
        visited.add(node);
        return false;
    }

    /**
     * Adds a vertex of user type to a graph.
     *
     * @param vertex of user type, must have overridden equals and hash code directly
     *               or within an ancestor except for the Object class
     */
    @Override
    public void addVertex(final T vertex) {
        Objects.requireNonNull(vertex);

        verifyEqualsAndHashCodePresent(vertex);

        graphDAO.findByUserData(vertex).ifPresent(d -> {
            throw new DuplicateUserDataException(
                String.format("Duplicate user data: %s", d.toString()));
        });
        graphDAO.addNode(nodeService.getNode(vertex));
    }

    private void verifyEqualsAndHashCodePresent(T vertex) {
        final Class<?> vertexClass = vertex.getClass();
        Method methodHashCode = null;
        Method methodEquals = null;
        try {
            methodHashCode = vertexClass.getMethod("hashCode");
            methodEquals = vertexClass.getMethod("equals", Object.class);
        } catch (NoSuchMethodException e) {
            throwExceptionBecauseEqualsAndHashCodeNotPresent(vertexClass.getCanonicalName());
        }
        if (methodHashCode.getDeclaringClass() == Object.class ||
            methodEquals.getDeclaringClass() == Object.class) {
            throwExceptionBecauseEqualsAndHashCodeNotPresent(vertexClass.getCanonicalName());
        }
    }

    private void throwExceptionBecauseEqualsAndHashCodeNotPresent(final String className) {
        throw new IllegalArgumentException(
            String.format("Class: %s should have overridden equals and hash code", className)
        );
    }

    /**
     * Gets a path between 2 vertices.
     *
     * @param startVertex  of user type, must have overridden equals and hash code directly
     *                     or within an ancestor except for the Object class
     * @param targetVertex of user type, must have overridden equals and hash code directly
     *                     or within an ancestor except for the Object class
     * @return the list of edges.
     */
    @Override
    public List<Edge<T>> getPath(final T startVertex, final T targetVertex) {
        Objects.requireNonNull(startVertex);
        Objects.requireNonNull(targetVertex);

        final Node<T> startNode = findByUserDataOrThrowException(startVertex);
        final Node<T> targetNode = findByUserDataOrThrowException(targetVertex);

        final Queue<List<Node<T>>> queue = new LinkedList<>();
        final Set<Node<T>> visited = new HashSet<>();
        List<Node<T>> pathToNode;

        queue.add(Collections.singletonList(startNode));

        Node<T> currentNode;
        while (!queue.isEmpty()) {
            pathToNode = queue.poll();
            currentNode = pathToNode.get(pathToNode.size() - 1);

            if (currentNode.getData().equals(targetNode.getData())) {
                return preparePairEdges(pathToNode);
            }

            for (Node<T> nextNode : currentNode.getChildren()) {

                if (!isVisited(nextNode, visited)) {
                    final List<Node<T>> pathToNextNode = new ArrayList<>(pathToNode);
                    pathToNextNode.add(nextNode);
                    queue.add(pathToNextNode);
                }
            }
        }

        return Collections.unmodifiableList(
            Collections.emptyList()
        );
    }

    private List<Edge<T>> preparePairEdges(final List<Node<T>> pathToNode) {
        return IntStream.range(1, pathToNode.size())
            .mapToObj(
                i -> new Edge<>(
                    pathToNode.get(i - 1).getData(),
                    pathToNode.get(i).getData()
                )
            )
            .collect(
                Collectors.collectingAndThen(
                    Collectors.toList(),
                    Collections::unmodifiableList
                )
            );
    }

    Node<T> findByUserDataOrThrowException(final T vertex) {
        Objects.requireNonNull(vertex);

        return graphDAO.findByUserData(vertex)
            .orElseThrow(() -> new UserDataNotFoundException(
                String.format(
                    "User data has not been found. Please, check that you had called a proper addVertex method: %s",
                    vertex.toString()))
            );
    }

    /**
     * Adds an edge to a graph. Note that startVertex and endVertex
     * should have been added via the addVertex method.
     *
     * @param edge an edge to add
     */
    @Override
    public void addEdge(final Edge<T> edge) {
        Objects.requireNonNull(edge);

        final T startVertex = edge.getStartVertex();
        final T endVertex = edge.getEndVertex();

        Objects.requireNonNull(startVertex);
        Objects.requireNonNull(endVertex);

        final Node<T> nodeStart = findByUserDataOrThrowException(startVertex);
        final Node<T> nodeEnd = findByUserDataOrThrowException(endVertex);

        nodeStart.addChild(nodeEnd);
    }
}
