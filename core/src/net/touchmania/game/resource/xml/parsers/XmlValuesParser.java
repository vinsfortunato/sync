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

package net.touchmania.game.resource.xml.parsers;

import com.badlogic.gdx.files.FileHandle;
import net.touchmania.game.resource.xml.XmlTheme;
import net.touchmania.game.resource.xml.resolvers.*;
import net.touchmania.game.util.xml.XmlParseException;
import net.touchmania.game.util.xml.XmlParser;

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
    public XmlReferenceValueResolver getResolver(XmlParser.Element element) {
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
    private XmlReferenceValueResolver<Boolean> booleanResolver = new XmlBooleanResolver() {
        @Override
        public Boolean resolveReference(String resourceId) throws XmlParseException {
            Object value = getResolvedValues().get(resourceId);
            if(value != null) {
                if(value instanceof Boolean) {
                    return (Boolean) value;
                }
                throw new XmlParseException(String.format(
                        "Incompatible reference! Trying to cast '%s' to boolean!", value.getClass().getName()));
            }
            return null;
        }
    };

    private XmlReferenceValueResolver<Float> floatResolver = new XmlFloatResolver() {
        @Override
        public Float resolveReference(String resourceId) throws XmlParseException {
            Object value = getResolvedValues().get(resourceId);
            if(value != null) {
                if(value instanceof Float) {
                    return (Float) value;
                }
                throw new XmlParseException(String.format(
                        "Incompatible reference! Trying to cast '%s' to float!", value.getClass().getName()));
            }
            return null;
        }
    };

    private XmlReferenceValueResolver<Integer> integerResolver = new XmlIntegerResolver() {
        @Override
        public Integer resolveReference(String resourceId) throws XmlParseException {
            Object value = getResolvedValues().get(resourceId);
            if(value != null) {
                if(value instanceof Integer) {
                    return (Integer) value;
                }
                throw new XmlParseException(String.format(
                        "Incompatible reference! Trying to cast '%s' to integer!", value.getClass().getName()));
            }
            return null;
        }
    };

    private XmlReferenceValueResolver<Long> durationResolver = new XmlDurationResolver() {
        @Override
        public Long resolveReference(String resourceId) throws XmlParseException {
            Object value = getResolvedValues().get(resourceId);
            if(value != null) {
                if(value instanceof Long) {
                    return (Long) value;
                }
                throw new XmlParseException(String.format(
                        "Incompatible reference! Trying to cast '%s' to duration long!", value.getClass().getName()));
            }
            return null;
        }
    };

    private XmlReferenceValueResolver<Float> percentResolver = new XmlPercentResolver() {
        @Override
        public Float resolveReference(String resourceId) throws XmlParseException {
            Object value = getResolvedValues().get(resourceId);
            if(value != null) {
                if(value instanceof Float) {
                    return (Float) value;
                }
                throw new XmlParseException(String.format(
                        "Incompatible reference! Trying to cast '%s' to percent float!", value.getClass().getName()));
            }
            return null;
        }
    };
}
