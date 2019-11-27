package net.touchmania.game.resource.lazy;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

//TODO
public class NinepatchResource extends DrawableResource {
    /**
     * Copy constructor
     *
     * @param resource
     */
    public NinepatchResource(DrawableResource resource) {
        super(resource);
    }

    @Override
    public DrawableResource copy() {
        return null;
    }

    @Override
    public Drawable get() {
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
