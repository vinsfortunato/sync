package net.touchmania.game.resource.xml;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import net.touchmania.game.util.Loader;

public abstract class XmlDrawableLoader implements Loader<Drawable> {
    public float minWidth;
    public float minHeight;
    public float leftWidth;
    public float rightWidth;
    public float topHeight;
    public float bottomHeight;

    public XmlDrawableLoader() {}

    /**
     * Copy constructor.
     * @param loader the loader to copy.
     */
    public XmlDrawableLoader(XmlDrawableLoader loader) {
        loader.minWidth = minWidth;
        loader.minHeight = minHeight;
        loader.leftWidth = leftWidth;
        loader.rightWidth = rightWidth;
        loader.topHeight = topHeight;
        loader.bottomHeight = bottomHeight;
    }

    public abstract XmlDrawableLoader copy();
}
