package net.touchmania.game.resource.xml;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class XmlRegionLoader extends XmlDrawableLoader {
    public int x;
    public int y;
    public int width;
    public int height;

    public XmlRegionLoader(XmlTheme theme) {
        super(theme);
    }

    public XmlRegionLoader(XmlRegionLoader loader) {
        super(loader);
        x = loader.x;
        y = loader.y;
        width = loader.width;
        height = loader.height;
    }

    @Override
    public XmlRegionLoader copy() {
        return new XmlRegionLoader(this);
    }

    @Override
    public Drawable load() throws Exception {
        return null;
    }
}
