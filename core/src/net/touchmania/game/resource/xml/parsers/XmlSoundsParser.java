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

import com.badlogic.gdx.files.FileHandle;
import net.touchmania.game.resource.xml.XmlSoundLoader;
import net.touchmania.game.resource.xml.XmlTheme;
import net.touchmania.game.resource.xml.resolvers.XmlReferenceValueResolver;
import net.touchmania.game.util.xml.XmlParseException;
import net.touchmania.game.util.xml.XmlParser;

public class XmlSoundsParser extends XmlMapResourceParser<XmlSoundLoader> {
    private XmlSoundLoaderResolver soundLoaderResolver;

    /**
     * Create a resource parser from its file.
     * @param resourceFile the resource file.
     */
    public XmlSoundsParser(FileHandle resourceFile, XmlTheme theme) {
        super(resourceFile);

        //Create a new instance for every reference resolver.
        this.soundLoaderResolver = new XmlSoundLoaderResolver();
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
    protected XmlReferenceValueResolver<XmlSoundLoader> getResolver(XmlParser.Element element) {
        return soundLoaderResolver;
    }

    private class XmlSoundLoaderResolver extends XmlReferenceValueResolver<XmlSoundLoader> {
        @Override
        protected String getResourceTypeName() {
            return "sound";
        }

        @Override
        public XmlSoundLoader resolveReference(String resourceId) {
            XmlSoundLoader loader = getResolvedValues().get(resourceId);
            return new XmlSoundLoader(loader);
        }

        @Override
        public XmlSoundLoader resolveValue(String value) throws XmlParseException {
            if(value == null || value.isEmpty()) {
                throw new XmlParseException("Invalid sound file! File name cannot be null or empty!");
            }

            return new XmlSoundLoader(getResourceFile().sibling("sounds").child(value));
        }
    }
}
