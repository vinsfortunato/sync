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

package net.sync.game.resource.xml2.parsers;

import com.badlogic.gdx.files.FileHandle;
import net.sync.game.resource.xml2.XmlReferenceNotFoundException;
import net.sync.game.resource.xml2.XmlTheme;
import net.sync.game.resource.xml2.resolvers.XmlReferenceResolver;
import net.sync.game.resource.xml2.resolvers.XmlStringResolver;
import net.sync.game.util.xml.XmlParseException;
import net.sync.game.util.xml.XmlParser;

public class XmlStringsParser extends XmlMapResourceParser<String>{
    private net.sync.game.resource.xml2.resolvers.XmlStringResolver stringResolver = new XmlStringResolver() {
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
