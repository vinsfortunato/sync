/*
 * Copyright 2018 Vincenzo Fortunato
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

package net.touchmania.game.resource.xml.parsers;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import net.touchmania.game.resource.lazy.Resource;
import net.touchmania.game.resource.lazy.SoundResource;
import net.touchmania.game.resource.xml.XmlTheme;
import net.touchmania.game.resource.xml.resolvers.XmlReferenceValueResolver;
import net.touchmania.game.resource.xml.resolvers.XmlSoundResolver;
import net.touchmania.game.util.xml.XmlParseException;
import net.touchmania.game.util.xml.XmlParser;

public class XmlSoundsParser extends XmlMapResourceParser<Resource<Sound>> {
    private final XmlTheme theme;
    private XmlSoundResolver soundResolver = new XmlSoundResolver() {
        @Override
        public Resource<Sound> resolveReference(String resourceId) {
            SoundResource resource = (SoundResource) getResolvedValues().get(resourceId);
            return new SoundResource(resource);
        }

        @Override
        public Resource<Sound> resolveValue(String value) throws XmlParseException {
            if(value == null || value.isEmpty()) {
                throw new XmlParseException("Invalid sound file! File name cannot be null or empty!");
            }

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
    protected XmlReferenceValueResolver<Resource<Sound>> getResolver(XmlParser.Element element) {
        return soundResolver;
    }
}
