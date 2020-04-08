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
import com.badlogic.gdx.utils.SerializationException;
import com.badlogic.gdx.utils.XmlReader;

import java.io.InputStream;
import java.io.Reader;

/**
 * An implementation of {@link XmlParser} that uses {@link XmlReader} from libGDX.
 */
public class GdxXmlParser implements XmlParser {
    @Override
    public XmlElement parse(String xml) {
        try {
            XmlReader parser = new XmlReader();
            return new GdxXmlElement(parser.parse(xml));
        } catch(SerializationException e) {
            throw new XmlSerializeException("Cannot parse xml", e);
        }
    }

    @Override
    public XmlElement parse(InputStream stream) {
        try {
            XmlReader parser = new XmlReader();
            return new GdxXmlElement(parser.parse(stream));
        } catch(SerializationException e) {
            throw new XmlSerializeException("Cannot parse xml", e);
        }
    }

    @Override
    public XmlElement parse(Reader reader) {
        try {
            XmlReader parser = new XmlReader();
            return new GdxXmlElement(parser.parse(reader));
        } catch(SerializationException e) {
            throw new XmlSerializeException("Cannot parse xml", e);
        }
    }

    @Override
    public XmlElement parse(FileHandle file) {
        try {
            XmlReader parser = new XmlReader();
            return new GdxXmlElement(parser.parse(file));
        } catch(SerializationException e) {
            throw new XmlSerializeException("Cannot parse xml", e);
        }
    }
}
