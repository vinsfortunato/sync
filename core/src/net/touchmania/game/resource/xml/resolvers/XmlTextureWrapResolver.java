package net.touchmania.game.resource.xml.resolvers;

import com.badlogic.gdx.graphics.Texture.TextureWrap;
import net.touchmania.game.util.xml.XmlParseException;
import net.touchmania.game.util.xml.XmlValueResolver;

public class XmlTextureWrapResolver implements XmlValueResolver<TextureWrap> {
    public static final XmlTextureWrapResolver GLOBAL_TEXTURE_WRAP_RESOLVER = new XmlTextureWrapResolver();

    @Override
    public TextureWrap resolve(String value) throws XmlParseException {
        if(value == null || value.isEmpty()) {
            throw new XmlParseException("Invalid wrap value! Value cannot be null or empty!");
        }

        //TODO
        switch(value.trim().toLowerCase()) {

        }

        throw new XmlParseException(String.format("Invalid wrap format for value '%s'!", value));
    }
}
