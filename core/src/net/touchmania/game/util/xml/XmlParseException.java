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

package net.touchmania.game.util.xml;

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

    public XmlParseException(FileHandle file, XmlParser.Element element) {
        this(getLineMessage(file, element));
    }

    public XmlParseException(String message, FileHandle file, XmlParser.Element element) {
        this(getLineMessage(file, element) + " " + message);
    }

    public XmlParseException(String message, Throwable cause, FileHandle file, XmlParser.Element element) {
        this(getLineMessage(file, element) + " " + message, cause);
    }

    public XmlParseException(Throwable cause, FileHandle file, XmlParser.Element element) {
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
