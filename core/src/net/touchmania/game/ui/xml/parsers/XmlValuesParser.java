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

package net.touchmania.game.ui.xml.parsers;

import com.badlogic.gdx.files.FileHandle;
import net.touchmania.game.ui.xml.resolvers.*;
import net.touchmania.game.util.xml.XmlParseException;
import net.touchmania.game.util.xml.XmlParser;

public class XmlValuesParser extends XmlMapResourceParser<Object> {
    /**
     * Create a resource parser from its file.
     * @param resourceFile the resource file.
     */
    public XmlValuesParser(FileHandle resourceFile) {
        super(resourceFile);
    }

    @Override
    protected XmlReferenceValueResolver<Object> getResolver(XmlParser.Element element) {
        if(element.getName().equals("boolean")) return booleanResolver;
        if(element.getName().equals("float")) return floatResolver;
        if(element.getName().equals("int")) return integerResolver;
        if(element.getName().equals("duration")) return durationResolver;
        if(element.getName().equals("percent")) return percentResolver;
        throw new IllegalArgumentException("Unrecognised element name!");
    }

    @Override
    protected void checkRootChild(XmlParser.Element element) throws XmlParseException {
        switch(element.getName()) {
            case "boolean":
            case "float":
            case "int":
            case "duration":
            case "percent":
                break;
            default:
                throw new XmlParseException(String.format("Unexpected element name '%s'!", element.getName()));
        }
    }

    @Override
    protected void checkRoot(XmlParser.Element root) throws XmlParseException {
        if(!root.getName().equals("values")) {
            throw new XmlParseException("Unexpected xml root element name. Expected to be 'values'!");
        }
    }

    /* Resolvers */
    private XmlReferenceValueResolver<Object> booleanResolver = wrap(new XmlBooleanResolver() {
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
    });
    private XmlReferenceValueResolver<Object> floatResolver = wrap(new XmlFloatResolver() {
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
    });
    private XmlReferenceValueResolver<Object> integerResolver = wrap(new XmlIntegerResolver() {
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
    });
    private XmlReferenceValueResolver<Object> durationResolver = wrap(new XmlDurationResolver() {
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
    });
    private XmlReferenceValueResolver<Object> percentResolver = wrap(new XmlPercentResolver() {
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
    });

    private static XmlReferenceValueResolver<Object> wrap(XmlReferenceValueResolver<?> resolver) {
        return new XmlReferenceValueResolverWrapper(resolver);
    }

    private static class XmlReferenceValueResolverWrapper extends XmlReferenceValueResolver<Object> {
        private XmlReferenceValueResolver<?> resolver;

        XmlReferenceValueResolverWrapper(XmlReferenceValueResolver<?> resolver) {
            this.resolver = resolver;
        }

        @Override
        public Object resolve(String value) throws XmlParseException {
            return resolver.resolve(value);
        }

        @Override
        public boolean isReference(String value) {
            return resolver.isReference(value);
        }

        @Override
        public String getReferenceId(String value) throws XmlParseException {
            return resolver.getReferenceId(value);
        }

        @Override
        protected String getResourceTypeName() {
            return null;
        }

        @Override
        public Object resolveReference(String resourceId) throws XmlParseException {
            return resolver.resolveReference(resourceId);
        }

        @Override
        public Object resolveValue(String value) throws XmlParseException {
            return resolver.resolveValue(value);
        }
    }
}
