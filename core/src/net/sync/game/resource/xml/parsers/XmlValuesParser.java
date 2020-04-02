/*
 * Copyright 2020 Vincenzo Fortunato
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
