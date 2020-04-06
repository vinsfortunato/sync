/*
 * Copyright (c) 2020 Vincenzo Fortunato.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.sync.game.resource.lazy;

/**
 * A container for resources that needs to be loaded before being used.
 * <p>This can be used during rendering to load, unload and get the needed resource.
 * Loading and unloading tasks will be performed asynchronously. The loading
 * progress can be monitored by the provided status access methods.</p>
 * @param <T> the resource type.
 */
public interface Resource<T> {
    /**
     * Gets the resource. Will return null if the resource is not loaded.
     * Check {@link #isAvailable()} to see if the resource is loaded.
     * @return the resource.
     */
    T get();

    /**
     * Checks if the resource is loaded.
     * @return true if the resource is loaded, false otherwise.
     */
    boolean isAvailable();

    /**
     * Checks if the resource is currently loading.
     * @return true if the resource is loading, false otherwise.
     */
    boolean isLoading();

    /**
     * Start loading the resource. This method will not block and can be safely
     * called during rendering.
     * <p>When this method is called the wrapped resource will be loaded asynchronously.
     * The status of the resource can be monitored by calling {@link #isAvailable()} and
     * {@link #isLoading()}.</p>
     * <p>Calling this method when the resource is loaded or when the resource is already
     * loading will have no effect.</p>
     */
    void load();

    /**
     * Start unloading the resource. This method will not block and can be safely
     * called during rendering.
     * <p>When this method is called the wrapped resource will be unloaded asynchronously.</p>
     * <p>Calling this method when the resource is not loaded or when the resource is already
     * unloading will have no effect.</p>
     */
    void unload();
}
