/*
 * Copyright 2020 Vincenzo Fortunato
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sync.game.resource.lazy;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import net.sync.game.Game;
import net.sync.game.util.ui.TexturePath;

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
        AssetManager assets = net.sync.game.Game.instance().getAssets();
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
        AssetManager assets = net.sync.game.Game.instance().getAssets();
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
