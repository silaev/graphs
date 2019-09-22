package com.silaev.graphs.testmodel;

import java.util.Objects;

/**
 * @author Konstantin Silaev on 9/20/2019
 */
public class TestUserData {
    private final String id;

    public TestUserData(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TestUserData)) return false;
        TestUserData testUserData = (TestUserData) o;
        return id.equals(testUserData.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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
