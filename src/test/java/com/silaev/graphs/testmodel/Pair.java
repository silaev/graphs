package com.silaev.graphs.testmodel;

/**
 * @author Konstantin Silaev on 9/20/2019
 */
public class Pair<L, R> {
    final L left;
    final R right;

    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public R getRight() {
        return right;
    }

    public L getLeft() {
        return left;
    }
}
