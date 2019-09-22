package com.silaev.graphs.service;

import com.silaev.graphs.model.Node;
import com.silaev.graphs.testmodel.TestUserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

/**
 * Serves the need to increase test coverage.
 * No direct need to test.
 *
 * @author Konstantin Silaev on 9/22/2019
 */
class NodeServiceImplTest {
    private final NodeService<TestUserData> nodeService = new NodeServiceImpl<>();

    @Test
    void shouldGetNode() {
        //GIVEN
        final TestUserData mock = mock(TestUserData.class);

        //WHEN
        Node<TestUserData> node = nodeService.getNode(mock);

        //THEN
        assertNotNull(node);
    }
}
