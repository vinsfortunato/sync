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

package net.sync.game.util.xml;

import com.badlogic.gdx.files.FileHandle;

import java.io.InputStream;
import java.io.Reader;

/**
 * An XML document parser. Provides a DOM representation of an XML document
 * by parsing it from different different possible sources.
 */
public interface XmlParser {
    /**
     * Parses the given XML document from a string.
     * @param xml the xml string to parse.
     * @return the parsed XML root element.
     * @throws XmlParseException if the document cannot be parsed.
     */
    XmlElement parse(String xml);

    /**
     * Parses the given XML document from a stream.
     * @param stream the xml stream to parse.
     * @return the parsed XML root element.
     * @throws XmlParseException if the document cannot be parsed.
     */
    XmlElement parse(InputStream stream);

    /**
     * Parses the given XML document from a reader.
     * @param reader the xml reader to parse.
     * @return the parsed XML root element.
     * @throws XmlParseException if the document cannot be parsed.
     */
    XmlElement parse(Reader reader);

    /**
     * Parses the given XML document from a file handle.
     * @param file the xml file to parse.
     * @return the parsed XML root element.
     * @throws XmlParseException if the document cannot be parsed.
     */
    XmlElement parse(FileHandle file);
}
