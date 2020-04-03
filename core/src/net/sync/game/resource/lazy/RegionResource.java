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
