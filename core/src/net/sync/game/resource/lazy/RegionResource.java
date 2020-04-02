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

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import net.sync.game.util.ui.TexturePath;

public class RegionResource extends net.sync.game.resource.lazy.TextureResource {
    public int x;
    public int y;
    public int width;
    public int height;

    public RegionResource(TexturePath path) {
        super(path);
    }

    public RegionResource(TextureResource resource) {
        super(resource);
    }

    public RegionResource(RegionResource resource) {
        super(resource);
        x = resource.x;
        y = resource.y;
        width = resource.width;
        height = resource.height;
    }

    @Override
    public TextureRegionDrawable get() {
        TextureRegionDrawable drawable = super.get();
        if(drawable == null) return null;

        TextureRegion region = drawable.getRegion();
        region.setRegionX(x);
        region.setRegionY(y);
        if(width > 0)
            region.setRegionWidth(width);
        if(height > 0)
            region.setRegionHeight(height);
        drawable.setMinWidth(region.getRegionWidth());
        drawable.setMinHeight(region.getRegionHeight());
        return drawable;
    }

    @Override
    public RegionResource copy() {
        return new RegionResource(this);
    }
}
