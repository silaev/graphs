package com.silaev.graphs.service;

import com.silaev.graphs.dao.GraphDAOImpl;
import com.silaev.graphs.model.Edge;
import com.silaev.graphs.model.Node;
import com.silaev.graphs.testmodel.Pair;
import com.silaev.graphs.testmodel.TestUserData;
import com.silaev.graphs.testmodel.TestUserDataWithoutEqualsAndHashCode;
import com.silaev.graphs.testutil.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Konstantin Silaev on 9/20/2019
 */
class GraphOperationServiceImplTest {
    @Test
    void shouldAddVertex() {
        // GIVEN
        @SuppressWarnings("unchecked") final GraphDAOImpl<TestUserData> graphDAO =
            mock(GraphDAOImpl.class);
        @SuppressWarnings("unchecked") final NodeService<TestUserData> nodeService =
            mock(NodeService.class);
        final GraphOperationServiceImpl<TestUserData> graphOperationService =
            new GraphOperationServiceImpl<>(graphDAO, nodeService);
        final TestUserData testUserData = mock(TestUserData.class);
        @SuppressWarnings("unchecked") final Node<TestUserData> node = mock(Node.class);
        when(nodeService.getNode(testUserData)).thenReturn(node);

        // WHEN
        graphOperationService.addVertex(testUserData);

        // THEN
        verify(graphDAO, times(1)).findByUserData(testUserData);
        verify(graphDAO, times(1)).addNode(node);
    }

    @ParameterizedTest(name = "{index}: testUserData: {0}, testEdges: {1}, pathPair: {2}, path: {3}")
    @CsvSource(value = {
        "0,1,2,3,10,15,13 @ 0,10,1,15; 1,2; 2,0,3; 15,13; 10,0 @ 10,13 @ 10,0,15,13",
        "0,1,2,3,10,15,13 @ 0,10,1,15; 1,2; 2,0,3; 15,13 @ 10,13 @ ",
        "5,10 @ @ 5,10 @ ",
        "5,10,15,18,9 @ 5,10; 10,15; 15,18; 18,9; 9,5 @ 5,18 @ 5,10,15,18",
    }, delimiter = '@')
    void shouldGetPath(
        @ConvertWith(TestUtil.StringToTestUserData.class) final Stream<TestUserData> testUserData,
        @ConvertWith(TestUtil.StringToTestEdge.class) final List<Pair<String, List<String>>> testEdges,
        @ConvertWith(TestUtil.StringToPathPair.class) final Pair<String, String> pathPair,
        @ConvertWith(TestUtil.StringToTestUserData.class) final Stream<TestUserData> path
    ) {
        // GIVEN
        final GraphDAOImpl<TestUserData> graphDAO = new GraphDAOImpl<>();
        final NodeService<TestUserData> nodeService = new NodeServiceImpl<>();
        final GraphOperationServiceImpl<TestUserData> graphOperationService =
            new GraphOperationServiceImpl<>(graphDAO, nodeService);

        final Map<String, TestUserData> testUserDataMap = testUserData
            .collect(Collectors.toMap(TestUserData::getId, Function.identity()));

        testUserDataMap.values().forEach(graphOperationService::addVertex);

        List<Edge<TestUserData>> edges = testEdges.stream()
            .map(e -> mapTestEdges(e, testUserDataMap))
            .flatMap(p -> p.getRight().stream().map(u -> new Edge<>(p.getLeft(), u)))
            .collect(Collectors.toList());
        edges.forEach(graphOperationService::addEdge);

        // WHEN
        final List<Edge<TestUserData>> pathActual = graphOperationService.getPath(
            testUserDataMap.get(pathPair.getLeft()),
            testUserDataMap.get(pathPair.getRight())
        );

        // THEN
        assertNotNull(pathActual);
        assertEquals(
            preparePairEdges(path.collect(Collectors.toList())),
            pathActual
        );
    }

    private <T> List<Edge<T>> preparePairEdges(final List<T> path) {
        return IntStream.range(1, path.size())
            .mapToObj(i -> new Edge<>(path.get(i - 1), path.get(i)))
            .collect(Collectors.toList());
    }

    private Pair<TestUserData, List<TestUserData>> mapTestEdges(
        final Pair<String, List<String>> testEdge,
        final Map<String, TestUserData> testUserData
    ) {
        final List<TestUserData> userDataNodes = testEdge.getRight().stream()
            .map(testUserData::get)
            .collect(Collectors.toList());

        return new Pair<>(
            testUserData.get(testEdge.getLeft()),
            userDataNodes
        );
    }

    @Test
    void shouldAddEdge() {
        // GIVEN
        @SuppressWarnings("unchecked") final GraphDAOImpl<TestUserData> graphDAO =
            mock(GraphDAOImpl.class);
        @SuppressWarnings("unchecked") final NodeService<TestUserData> nodeService =
            mock(NodeService.class);
        final GraphOperationServiceImpl<TestUserData> graphOperationService =
            spy(new GraphOperationServiceImpl<>(graphDAO, nodeService));
        final TestUserData testUserData1 = mock(TestUserData.class);
        final TestUserData testUserData2 = mock(TestUserData.class);
        @SuppressWarnings("unchecked") final Node<TestUserData> node1 = mock(Node.class);
        @SuppressWarnings("unchecked") final Node<TestUserData> node2 = mock(Node.class);
        doReturn(node1).when(graphOperationService)
            .findByUserDataOrThrowException(testUserData1);
        doReturn(node2).when(graphOperationService)
            .findByUserDataOrThrowException(testUserData2);

        // WHEN
        graphOperationService.addEdge(new Edge<>(testUserData1, testUserData2));

        // THEN
        verify(node1, times(1)).addChild(node2);
    }

    @Test
    void shouldNotAddVertexBecauseOfEqualsEndHashCode() {
        // GIVEN
        final GraphDAOImpl<TestUserDataWithoutEqualsAndHashCode> graphDAO = new GraphDAOImpl<>();
        final NodeService<TestUserDataWithoutEqualsAndHashCode> nodeService = new NodeServiceImpl<>();
        final GraphOperationServiceImpl<TestUserDataWithoutEqualsAndHashCode> graphOperationService =
            new GraphOperationServiceImpl<>(graphDAO, nodeService);
        final TestUserDataWithoutEqualsAndHashCode testUserDataWithoutEqualsAndHashCode =
            new TestUserDataWithoutEqualsAndHashCode("0");

        // WHEN
        final Executable executable =
            () -> graphOperationService.addVertex(testUserDataWithoutEqualsAndHashCode);

        // THEN
        assertThrows(IllegalArgumentException.class, executable);
    }
}
