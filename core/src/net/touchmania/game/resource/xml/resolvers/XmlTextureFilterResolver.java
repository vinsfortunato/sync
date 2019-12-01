package net.touchmania.game.resource.xml.resolvers;

import com.badlogic.gdx.graphics.Texture;
import net.touchmania.game.util.xml.XmlParseException;
import net.touchmania.game.util.xml.XmlValueResolver;

public class XmlTextureFilterResolver implements XmlValueResolver<Texture.TextureFilter> {
    public static final XmlTextureFilterResolver GLOBAL_TEXTURE_FILTER_RESOLVER = new XmlTextureFilterResolver();

    @Override
    public Texture.TextureFilter resolve(String value) throws XmlParseException {
        switch(value.trim().toLowerCase()) {
            case "nearest": return Texture.TextureFilter.Nearest;
            case "linear": return Texture.TextureFilter.Linear;
            case "mipmap": return Texture.TextureFilter.MipMap;
            case "mipmapnearestnearest":
            case "mipmap_nearest_nearest": return Texture.TextureFilter.MipMapNearestNearest;
            case "mipmapnearestlinear":
            case "mipmap_nearest_linear": return Texture.TextureFilter.MipMapNearestLinear;
            case "mipmaplinearlinear":
            case "mipmap_linear_linear": return Texture.TextureFilter.MipMapLinearLinear;
            case "mipmaplinearnearest":
            case "mipmap_linear_nearest": return Texture.TextureFilter.MipMapLinearNearest;
        }

        throw new XmlParseException(String.format("Invalid filter format for value '%s'!", value));
    }
}
