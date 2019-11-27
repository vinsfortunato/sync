package net.touchmania.game.resource.lazy;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import net.touchmania.game.util.ui.TexturePath;

public class RegionResource extends TextureResource {
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
