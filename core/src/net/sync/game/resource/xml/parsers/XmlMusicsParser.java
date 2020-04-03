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

package net.sync.game.resource.xml.parsers;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import net.sync.game.resource.lazy.MusicResource;
import net.sync.game.resource.lazy.Resource;
import net.sync.game.resource.xml.XmlReferenceNotCompatibleException;
import net.sync.game.resource.xml.XmlReferenceNotFoundException;
import net.sync.game.resource.xml.XmlTheme;
import net.sync.game.resource.xml.resolvers.XmlMusicResolver;
import net.sync.game.resource.xml.resolvers.XmlReferenceResolver;
import net.sync.game.util.xml.XmlParseException;
import net.sync.game.util.xml.XmlParser;

public class XmlMusicsParser extends XmlMapResourceParser<Resource<Music>> {
    private final net.sync.game.resource.xml.XmlTheme theme;
    private XmlMusicResolver musicResolver = new XmlMusicResolver() {
        @Override
        public Resource<Music> resolveReference(String resourceId) throws XmlReferenceNotFoundException, net.sync.game.resource.xml.XmlReferenceNotCompatibleException {
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
