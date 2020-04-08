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
import net.sync.game.resource.xml2.XmlTheme;
import net.sync.game.resource.xml2.resolvers.XmlBooleanResolver;
import net.sync.game.resource.xml2.resolvers.XmlLocaleResolver;
import net.sync.game.util.xml.XmlParseException;
import net.sync.game.util.xml.XmlParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class XmlLangsParser extends XmlResourceParser<List<Locale>> {
    /**
     * Create a resource parser from its file.
     *
     * @param resourceFile the resource file.
     */
    public XmlLangsParser(FileHandle resourceFile, XmlTheme theme) {
        super(resourceFile);
    }

    @Override
    public List<Locale> parse(XmlParser.Element root) throws XmlParseException {
        List<Locale> langs = new ArrayList<>();

        for(int i = 0; i < root.getChildCount(); i++) {
            XmlParser.Element element = root.getChild(i);

            //Check root child name
            if(!element.getName().equals("lang")) {
                throw new XmlParseException(
                        String.format("Unexpected element name '%s'! Expected to be 'lang'!", element.getName()));
            }

            //Parse lang locale
            Locale lang = XmlLocaleResolver.GLOBAL_LOCALE_RESOLVER.resolve(element.getAttribute("locale"));

            //Check if lang is default
            boolean isDefault =
                    element.hasAttribute("default") &&
                            XmlBooleanResolver.GLOBAL_BOOLEAN_RESOLVER.resolve(element.getAttribute("default"));

            //Check for duplicates
            if(langs.contains(lang)) {
                throw new XmlParseException(
                        String.format("A language with the same locale '%s' has been already declared!", element.getAttribute("locale")));
            }

            //Add lang to the list of theme supported languages
            if(isDefault) {
                //Put default lang on list first position
                langs.add(0, lang);
            } else {
                langs.add(lang);
            }
        }

        //Returning null means that the theme doesn't support other languages than the ones defined into the fallback theme
        return langs.isEmpty() ? null : langs;
    }

    @Override
    protected void checkRoot(XmlParser.Element root) throws XmlParseException {
        if(!root.getName().equals("langs")){
            throw new XmlParseException("Unexpected xml root element name. Expected to be 'langs'");
        }
    }
}
