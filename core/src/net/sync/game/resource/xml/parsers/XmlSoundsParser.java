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

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import net.sync.game.resource.lazy.Resource;
import net.sync.game.resource.lazy.SoundResource;
import net.sync.game.resource.xml.XmlReferenceNotCompatibleException;
import net.sync.game.resource.xml.XmlReferenceNotFoundException;
import net.sync.game.resource.xml.XmlTheme;
import net.sync.game.resource.xml.resolvers.XmlReferenceResolver;
import net.sync.game.resource.xml.resolvers.XmlSoundResolver;
import net.sync.game.util.xml.XmlParseException;
import net.sync.game.util.xml.XmlParser;

public class XmlSoundsParser extends XmlMapResourceParser<Resource<Sound>> {
    private final net.sync.game.resource.xml.XmlTheme theme;
    private XmlSoundResolver soundResolver = new XmlSoundResolver() {
        @Override
        public Resource<Sound> resolveReference(String resourceId) throws XmlReferenceNotFoundException, net.sync.game.resource.xml.XmlReferenceNotCompatibleException {
            Resource<Sound> resource = getResolvedValueOrThrow(resourceId);

            if(resource instanceof SoundResource)
                return new SoundResource((SoundResource) resource);

            throw XmlReferenceNotCompatibleException.incompatibleType(resource.getClass(), SoundResource.class);
        }

        @Override
        public Resource<Sound> resolveValue(String value) throws XmlParseException {
            return new SoundResource(getResourceFile().sibling("sounds").child(value));
        }
    };

    /**
     * Create a resource parser from its file.
     * @param resourceFile the resource file.
     */
    public XmlSoundsParser(FileHandle resourceFile, XmlTheme theme) {
        super(resourceFile);
        this.theme = theme;
    }

    @Override
    protected void checkRoot(XmlParser.Element root) throws XmlParseException {
        if(!root.getName().equals("sounds")) {
            throw new XmlParseException("Unexpected xml root element name. Expected to be 'sounds'!");
        }
    }

    @Override
    protected void checkRootChild(XmlParser.Element element) throws XmlParseException {
        if(!element.getName().equals("sound")) {
            throw new XmlParseException(String.format(
                    "Unexpected element name '%s'! Expected to be 'sound' or 'music'!", element.getName()));
        }
    }

    @Override
    protected XmlReferenceResolver<Resource<Sound>> getResolver(XmlParser.Element element) {
        return soundResolver;
    }
}
