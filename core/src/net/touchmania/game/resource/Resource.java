package net.touchmania.game.resource;

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
     * @return
     */
    boolean isAvailable();
}
