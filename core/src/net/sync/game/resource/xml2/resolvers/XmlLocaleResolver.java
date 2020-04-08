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

package net.sync.game.resource.xml2.resolvers;

import net.sync.game.util.xml.XmlParseException;
import net.sync.game.util.xml.XmlValueResolver;

import java.util.Locale;

public class XmlLocaleResolver implements XmlValueResolver<Locale> {
    public static final XmlLocaleResolver GLOBAL_LOCALE_RESOLVER = new XmlLocaleResolver();

    @Override
    public Locale resolve(String value) throws XmlParseException {
        //Prepare the value for parsing by removing leading/trailing spaces
        value = value.trim();

        if(value.contains("_")) {
            String parts[] = value.split("_");
            String lang = parts[0];
            String country = parts[1];
            return new Locale(lang, country);
        }

        return new Locale(value);
    }
}
