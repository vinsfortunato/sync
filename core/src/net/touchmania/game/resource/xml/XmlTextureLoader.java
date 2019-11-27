package net.touchmania.game.resource.xml;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import net.touchmania.game.Game;
import net.touchmania.game.util.ui.TexturePath;

import java.io.IOException;
import java.util.Objects;

public class XmlTextureLoader extends XmlDrawableLoader {
    public TexturePath path;
    public Format format;
    public TextureFilter minFilter;
    public TextureFilter magFilter;
    public TextureWrap uWrap;
    public TextureWrap vWrap;
    public boolean useMipMaps = false;

    public XmlTextureLoader(XmlTheme theme, TexturePath path) {
        super(theme);
        this.path = path;
    }

    public XmlTextureLoader(XmlTextureLoader loader) {
        super(loader);
        path = loader.path;
        minFilter = loader.minFilter;
        magFilter = loader.magFilter;
        uWrap = loader.uWrap;
        vWrap = loader.vWrap;
    }

    @Override
    public Drawable load() throws Exception {
        Texture texture = loadTexture();
        //TODO set min/mag filters;
        //TODO set uWrap/vWrap
        TextureRegionDrawable drawable = new TextureRegionDrawable(texture);
        drawable.setLeftWidth(leftWidth);
        drawable.setRightWidth(rightWidth);
        drawable.setTopHeight(topHeight);
        drawable.setBottomHeight(bottomHeight);
        return drawable;
    }

    protected Texture loadTexture() throws Exception {
        FileHandle file = path.getFile();
        if(!file.exists()) {
            throw new IOException(String.format("Texture file not found at '%s'!", file.path()));
        }

        HashFunction hf = Hashing.murmur3_128();
        HashCode hc = hf.newHasher()
                .putString(file.path(), Charsets.UTF_8)
                .putInt(Objects.hashCode(format))
                .putBoolean(useMipMaps).hash();

        System.out.println("LOAD asset!");
        AssetManager assets = Game.instance().getAssets();
        AssetDescriptor<Texture> descriptor = new AssetDescriptor<>(file, Texture.class);
        assets.load(descriptor);
        assets.finishLoadingAsset(descriptor);
        return assets.get(descriptor);

        /**
        return theme.load(hc.asLong(), Texture.class, () -> {
            System.out.println("ACTUAL LOAD!");
            return new Texture(file, format, useMipMaps);
        });
         **/
    }

    @Override
    public XmlTextureLoader copy() {
        return new XmlTextureLoader(this);
    }
}
