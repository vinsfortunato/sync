package net.touchmania.game.resource.lazy;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;

//TODO
public class MusicResource implements Resource<Music> {

    public MusicResource(FileHandle handle) {

    }

    /**
     * Copy constructor
     * @param resource
     */
    public MusicResource(MusicResource resource) {

    }

    @Override
    public Music get() {
        return null;
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public boolean isLoading() {
        return false;
    }

    @Override
    public void load() {

    }
}
