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
import net.sync.game.util.xml.XmlElement;
import net.sync.game.util.xml.XmlElementParser;
import net.sync.game.util.xml.XmlParseException;
import net.sync.game.util.xml.XmlParser;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An XML resource file parser.
 * @param <T> the type of parsing result.
 */
public abstract class XmlResourceParser<T> implements XmlElementParser<T> {
    private XmlParser parser;
    private FileHandle file;

    /**
     * Creates a resource parser from its file.
     * @param file the resource file.
     */
    public XmlResourceParser(XmlParser parser, FileHandle file) {
        this.parser = checkNotNull(parser);
        this.file = checkNotNull(file);
    }

    /**
     * Parses the xml resource file. Checks if the root element is valid
     * by calling {@link #validateRoot(XmlElement)}, if valid then parse
     * it by calling {@link #parse(XmlElement)}.
     * @return the result of the parsing.
     * @throws XmlParseException if the resource file cannot be parsed correctly
     */
    public T parse() {
        XmlElement root = parser.parse(file);
        if(root != null) {
            validateRoot(root);
            return parse(root);
        }
        throw new XmlParseException("Empty xml resource file!");
    }

    /**
     * Parses the XML document.
     * @param root the xml root element, never null.
     * @return the result of the parsing.
     * @throws XmlParseException if the document cannot be parsed correctly.
     */
    public abstract T parse(XmlElement root);

    /**
     * Validates root element.
     * @throws XmlParseException if the root element is invalid
     */
    protected abstract void validateRoot(XmlElement root);

    /**
     * The xml document parser.
     * @return the xml parser.
     */
    public XmlParser getParser() {
        return parser;
    }

    /**
     * The resource file.
     * @return the handle of the resource file.
     */
    public FileHandle getFile() {
        return file;
    }
}
