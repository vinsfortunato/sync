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
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import net.sync.game.util.ui.TexturePath;

import java.util.UUID;

import static net.sync.game.Game.assets;

public class TextureResource extends DrawableResource {
    public final TexturePath path;
    private String assetId;

    public TextureResource(TexturePath path) {
        super();
        this.path = path;
    }

    public TextureResource(TextureResource resource) {
        super(resource);
        path = resource.path;

        assets()
    }

    @Override
    public synchronized TextureRegionDrawable get() {
        if(isAvailable()) {
            return assets().get(assetId);
        }
        throw new IllegalStateException("Resource not loaded");
    }

    @Override
    public synchronized boolean isAvailable() {
        return assetId != null && assets().isLoaded(assetId);
    }

    @Override
    public synchronized boolean isLoading() {
        return assetId != null && !assets().isLoaded(assetId);
    }

    @Override
    public synchronized void load() {
        if(assetId == null) {
            assetId = UUID.randomUUID().toString(); //TODO track generated UUIDs?
            AssetDescriptor<Texture> descriptor = new AssetDescriptor<Texture>();


        }

    }

    @Override
    public synchronized void unload() {

    }

    @Override
    public TextureResource copy() {
        return new TextureResource(this);
    }

    protected AssetLoaderParameters<Texture> getLoaderParameters() {
        TextureLoader.TextureParameter parameter = new TextureLoader.TextureParameter();
        parameter.format = format;
        parameter.minFilter = minFilter != null ? minFilter : Texture.TextureFilter.Nearest;
        parameter.magFilter = magFilter != null ? magFilter : Texture.TextureFilter.Nearest;
        parameter.wrapU = uWrap != null ? uWrap : Texture.TextureWrap.ClampToEdge;
        parameter.wrapV = vWrap != null ? vWrap : Texture.TextureWrap.ClampToEdge;
        parameter.genMipMaps = useMipMaps;
        return parameter;
    }
}
