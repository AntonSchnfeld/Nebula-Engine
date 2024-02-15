package org.nebula.base.util;

import java.util.ArrayDeque;
import java.util.Deque;

public abstract class Pool<T extends Poolable> {
    protected final Deque<T> poolables;

    public Pool() {
        poolables = new ArrayDeque<>();
    }

    protected abstract T newPoolable();

    public T get() {
        final T poolable = poolables.pop();
        return poolable == null ? newPoolable() : poolable;
    }

    public Deque<T> getAll() {
        return poolables;
    }

    public void returnPoolable(T poolable) {
        poolable.clean();
        poolables.push(poolable);
    }

    public void clear() {
        poolables.clear();
    }
}
