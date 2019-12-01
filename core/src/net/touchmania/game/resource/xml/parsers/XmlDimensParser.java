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
import net.touchmania.game.resource.Dimension;
import net.touchmania.game.resource.xml.XmlReferenceNotFoundException;
import net.touchmania.game.resource.xml.XmlTheme;
import net.touchmania.game.resource.xml.resolvers.XmlDimensionResolver;
import net.touchmania.game.resource.xml.resolvers.XmlReferenceResolver;
import net.touchmania.game.util.xml.XmlParseException;
import net.touchmania.game.util.xml.XmlParser;

public class XmlDimensParser extends XmlMapResourceParser<Dimension> {
    private XmlDimensionResolver dimenResolver = new XmlDimensionResolver() {
        @Override
        public Dimension resolveReference(String resourceId) throws XmlReferenceNotFoundException {
            return getResolvedValueOrThrow(resourceId);
        }
    };

    /**
     * Create a resource parser from its file.
     * @param resourceFile the resource file.
     */
    public XmlDimensParser(FileHandle resourceFile, XmlTheme theme) {
        super(resourceFile);
    }

    @Override
    protected void checkRoot(XmlParser.Element root) throws XmlParseException {
        if(!root.getName().equals("dimens")) {
            throw new XmlParseException("Unexpected xml root element name. Expected to be 'dimens'!");
        }
    }

    @Override
    protected void checkRootChild(XmlParser.Element element) throws XmlParseException {
        if(!element.getName().equals("dimen")) {
            throw new XmlParseException(String.format("Unexpected element name '%s'! Expected to be 'dimen'!", element.getName()));
        }
    }

    @Override
    protected XmlReferenceResolver<Dimension> getResolver(XmlParser.Element element) {
        return dimenResolver;
    }
}
