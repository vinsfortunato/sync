package net.touchmania.game.resource.xml.resolvers;

import com.badlogic.gdx.graphics.Pixmap.Format;
import net.touchmania.game.util.xml.XmlParseException;
import net.touchmania.game.util.xml.XmlValueResolver;

public class XmlPixmapFormatResolver implements XmlValueResolver<Format> {
    public static final XmlPixmapFormatResolver GLOBAL_PIXMAP_FORMAT_RESOLVER = new XmlPixmapFormatResolver();

    @Override
    public Format resolve(String value) throws XmlParseException {
        //TODO
        switch(value.trim().toLowerCase()) {

        }

        throw new XmlParseException(String.format("Invalid pixmap format for value '%s'!", value));
    }
}
