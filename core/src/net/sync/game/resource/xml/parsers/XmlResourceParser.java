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
import net.sync.game.util.xml.XmlElementParser;
import net.sync.game.util.xml.XmlParseException;
import net.sync.game.util.xml.XmlParser;

import java.io.IOException;

/**
 * Parse xml resource file.
 *
 * @param <T> the type of parsing result.
 */
public abstract class XmlResourceParser<T> implements XmlElementParser<T> {
    private FileHandle resourceFile;

    /**
     * Create a resource parser from its file.
     * @param resourceFile the resource file.
     */
    public XmlResourceParser(FileHandle resourceFile) {
        this.resourceFile = resourceFile;
    }

    /**
     * Parse the xml resource file. If the xml syntax is
     * correct the elements will be parsed in {@link #parse(XmlParser.Element)}
     * starting from the root element.
     *
     * @return the result of the parsing.
     * @throws IOException if the file cannot be parsed correctly.
     */
    public T parse() throws Exception {
        XmlParser parser = new XmlParser();
        XmlParser.Element root = parser.parse(resourceFile);
        if(root != null) {
            checkRoot(root);
            return parse(root);
        }
        throw new XmlParseException("Empty xml resource file!");
    }

    /**
     * Parse xml resource file content.
     *
     * @param root the xml root element, never null.
     * @return the result of the parsing.
     * @throws XmlParseException if the content cannot be parsed correctly.
     */
    public abstract T parse(XmlParser.Element root) throws XmlParseException;

    /**
     * Checks root element's validity.
     * @throws XmlParseException if the root element is invalid
     */
    protected abstract void checkRoot(XmlParser.Element root) throws XmlParseException;
    /**
     * The resource file.
     * @return the handle of the resource file.
     */
    public FileHandle getResourceFile() {
        return resourceFile;
    }
}
