package org.nebula.engine.interfaces;

/**
 * <br>
 * <h2>IDisposable</h2>
 * <br>
 * The IDisposable interface represents an object that can be disposed of,
 * releasing associated resources.
 * <p>
 * Implementing classes or objects are expected to provide a method, {@link IDisposable#dispose()},
 * that performs cleanup and resource release operations.
 * </p>
 *
 * @author Anton Schoenfeld
 *
 * @see java.lang.AutoCloseable
 */
@FunctionalInterface
public interface IDisposable
{

    /**
     * Performs cleanup and releases associated resources.
     * <p>
     * Implementers of this method should release any resources held by the object and
     * perform cleanup operations.
     * </p>
     */
    void dispose();
}