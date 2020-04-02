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
