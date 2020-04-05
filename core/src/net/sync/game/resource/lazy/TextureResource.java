/*
 * Copyright (c) 2020 Vincenzo Fortunato.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.sync.game.resource.lazy;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import net.sync.game.util.ui.TexturePath;

import static net.sync.game.Game.assets;

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
        AssetDescriptor<Texture> descriptor = getAssetDescriptor();
        if(assets().isLoaded(descriptor)) {
            Texture texture = assets().get(descriptor);
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
        AssetDescriptor<Texture> descriptor = getAssetDescriptor();
        return assets().isLoaded(descriptor);
    }

    @Override
    public boolean isLoading() {
        AssetDescriptor<Texture> descriptor = getAssetDescriptor();
        return assets().contains(descriptor.fileName, descriptor.type) && !assets().isLoaded(descriptor);
    }

    @Override
    public void load() {
        AssetDescriptor<Texture> descriptor = getAssetDescriptor();
        if(!assets().isLoaded(descriptor)) {
            assets().load(getAssetDescriptor());
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
