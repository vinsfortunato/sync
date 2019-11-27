package net.touchmania.game.resource.lazy;

/**
 * A resource that needs to be loaded before being used.
 * @param <T>
 */
public interface Resource<T> {
    /**
     * Gets the resource. Will return null if the resource is not available.
     * Check {@link #isAvailable()} to see if the resource is available.
     * @return the resource.
     */
    T get();

    /**
     * Checks if the resource is available. Some resources need to be loaded
     * before being available.
     * @return true if the resource is available, false otherwise.
     */
    boolean isAvailable();

    /**
     * Checks if the resource is currently loading.
     * @return true if the resource is loading, false otherwise.
     */
    boolean isLoading();

    /**
     * Loads the resource.
     */
    void load();
}
