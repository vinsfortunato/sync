package net.touchmania.game.util;

public interface Loader<T> {
    T load() throws Exception;
}
