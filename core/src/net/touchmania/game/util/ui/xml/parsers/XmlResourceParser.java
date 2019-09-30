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

package net.touchmania.game.util.ui.xml.parsers;

import com.badlogic.gdx.files.FileHandle;
import net.touchmania.game.util.xml.XmlElementParser;
import net.touchmania.game.util.xml.XmlParseException;
import net.touchmania.game.util.xml.XmlParser;

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
