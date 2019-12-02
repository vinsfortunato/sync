package net.touchmania.game.resource.xml.parsers;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import net.touchmania.game.resource.lazy.MusicResource;
import net.touchmania.game.resource.lazy.Resource;
import net.touchmania.game.resource.xml.XmlReferenceNotCompatibleException;
import net.touchmania.game.resource.xml.XmlReferenceNotFoundException;
import net.touchmania.game.resource.xml.XmlTheme;
import net.touchmania.game.resource.xml.resolvers.XmlMusicResolver;
import net.touchmania.game.resource.xml.resolvers.XmlReferenceResolver;
import net.touchmania.game.util.xml.XmlParseException;
import net.touchmania.game.util.xml.XmlParser;

public class XmlMusicsParser extends XmlMapResourceParser<Resource<Music>> {
    private final XmlTheme theme;
    private XmlMusicResolver musicResolver = new XmlMusicResolver() {
        @Override
        public Resource<Music> resolveReference(String resourceId) throws XmlReferenceNotFoundException, XmlReferenceNotCompatibleException {
            Resource<Music> resource = getResolvedValueOrThrow(resourceId);

            if(resource instanceof MusicResource)
                return new MusicResource((MusicResource) resource);

            throw XmlReferenceNotCompatibleException.incompatibleType(resource.getClass(), MusicResource.class);
        }

        @Override
        public Resource<Music> resolveValue(String value) {
            return new MusicResource(getResourceFile().sibling("musics").sibling(value));
        }
    };

    /**
     * Create a resource parser from its file.
     * @param resourceFile the resource file.
     */
    public XmlMusicsParser(FileHandle resourceFile, XmlTheme theme) {
        super(resourceFile);
        this.theme = theme;
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
    protected XmlReferenceResolver<Resource<Music>> getResolver(XmlParser.Element element) {
        return musicResolver;
    }
}
