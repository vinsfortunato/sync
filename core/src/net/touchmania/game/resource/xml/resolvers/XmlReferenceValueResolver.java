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

package net.touchmania.game.resource.xml.resolvers;

import net.touchmania.game.util.xml.XmlParseException;
import net.touchmania.game.util.xml.XmlValueResolver;

/**
 * Resolve values that can reference resources of the same type. If the value
 * isn't referencing a resource it will be parsed as a simple value (A simple value
 * is a value that doesn't reference another resource of the same type).
 * @param <T> the type of the value to resolve.
 */
public abstract class XmlReferenceValueResolver<T> implements XmlValueResolver<T> {
    @Override
    public T resolve(String value) throws XmlParseException {
        T result;
        if(isReference(value)) {
            //Resolve the reference from the resource id
            result = resolveReference(getReferenceId(value));
        } else {
            result = resolveValue(value);
        }
        return result;
    }

    /**
     * Checks if the given value is a reference of another resource.
     * @return true if the value is referencing, false otherwise.
     */
    public boolean isReference(String value) {
         return value.trim().startsWith(getResourceTypeName() + ":");
    }

    /**
     * Gets the identifier of the resource that the given value is referencing.
     * @param value the value
     * @return the referenced resource id.
     * @throws XmlParseException if the given value is not a reference or the resource identifier is invalid.
     */
    public String getReferenceId(String value) throws XmlParseException {
        if(isReference(value)) {
            //Prepare the value
            value = value.trim();
            //Value is referencing a resource. Remove resource type from reference declaration.
            String resourceId = value.substring(value.indexOf(":") + 1);
            //Returns identifier after checking its validity
            return XmlIdentifierResolver.GLOBAL_IDENTIFIER_RESOLVER.resolve(resourceId);
        } else {
            throw new XmlParseException(String.format("The value '%s' is not a reference!", value));
        }
    }

    /**
     * Returns the resource type name. It is used to check if the
     * value is referencing a resource.
     * @return the resource type name.
     */
    protected abstract String getResourceTypeName();

    /**
     * Resolve reference by finding a resource with the given id. If the
     * resource cannot be found an {@link XmlParseException} must be
     * thrown.
     * @param resourceId the resource identifier.
     * @return the referenced resource value, never null.
     * @throws XmlParseException if there's no declared resource with the given id and type.
     */
    public abstract T resolveReference(String resourceId) throws XmlParseException;

    /**
     * Parse the given value as a simple value (A value that is not referencing another resource).
     * @param value the value, can be null.
     * @return the parsed value.
     * @throws XmlParseException if the value cannot be parsed correctly.
     */
    public abstract T resolveValue(String value) throws XmlParseException;
}
