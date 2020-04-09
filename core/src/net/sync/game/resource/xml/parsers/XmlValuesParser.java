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
import net.sync.game.resource.MapTheme;
import net.sync.game.resource.xml.resolvers.*;
import net.sync.game.util.xml.XmlElement;
import net.sync.game.util.xml.XmlParseException;
import net.sync.game.util.xml.XmlParser;

public class XmlValuesParser extends XmlMapResourceParser<Object> {
    private static final String RESOURCE_ROOT_NAME = "values";
    private static final String BOOLEAN_RESOURCE_TYPE_NAME = "boolean";
    private static final String FLOAT_RESOURCE_TYPE_NAME = "float";
    private static final String INTEGER_RESOURCE_TYPE_NAME = "int";
    private static final String DURATION_RESOURCE_TYPE_NAME = "duration";
    private static final String PERCENT_RESOURCE_TYPE_NAME = "percent";

    /**
     * Create a resource parser from its file.
     * @param resourceFile the resource file.
     */
    public XmlValuesParser(XmlParser parser, FileHandle resourceFile, MapTheme theme) {
        super(parser, resourceFile, RESOURCE_ROOT_NAME);
    }

    @Override
    protected void validateRootChild(XmlElement element) {
        switch(element.getName()) {
            case BOOLEAN_RESOURCE_TYPE_NAME:
            case FLOAT_RESOURCE_TYPE_NAME:
            case INTEGER_RESOURCE_TYPE_NAME:
            case DURATION_RESOURCE_TYPE_NAME:
            case PERCENT_RESOURCE_TYPE_NAME:
                return;
            default:
                throw new XmlParseException(String.format("Unexpected element name '%s'!", element.getName()));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public XmlReferenceResolver getResolver(XmlElement element) {
        switch (element.getName()) {
            case BOOLEAN_RESOURCE_TYPE_NAME:
                return booleanResolver;
            case FLOAT_RESOURCE_TYPE_NAME:
                return floatResolver;
            case INTEGER_RESOURCE_TYPE_NAME:
                return integerResolver;
            case DURATION_RESOURCE_TYPE_NAME:
                return durationResolver;
            case PERCENT_RESOURCE_TYPE_NAME:
                return percentResolver;
            default:
                throw new XmlParseException(String.format("Unexpected element name '%s'!", element.getName()));
        }
    }

    /* Resolvers */

    private XmlReferenceResolver<Boolean> booleanResolver = XmlReferenceResolver.from(
            new XmlBooleanResolver(),
            resourceId -> {
                Object value = getResolvedValueOrThrow(resourceId);
                if(value instanceof Boolean)
                    return (Boolean) value;
                throw new XmlReferenceNotCompatibleException(value.getClass(), Boolean.class);
            },
            BOOLEAN_RESOURCE_TYPE_NAME);

    private XmlReferenceResolver<Float> floatResolver = XmlReferenceResolver.from(
            new XmlFloatResolver(),
            resourceId -> {
                Object value = getResolvedValueOrThrow(resourceId);
                if(value instanceof Float)
                    return (Float) value;
                throw new XmlReferenceNotCompatibleException(value.getClass(), Float.class);
            },
            FLOAT_RESOURCE_TYPE_NAME);

    private XmlReferenceResolver<Integer> integerResolver = XmlReferenceResolver.from(
            new XmlIntegerResolver(),
            resourceId -> {
                Object value = getResolvedValueOrThrow(resourceId);
                if(value instanceof Integer)
                    return (Integer) value;
                throw new XmlReferenceNotCompatibleException(value.getClass(), Integer.class);
            },
            INTEGER_RESOURCE_TYPE_NAME);

    private XmlReferenceResolver<Long> durationResolver = XmlReferenceResolver.from(
            new XmlDurationResolver(),
            resourceId -> {
                Object value = getResolvedValueOrThrow(resourceId);
                if(value instanceof Long)
                    return (Long) value;
                throw new XmlReferenceNotCompatibleException(value.getClass(), Long.class);
            },
            DURATION_RESOURCE_TYPE_NAME);

    private XmlReferenceResolver<Float> percentResolver = XmlReferenceResolver.from(
            new XmlPercentResolver(),
            resourceId -> {
                Object value = getResolvedValueOrThrow(resourceId);
                if(value instanceof Float)
                    return (Float) value;
                throw new XmlReferenceNotCompatibleException(value.getClass(), Float.class);
            },
            PERCENT_RESOURCE_TYPE_NAME);
}
