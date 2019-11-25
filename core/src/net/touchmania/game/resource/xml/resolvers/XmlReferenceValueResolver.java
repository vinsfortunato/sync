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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Resolve values that can reference resources. If the value
 * isn't referencing a resource it will be parsed as a simple value (A simple value
 * is a value that doesn't reference another resource).
 * @param <T> the type of the value to resolve.
 */
public abstract class XmlReferenceValueResolver<T> implements XmlValueResolver<T> {
    public static Pattern REFERENCE_REGEX = Pattern.compile("[0-9a-zA-Z_-]+:[0-9a-zA-Z_-]+");

    @Override
    public T resolve(String value) throws XmlParseException {
        T result;
        Matcher matcher = REFERENCE_REGEX.matcher(value.trim());
        if(matcher.matches()) {
            //Reference value
            String referenceType = matcher.group(1);
            String referenceId = matcher.group(2);
            if(checkReferenceType(referenceType)) {
                //Resolve the reference from the reference id
                result = resolveReference(referenceId);
            } else {
                throw new XmlParseException("Incompatible reference type!");
            }
        } else {
            //Simple value
            result = resolveValue(value);
        }
        return result;
    }

    public boolean isReference(String value) {
        return REFERENCE_REGEX.matcher(value.trim()).matches();
    }

    public String getReferenceId(String value) {
        Matcher matcher = REFERENCE_REGEX.matcher(value.trim());
        if(matcher.matches()) {
            return matcher.group(2);
        }
        return null;
    }

    /**
     * Checks if the given reference type is compatible.
     * <p>Base implementation will check if the reference type is the same of {@link #getResourceTypeName()}. </p>
     * @return true if the reference type is compatible, false otherwise.
     */
    public boolean checkReferenceType(String type) {
         return type.equals(getResourceTypeName());
    }

    /**
     * Returns the resource type name.
     * @return the resource type name.
     */
    protected abstract String getResourceTypeName();

    /**
     * Resolve reference by finding a resource with the given id. If the
     * resource cannot be found an {@link XmlParseException} must be
     * thrown.
     * @param resourceId the resource identifier.
     * @return the referenced resource value, never null.
     * @throws XmlParseException if there's no declared resource with the given id and type or the
     * referenced resource is incompatible (e.g. int resource referencing string resource).
     */
    public abstract T resolveReference(String resourceId) throws XmlParseException;

    /**
     * Parse the given value as a simple value (A value that is not referencing another resource).
     * @param value the value, can be null (e.g. when using self closing tags).
     * @return the parsed value.
     * @throws XmlParseException if the value cannot be parsed correctly.
     */
    public abstract T resolveValue(String value) throws XmlParseException;
}
