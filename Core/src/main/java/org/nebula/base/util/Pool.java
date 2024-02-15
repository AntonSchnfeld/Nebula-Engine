package org.nebula.base.util;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.List;

public abstract class Pool<T extends Poolable> {
    protected final Deque<T> poolables;

    public Pool() {
        poolables = new ArrayDeque<>();
    }

    protected abstract T newPoolable();

    public T get() {
        return poolables.isEmpty() ? newPoolable() : poolables.pop();
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

    public void addAll(Collection<T> poolables) {
        this.poolables.addAll(poolables);
    }
}
