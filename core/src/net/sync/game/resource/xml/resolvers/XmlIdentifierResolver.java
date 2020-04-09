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

package net.sync.game.resource.xml.resolvers;

import net.sync.game.util.xml.XmlParseException;
import net.sync.game.util.xml.XmlValueResolver;

import java.util.regex.Pattern;

/**
 * Resolves id values used to bind resources. Id values can contain
 * only alphanumeric characters plus the characters '_' and '-' and can have only
 * leading/trailing whitespaces that will be removed when resolved.
 */
public class XmlIdentifierResolver implements XmlValueResolver<String> {
    private static final Pattern ID_REGEX = Pattern.compile("[0-9a-zA-Z_-]+");

    @Override
    public String resolve(String value) throws XmlParseException {
        value = value.trim();
        if (!ID_REGEX.matcher(value).matches()) {
            throw new XmlParseException("Invalid id! Identifier can contain only alphanumeric characters plus _ and -!");
        }
        return value;
    }
}
