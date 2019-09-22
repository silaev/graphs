package com.silaev.graphs.testutil;

import com.silaev.graphs.testmodel.Pair;
import com.silaev.graphs.testmodel.TestUserData;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.platform.commons.JUnitException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestUtil {
    private TestUtil() {

    }

    public static class StringToTestEdge extends SimpleArgumentConverter {
        @Override
        protected List<Pair<String, List<String>>> convert(Object source, Class<?> targetType) {
            return Optional.ofNullable(source)
                .map(s -> ((String) s).split("\\s*;\\s*"))
                .map(a -> Stream.of(a)
                    .map(this::mapTestEdge)
                    .collect(Collectors.toList())
                )
                .orElseGet(Collections::emptyList);
        }

        private Pair<String, List<String>> mapTestEdge(String userTestEdge) {
            String[] nodes = userTestEdge.split("\\s*,\\s*");

            return new Pair<>(
                nodes[0],
                Stream.of(nodes).skip(1).collect(Collectors.toList())
            );
        }
    }

    public static class StringToTestUserData extends SimpleArgumentConverter {
        @Override
        protected Stream<TestUserData> convert(Object source, Class<?> targetType) {
            return Optional.ofNullable(source)
                .map(s -> ((String) s).split("\\s*,\\s*"))
                .map(a -> Stream.of(a)
                    .map(this::mapUserData)
                )
                .orElseGet(Stream::empty);
        }

        private TestUserData mapUserData(String userDataString) {
            return new TestUserData(userDataString);
        }
    }

    public static class StringToPathPair extends SimpleArgumentConverter {
        @Override
        protected Pair<String, String> convert(Object source, Class<?> targetType) {
            String[] groups = ((String) source).split("\\s*,\\s*");
            if (groups.length != 2) {
                throw new JUnitException("StringToPathPair should hav only 2 values");
            }
            return new Pair<>(groups[0], groups[1]);
        }
    }
}
