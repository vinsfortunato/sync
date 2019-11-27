package net.touchmania.game.resource.xml.parsers;

import com.badlogic.gdx.files.FileHandle;
import net.touchmania.game.resource.xml.XmlMusicLoader;
import net.touchmania.game.resource.xml.XmlTheme;
import net.touchmania.game.resource.xml.resolvers.XmlReferenceValueResolver;
import net.touchmania.game.util.xml.XmlParseException;
import net.touchmania.game.util.xml.XmlParser;

public class XmlMusicsParser extends XmlMapResourceParser<XmlMusicLoader> {
    private final XmlTheme theme;
    private XmlMusicLoaderResolver musicLoaderResolver;

    /**
     * Create a resource parser from its file.
     * @param resourceFile the resource file.
     */
    public XmlMusicsParser(FileHandle resourceFile, XmlTheme theme) {
        super(resourceFile);
        this.theme = theme;

        //Create a new instance for every reference resolver.
        this.musicLoaderResolver = new XmlMusicLoaderResolver();
    }

    @Override
    protected void checkRoot(XmlParser.Element root) throws XmlParseException {
        if(!root.getName().equals("musics")) {
            throw new XmlParseException("Unexpected xml root element name. Expected to be 'musics'!");
        }
    }

    @Override
    protected void checkRootChild(XmlParser.Element element) throws XmlParseException {
        if(!element.getName().equals("music")) {
            throw new XmlParseException(String.format(
                    "Unexpected element name '%s'! Expected to be 'music'!", element.getName()));
        }
    }

    @Override
    protected XmlReferenceValueResolver<XmlMusicLoader> getResolver(XmlParser.Element element) {
        return musicLoaderResolver;
    }

    private class XmlMusicLoaderResolver extends XmlReferenceValueResolver<XmlMusicLoader> {
        @Override
        protected String getResourceTypeName() {
            return "music";
        }

        @Override
        public XmlMusicLoader resolveReference(String resourceId) {
            XmlMusicLoader loader = getResolvedValues().get(resourceId);
            return new XmlMusicLoader(loader);
        }

        @Override
        public XmlMusicLoader resolveValue(String value) throws XmlParseException {
            if(value == null || value.isEmpty()) {
                throw new XmlParseException("Invalid music file! File name cannot be null or empty!");
            }
            return new XmlMusicLoader(theme, getResourceFile().sibling("musics").sibling(value));
        }
    }
}
