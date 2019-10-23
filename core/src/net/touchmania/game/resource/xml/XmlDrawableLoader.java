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

    public XmlDrawableLoader copy() {
        XmlDrawableLoader copy = create();
        copy.minWidth = minWidth;
        copy.minHeight = minHeight;
        copy.leftWidth = leftWidth;
        copy.rightWidth = rightWidth;
        copy.topHeight = topHeight;
        copy.bottomHeight = bottomHeight;
        return copy;
    }

    protected abstract XmlDrawableLoader create();
}
