package net.touchmania.game.resource.xml;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class XmlRegionLoader extends XmlDrawableLoader {
    public int x;
    public int y;
    public int width;
    public int height;

    public XmlRegionLoader() {
        super();
    }

    public XmlRegionLoader(XmlRegionLoader loader) {
        super(loader);
        this.x = loader.x;
        this.y = loader.y;
        this.width = loader.width;
        this.height = loader.height;
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
