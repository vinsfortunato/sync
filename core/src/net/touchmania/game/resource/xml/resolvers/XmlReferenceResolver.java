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

import com.google.common.base.Preconditions;
import net.touchmania.game.resource.xml.XmlReferenceNotCompatibleException;
import net.touchmania.game.resource.xml.XmlReferenceNotFoundException;
import net.touchmania.game.util.xml.XmlParseException;
import net.touchmania.game.util.xml.XmlValueResolver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Resolve values that can reference other resources.
 * <p> If the value isn't referencing a resource it will be parsed as a simple value
 * (A simple value is a value that doesn't reference another resource).</p>
 * @param <T> the type of the value to resolve.
 */
public abstract class XmlReferenceResolver<T> implements XmlValueResolver<T> {
    public final static Pattern REFERENCE_REGEX = Pattern.compile("([0-9a-zA-Z_-]+):([0-9a-zA-Z_-]+)");

    @Override
    public T resolve(String value) throws XmlParseException {
        T result;
        Matcher matcher = REFERENCE_REGEX.matcher(value.trim());
        if(matcher.matches()) {
            //Reference value
            String referenceType = matcher.group(1);
            String referenceId = matcher.group(2);

            //Check reference type first
            checkReferenceType(referenceType);

            //Resolve the reference from the reference id

            result = resolveReference(referenceId);
            Preconditions.checkNotNull(result, "Unexpected null value! Resolved referenced resource cannot be null.");
        } else {
            //Simple value
            result = resolveValue(value);
            Preconditions.checkNotNull(result, "Unexpected null value! Resolved simple value cannot be null!");
        }
        return result;
    }

    /**
     * Checks if the given reference type is compatible.
     * <p>Base implementation will check if the reference type is the same of {@link #getResourceTypeName()}. </p>
     * @param type the reference type, not null.
     * @throws XmlReferenceNotCompatibleException if the given reference type is not compatible.
     */
    public void checkReferenceType(String type) throws XmlReferenceNotCompatibleException {
        String resType = getResourceTypeName();
         if(!type.equals(resType)) {
             throw new XmlReferenceNotCompatibleException(
                     String.format("Uncompatible reference type. Expected to be %s", resType));
         }
    }

    /**
     * Returns the resource type name.
     * @return the resource type name.
     */
    protected abstract String getResourceTypeName();

    /**
     * Resolve reference by finding a resource with the given id. If the
     * resource cannot be found an {@link XmlParseException} must be
     * thrown. It must throw {@link XmlReferenceNotFoundException} if the referenced resource
     * has not been declared before and {@link XmlReferenceNotCompatibleException} if the
     * referenced resource is present but not compatible (e.g. int resource referencing string resource).
     * @param resourceId the resource identifier.
     * @return the referenced resource value, never null.
     * @throws XmlReferenceNotFoundException if there's no declared resource with the given id.
     * @throws XmlReferenceNotCompatibleException if the referenced resource is incompatible
     */
    public abstract T resolveReference(String resourceId)
            throws XmlReferenceNotFoundException, XmlReferenceNotCompatibleException;

    /**
     * Parse the given value as a simple value (A value that is not referencing another resource).
     * @param value the value, a not null and not empty string.
     * @return the parsed value, never null.
     * @throws XmlParseException if the value cannot be parsed correctly.
     */
    public abstract T resolveValue(String value) throws XmlParseException;

    /**
     * Checks if the given value is a reference.
     * @param value the value, not null.
     * @return true if the value is reference, false otherwise.
     */
    public static boolean isReference(String value) {
        return REFERENCE_REGEX.matcher(value.trim()).matches();
    }

    /**
     * Gets the referenced resource id. It will throw if the given
     * value is not a reference. Check if the value is a reference
     * first {@link #isReference(String)}.
     * @param value the value, not null.
     * @throws IllegalArgumentException if the value is not a reference.
     * @return the referenced resource id, never null.
     */
    public static String getReferenceId(String value) {
        Matcher matcher = REFERENCE_REGEX.matcher(value.trim());
        if(matcher.matches()) {
            return matcher.group(2);
        }
        System.out.println(value);
        throw new IllegalArgumentException("Value is not a reference!");
    }
}
