package net.touchmania.game.resource.xml;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import net.touchmania.game.util.Copy;
import net.touchmania.game.util.Loader;

public abstract class XmlDrawableLoader implements Loader<Drawable>, Copy {
    public final XmlTheme theme;
    public float leftWidth;
    public float rightWidth;
    public float topHeight;
    public float bottomHeight;

    public XmlDrawableLoader(XmlTheme theme) {
        this.theme = theme;
    }

    /**
     * Copy constructor.
     * @param loader the loader to copy.
     */
    public XmlDrawableLoader(XmlDrawableLoader loader) {
        theme = loader.theme;
        leftWidth = loader.leftWidth;
        rightWidth = loader.rightWidth;
        topHeight = loader.topHeight;
        bottomHeight = loader.bottomHeight;
    }

    @Override
    public abstract XmlDrawableLoader copy();
}
