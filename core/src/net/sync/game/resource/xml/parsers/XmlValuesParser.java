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
import net.sync.game.resource.xml.XmlReferenceNotCompatibleException;
import net.sync.game.resource.xml.XmlReferenceNotFoundException;
import net.sync.game.resource.xml.XmlTheme;
import net.sync.game.resource.xml.resolvers.XmlPercentResolver;
import net.sync.game.util.xml.XmlParseException;
import net.sync.game.util.xml.XmlParser;

public class XmlValuesParser extends XmlMapResourceParser<Object> {
    /**
     * Create a resource parser from its file.
     * @param resourceFile the resource file.
     */
    public XmlValuesParser(FileHandle resourceFile, XmlTheme theme) {
        super(resourceFile);
    }

    @Override
    protected void checkRoot(XmlParser.Element root) throws XmlParseException {
        if(!root.getName().equals("values")) {
            throw new XmlParseException("Unexpected xml root element name. Expected to be 'values'!");
        }
    }

    @Override
    protected void checkRootChild(XmlParser.Element element) throws XmlParseException {
        switch(element.getName()) {
            case "boolean":
            case "float":
            case "int":
            case "duration":
            case "percent":
                return;
            default:
                throw new XmlParseException(String.format("Unexpected element name '%s'!", element.getName()));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public net.sync.game.resource.xml.resolvers.XmlReferenceResolver getResolver(XmlParser.Element element) {
        switch (element.getName()) {
            case "boolean":
                return booleanResolver;
            case "float":
                return floatResolver;
            case "int":
                return integerResolver;
            case "duration":
                return durationResolver;
            case "percent":
                return percentResolver;
            default:
                throw new IllegalArgumentException("Unexpected element name!");
        }
    }

    /* Resolvers */
    private net.sync.game.resource.xml.resolvers.XmlReferenceResolver<Boolean> booleanResolver = new net.sync.game.resource.xml.resolvers.XmlBooleanResolver() {
        @Override
        public Boolean resolveReference(String resourceId) throws net.sync.game.resource.xml.XmlReferenceNotFoundException, net.sync.game.resource.xml.XmlReferenceNotCompatibleException {
            Object value = getResolvedValueOrThrow(resourceId);

            if(value instanceof Boolean)
                return (Boolean) value;

            throw net.sync.game.resource.xml.XmlReferenceNotCompatibleException.incompatibleType(value.getClass(), Boolean.class);
        }
    };

    private net.sync.game.resource.xml.resolvers.XmlReferenceResolver<Float> floatResolver = new net.sync.game.resource.xml.resolvers.XmlFloatResolver() {
        @Override
        public Float resolveReference(String resourceId) throws net.sync.game.resource.xml.XmlReferenceNotFoundException, net.sync.game.resource.xml.XmlReferenceNotCompatibleException {
            Object value = getResolvedValueOrThrow(resourceId);

            if(value instanceof Float)
                return (Float) value;

            throw net.sync.game.resource.xml.XmlReferenceNotCompatibleException.incompatibleType(value.getClass(), Float.class);
        }
    };

    private net.sync.game.resource.xml.resolvers.XmlReferenceResolver<Integer> integerResolver = new net.sync.game.resource.xml.resolvers.XmlIntegerResolver() {
        @Override
        public Integer resolveReference(String resourceId) throws net.sync.game.resource.xml.XmlReferenceNotFoundException, net.sync.game.resource.xml.XmlReferenceNotCompatibleException {
            Object value = getResolvedValueOrThrow(resourceId);

            if(value instanceof Integer)
                return (Integer) value;

            throw net.sync.game.resource.xml.XmlReferenceNotCompatibleException.incompatibleType(value.getClass(), Integer.class);
        }
    };

    private net.sync.game.resource.xml.resolvers.XmlReferenceResolver<Long> durationResolver = new net.sync.game.resource.xml.resolvers.XmlDurationResolver() {
        @Override
        public Long resolveReference(String resourceId) throws net.sync.game.resource.xml.XmlReferenceNotFoundException, net.sync.game.resource.xml.XmlReferenceNotCompatibleException {
            Object value = getResolvedValueOrThrow(resourceId);

            if(value instanceof Long)
                return (Long) value;

            throw net.sync.game.resource.xml.XmlReferenceNotCompatibleException.incompatibleType(value.getClass(), Long.class);
        }
    };

    private net.sync.game.resource.xml.resolvers.XmlReferenceResolver<Float> percentResolver = new XmlPercentResolver() {
        @Override
        public Float resolveReference(String resourceId) throws XmlReferenceNotFoundException, net.sync.game.resource.xml.XmlReferenceNotCompatibleException {
            Object value = getResolvedValueOrThrow(resourceId);

            if(value instanceof Float)
                return (Float) value;

            throw XmlReferenceNotCompatibleException.incompatibleType(value.getClass(), Float.class);
        }
    };
}
