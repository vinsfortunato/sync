package net.touchmania.game.resource.xml;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class XmlNinePatchLoader extends XmlDrawableLoader {
    public XmlNinePatchLoader(XmlTheme theme) {
        super(theme);
    }

    public XmlNinePatchLoader(XmlNinePatchLoader loader) {
        super(loader);
    }

    @Override
    public XmlNinePatchLoader copy() {
        return new XmlNinePatchLoader(this);
    }

    @Override
    public Drawable load() throws Exception {
        return null;
    }
}
