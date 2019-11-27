package net.touchmania.game.resource.xml;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import net.touchmania.game.util.ui.TexturePath;

public class XmlRegionLoader extends XmlTextureLoader {
    public int x;
    public int y;
    public int width;
    public int height;

    public XmlRegionLoader(XmlTheme theme, TexturePath path) {
        super(theme, path);
    }

    public XmlRegionLoader(XmlTextureLoader loader) {
        super(loader);
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
        //Load texture
        Texture texture = loadTexture();

        //Set region
        TextureRegion region = new TextureRegion(texture);
        region.setRegionX(x);
        region.setRegionY(y);
        if(width > 0)
            region.setRegionWidth(width);
        if(height > 0)
            region.setRegionHeight(height);

        //Set drawable
        TextureRegionDrawable drawable = new TextureRegionDrawable(region);
        drawable.setLeftWidth(leftWidth);
        drawable.setRightWidth(rightWidth);
        drawable.setTopHeight(topHeight);
        drawable.setBottomHeight(bottomHeight);
        return drawable;
    }
}
