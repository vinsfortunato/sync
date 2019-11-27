package net.touchmania.game.resource.lazy;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public abstract class DrawableResource implements Resource<Drawable> {
    public float leftWidth;
    public float rightWidth;
    public float topHeight;
    public float bottomHeight;

    /**
     * Copy constructor
     * @param resource
     */
    public DrawableResource(DrawableResource resource) {
        leftWidth = resource.leftWidth;
        rightWidth = resource.rightWidth;
        topHeight = resource.topHeight;
        bottomHeight = resource.bottomHeight;
    }

    public DrawableResource() {}

    public abstract DrawableResource copy();
}
