package net.touchmania.game.resource.lazy;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

//TODO
public class SoundResource implements Resource<Sound> {

    public SoundResource(FileHandle handle) {

    }

    /**
     * Copy constructor
     * @param resource
     */
    public SoundResource(SoundResource resource) {

    }

    @Override
    public Sound get() {
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
