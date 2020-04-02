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
