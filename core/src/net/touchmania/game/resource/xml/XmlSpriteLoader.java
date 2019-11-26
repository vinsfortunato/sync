package net.touchmania.game.resource.xml;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class XmlSpriteLoader extends XmlDrawableLoader {
    public XmlSpriteLoader(XmlTheme theme) {
        super(theme);
    }

    public XmlSpriteLoader(XmlSpriteLoader loader) {
        super(loader);
    }

    @Override
    public XmlSpriteLoader copy() {
        return new XmlSpriteLoader(this);
    }

    @Override
    public Drawable load() throws Exception {
        return null;
    }
}
