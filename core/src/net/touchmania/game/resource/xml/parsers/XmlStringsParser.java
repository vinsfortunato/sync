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
import net.touchmania.game.resource.xml.XmlTheme;
import net.touchmania.game.resource.xml.exception.XmlReferenceNotFoundException;
import net.touchmania.game.resource.xml.resolvers.XmlReferenceResolver;
import net.touchmania.game.resource.xml.resolvers.XmlStringResolver;
import net.touchmania.game.util.xml.XmlParseException;
import net.touchmania.game.util.xml.XmlParser;

public class XmlStringsParser extends XmlMapResourceParser<String>{
    private XmlStringResolver stringResolver = new XmlStringResolver() {
        @Override
        public String resolveReference(String resourceId) throws XmlReferenceNotFoundException {
            return getResolvedValueOrThrow(resourceId);
        }
    };

    /**
     * Create a resource parser from its file.
     * @param resourceFile the resource file.
     */
    public XmlStringsParser(FileHandle resourceFile, XmlTheme theme) {
        super(resourceFile);
    }

    @Override
    protected XmlReferenceResolver<String> getResolver(XmlParser.Element element) {
        return stringResolver;
    }

    @Override
    protected void checkRootChild(XmlParser.Element element) throws XmlParseException {
        if(!element.getName().equals("string")) {
            throw new XmlParseException(String.format("Unexpected element name '%s'! Expected to be 'string'!", element.getName()));
        }
    }

    @Override
    protected void checkRoot(XmlParser.Element root) throws XmlParseException {
        if(!root.getName().equals("strings")) {
            throw new XmlParseException("Unexpected xml root element name. Expected to be 'strings'!");
        }
    }
}
