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
import net.sync.game.resource.xml.XmlStyle;
import net.sync.game.resource.xml.XmlTheme;
import net.sync.game.util.xml.XmlParseException;
import net.sync.game.util.xml.XmlParser;

import java.util.HashMap;
import java.util.Map;

public class XmlStyleParser extends XmlResourceParser<XmlStyle> {
    /**
     * Create a resource parser from a its file.
     *
     * @param resourceFile the resource file.
     */
    public XmlStyleParser(FileHandle resourceFile, XmlTheme theme) {
        super(resourceFile);
    }

    @Override
    public XmlStyle parse(XmlParser.Element root) throws XmlParseException {
        //Initialize map with an appropriate size
        Map<String, String> attributes = new HashMap<>(root.getChildCount());

        for(int i = 0; i < root.getChildCount(); i++) {
            XmlParser.Element element = root.getChild(i);

            if(!element.getName().equals("attribute")) {
                throw new XmlParseException(String.format("Unexpected element name '%s'! Expected to be 'attribute'!", element.getName()));
            }

            //Get the attribute name
            String name = element.getAttribute("name");
            if(name == null || name.isEmpty()) {
                throw new XmlParseException("Required attribute 'name' empty or not found in style attribute definition!");
            }

            //Fix the attribute name
            name = name.trim();

            //Check for illegal attribute name
            if(name.equals("style")) {
                throw new XmlParseException("Attribute with name 'style' is not allowed in a style resource!");
            }

            //Check for duplicates
            if(attributes.containsKey(name)) {
                throw new XmlParseException(String.format("Duplicated attribute name '%s'", name));
            }

            //Get the attribute value and bind it to the attribute name
            attributes.put(name, element.hasText() ? element.getText() : "");
        }

        XmlStyle style = new XmlStyle();
        style.setAttributes(attributes);
        return style;
    }

    @Override
    protected void checkRoot(XmlParser.Element root) throws XmlParseException {
        if(!root.getName().equals("style")) {
            throw new XmlParseException("Unexpected xml root element name. Expected to be 'style'!");
        }
    }
}
