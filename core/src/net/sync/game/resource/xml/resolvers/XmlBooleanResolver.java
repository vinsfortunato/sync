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

import net.sync.game.resource.ResourceProvider;
import net.sync.game.resource.xml.XmlReferenceNotFoundException;
import net.sync.game.util.xml.XmlParseException;
import net.sync.game.util.xml.XmlValueResolver;

public abstract class XmlBooleanResolver extends XmlReferenceResolver<Boolean> {
    /** Global Boolean primitive resolver. */
    public static XmlValueResolver<Boolean> GLOBAL_BOOLEAN_RESOLVER = new XmlValueResolver<Boolean>() {
        @Override
        public Boolean resolve(String value) throws XmlParseException {
            value = value.trim();
            if(value.equalsIgnoreCase("true")) {
                return true;
            } else if(value.equalsIgnoreCase("false")) {
                return false;
            }

            throw new XmlParseException(String.format("Invalid boolean value '%s'! Must be 'true' or 'false'!", value));
        }
    };

    @Override
    protected String getResourceTypeName() {
        return "boolean";
    }

    @Override
    public Boolean resolveValue(String value) throws XmlParseException {
        value = value.trim();
        if(value.equalsIgnoreCase("true")) {
            return true;
        } else if(value.equalsIgnoreCase("false")) {
            return false;
        }

        throw new XmlParseException(String.format("Invalid boolean value '%s'! Must be 'true' or 'false'!", value));
    }

    public static XmlBooleanResolver from(final ResourceProvider provider) {
        return new XmlBooleanResolver() {
            @Override
            public Boolean resolveReference(String resourceId) throws XmlReferenceNotFoundException {
                Boolean bool = provider.getBoolean(resourceId);

                if(bool == null)
                    throw new XmlReferenceNotFoundException(
                            String.format("Cannot resolve reference with id '%s'", resourceId));

                return bool;
            }
        };
    }
}
