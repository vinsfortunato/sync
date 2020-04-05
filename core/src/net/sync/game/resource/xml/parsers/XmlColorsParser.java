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

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import net.sync.game.resource.xml.XmlReferenceNotFoundException;
import net.sync.game.resource.xml.XmlTheme;
import net.sync.game.resource.xml.resolvers.XmlColorResolver;
import net.sync.game.resource.xml.resolvers.XmlReferenceResolver;
import net.sync.game.util.xml.XmlParseException;
import net.sync.game.util.xml.XmlParser;

public class XmlColorsParser extends XmlMapResourceParser<Color> {
    private XmlColorResolver colorResolver = new XmlColorResolver() {
        @Override
        public Color resolveReference(String resourceId) throws XmlReferenceNotFoundException {
            Color color = getResolvedValueOrThrow(resourceId);
            return new Color(color.r, color.g, color.b, color.a);
        }
    };

    /**
     * Create a resource parser from its file.
     * @param resourceFile the resource file.
     */
    public XmlColorsParser(FileHandle resourceFile, XmlTheme theme) {
        super(resourceFile);
    }

    @Override
    protected void checkRoot(XmlParser.Element root) throws XmlParseException {
        if(!root.getName().equals("colors")) {
            throw new XmlParseException("Unexpected xml root element name. Expected to be 'colors'!");
        }
    }

    @Override
    protected void checkRootChild(XmlParser.Element element) throws XmlParseException {
        if(!element.getName().equals("color")) {
            throw new XmlParseException(String.format("Unexpected element name '%s'! Expected to be 'color'!", element.getName()));
        }
    }

    @Override
    protected XmlReferenceResolver<Color> getResolver(XmlParser.Element element) {
        return colorResolver;
    }
}
