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

package net.sync.game.resource.xml.deserializers;

import com.badlogic.gdx.files.FileHandle;
import net.sync.game.resource.MapTheme;
import net.sync.game.resource.xml.resolvers.XmlReferenceResolver;
import net.sync.game.util.xml.XmlDeserializeException;
import net.sync.game.util.xml.XmlElement;
import net.sync.game.util.xml.XmlParser;

public class XmlStringsDeserializer extends XmlMapResourceDeserializer<String> {
    private static final String RESOURCE_ROOT_NAME = "strings";
    private static final String RESOURCE_TYPE_NAME = "string";

    /**
     * Creates a strings resource deserializer.
     * @param parser the XML parser.
     * @param file the resource file.
     * @param theme the theme.
     */
    public XmlStringsDeserializer(XmlParser parser, FileHandle file, MapTheme theme) {
        super(parser, file, RESOURCE_ROOT_NAME);
    }

    @Override
    protected void validateRootChild(XmlElement element) {
        if(!element.getName().equals(RESOURCE_TYPE_NAME)) {
            throw new XmlDeserializeException(String.format(
                    "Unexpected element name '%s'! Expected to be '%s'!", element.getName(), RESOURCE_TYPE_NAME));
        }
    }

    @Override
    protected XmlReferenceResolver<String> getResolver(XmlElement element) {
        return stringResolver;
    }

    /* Resolvers */

    private XmlReferenceResolver<String> stringResolver = XmlReferenceResolver.from(
            value -> value,
            this::getResolvedValueOrThrow,
            RESOURCE_TYPE_NAME);
}
