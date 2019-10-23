package net.touchmania.game.resource.xml;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class XmlTextureDrawableLoader extends XmlDrawableLoader {
    @Override
    public Drawable load() throws Exception {
        return null; //TODO
    }

    @Override
    public XmlTextureDrawableLoader copy() {
        XmlTextureDrawableLoader copy = (XmlTextureDrawableLoader) super.copy();
        return copy;
    }

    @Override
    protected XmlTextureDrawableLoader create() {
        return new XmlTextureDrawableLoader();
    }
}
