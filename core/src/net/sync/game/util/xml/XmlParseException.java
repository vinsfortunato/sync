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

import java.io.IOException;

public class XmlParseException extends IOException {
    public XmlParseException() {}

    public XmlParseException(String message) {
        super(message);
    }

    public XmlParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public XmlParseException(Throwable cause) {
        super(cause);
    }

    public XmlParseException(FileHandle file, net.sync.game.util.xml.XmlParser.Element element) {
        this(getLineMessage(file, element));
    }

    public XmlParseException(String message, FileHandle file, net.sync.game.util.xml.XmlParser.Element element) {
        this(getLineMessage(file, element) + " " + message);
    }

    public XmlParseException(String message, Throwable cause, FileHandle file, net.sync.game.util.xml.XmlParser.Element element) {
        this(getLineMessage(file, element) + " " + message, cause);
    }

    public XmlParseException(Throwable cause, FileHandle file, net.sync.game.util.xml.XmlParser.Element element) {
        this(getLineMessage(file, element), cause);
    }

    private static String getLineMessage(FileHandle file, XmlParser.Element element) {
        StringBuilder builder = new StringBuilder();
        builder.append("Cannot parse xml element ");
        builder.append(element.getName());
        builder.append(" at line ");
        builder.append(element.getLineNumber());
        builder.append(" in file '");
        builder.append(file.path());
        builder.append("'.");
        return builder.toString();
    }
}
