package net.touchmania.game.resource.xml;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class XmlTextureLoader extends XmlDrawableLoader {
    public TextureFilter minFilter;
    public TextureFilter magFilter;
    public TextureWrap uWrap;
    public TextureWrap vWrap;

    public XmlTextureLoader() {}

    public XmlTextureLoader(XmlTextureLoader loader) {
        super(loader);
        loader.minFilter = minFilter;
        loader.magFilter = magFilter;
        loader.uWrap = uWrap;
        loader.vWrap = vWrap;
    }

    @Override
    public Drawable load() throws Exception {
        return null; //TODO
    }

    @Override
    public XmlTextureLoader copy() {
        return new XmlTextureLoader(this);
    }
}
