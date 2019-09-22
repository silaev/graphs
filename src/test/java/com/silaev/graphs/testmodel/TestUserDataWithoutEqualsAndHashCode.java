package com.silaev.graphs.testmodel;

/**
 * @author Konstantin Silaev on 9/20/2019
 */
public class TestUserDataWithoutEqualsAndHashCode {
    private final String id;

    public TestUserDataWithoutEqualsAndHashCode(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "UserData{" +
            "id='" + id + '\'' +
            '}';
    }
}
