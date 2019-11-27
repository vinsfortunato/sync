package net.touchmania.game.resource.lazy;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import net.touchmania.game.Game;
import net.touchmania.game.util.ui.TexturePath;

public class TextureResource extends DrawableResource {
    public final TexturePath path;
    public Pixmap.Format format;
    public Texture.TextureFilter minFilter;
    public Texture.TextureFilter magFilter;
    public Texture.TextureWrap uWrap;
    public Texture.TextureWrap vWrap;
    public boolean useMipMaps = false;

    public TextureResource(TexturePath path) {
        super();
        this.path = path;
    }

    public TextureResource(TextureResource resource) {
        super(resource);
        path = resource.path;
        minFilter = resource.minFilter;
        magFilter = resource.magFilter;
        uWrap = resource.uWrap;
        vWrap = resource.vWrap;
    }

    @Override
    public TextureRegionDrawable get() {
        AssetManager assets = Game.instance().getAssets();
        AssetDescriptor<Texture> descriptor = getAssetDescriptor();

        if(assets.isLoaded(descriptor)) {
            Texture texture = assets.get(descriptor);
            TextureRegionDrawable drawable = new TextureRegionDrawable(texture);
            drawable.setLeftWidth(leftWidth);
            drawable.setRightWidth(rightWidth);
            drawable.setTopHeight(topHeight);
            drawable.setBottomHeight(bottomHeight);
            return drawable;
        }

        return null;
    }

    @Override
    public boolean isAvailable() {
        AssetManager assets = Game.instance().getAssets();
        AssetDescriptor<Texture> descriptor = getAssetDescriptor();

        return assets.isLoaded(descriptor);
    }

    @Override
    public boolean isLoading() {
        return false; //TODO
    }

    @Override
    public void load() {
        AssetManager assets = Game.instance().getAssets();
        AssetDescriptor<Texture> descriptor = getAssetDescriptor();

        if(!assets.isLoaded(descriptor)) {
            assets.load(getAssetDescriptor());
        }
    }

    @Override
    public TextureResource copy() {
        return new TextureResource(this);
    }

    protected AssetDescriptor<Texture> getAssetDescriptor() {
        TextureLoader.TextureParameter parameter = new TextureLoader.TextureParameter();
        parameter.format = format;
        parameter.minFilter = minFilter != null ? minFilter : Texture.TextureFilter.Nearest;
        parameter.magFilter = magFilter != null ? magFilter : Texture.TextureFilter.Nearest;
        parameter.wrapU = uWrap != null ? uWrap : Texture.TextureWrap.ClampToEdge;
        parameter.wrapV = vWrap != null ? vWrap : Texture.TextureWrap.ClampToEdge;
        parameter.genMipMaps = useMipMaps;
        return new AssetDescriptor<>(path.getFile(), Texture.class, parameter);
    }
}
