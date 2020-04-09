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
import net.sync.game.resource.MapTheme;
import net.sync.game.util.xml.XmlElement;
import net.sync.game.util.xml.XmlParseException;
import net.sync.game.util.xml.XmlParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static net.sync.game.resource.xml.resolvers.XmlGlobalResolvers.GLOBAL_LOCALE_RESOLVER;
import static net.sync.game.resource.xml.resolvers.XmlGlobalResolvers.GLOBAL_BOOLEAN_RESOLVER;

public class XmlLangsParser extends XmlResourceParser<List<Locale>> {
    private static final String RESOURCE_ROOT_NAME = "langs";
    private static final String RESOURCE_TYPE_NAME = "lang";

    /**
     * Creates a resource parser from its file.
     * @param file the resource file.
     */
    public XmlLangsParser(XmlParser parser, FileHandle file, MapTheme theme) {
        super(parser, file);
    }

    @Override
    public List<Locale> parse(XmlElement root) {
        List<Locale> langs = new ArrayList<>();

        for(XmlElement element : root.getChildren()) {
            //Check root child name
            if(!element.getName().equals(RESOURCE_TYPE_NAME)) {
                throw new XmlParseException(String.format(
                        "Unexpected element name '%s'! Expected to be '%s'!", element.getName(), RESOURCE_TYPE_NAME));
            }

            //Parse lang locale
            Locale lang = GLOBAL_LOCALE_RESOLVER.resolve(element.getAttribute("locale"));

            //Check if lang is default
            boolean isDefault = GLOBAL_BOOLEAN_RESOLVER.resolve(element.getAttribute("default", "false"));

            //Check for duplicates
            if(langs.contains(lang)) {
                throw new XmlParseException(String.format(
                        "A language with the same locale '%s' has been already declared!", element.getAttribute("locale")));
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
    protected void validateRoot(XmlElement root) {
        if(!root.getName().equals(RESOURCE_ROOT_NAME)) {
            throw new XmlParseException(String.format(
                    "Unexpected xml root element name '%s'. Expected to be '%s'!", root.getName(), RESOURCE_ROOT_NAME));
        }
    }
}
