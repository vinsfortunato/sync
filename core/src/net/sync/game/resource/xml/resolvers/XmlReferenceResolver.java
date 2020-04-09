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

import com.google.common.base.Function;
import net.sync.game.resource.xml.XmlReferenceNotCompatibleException;
import net.sync.game.resource.xml.XmlReferenceNotFoundException;
import net.sync.game.util.xml.XmlDeserializeException;
import net.sync.game.util.xml.XmlValueResolver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Resolve values that can reference other resources.
 * <p> If the value isn't referencing a resource it will be parsed as a simple value
 * (A simple value is a value that doesn't reference another resource).</p>
 * @param <T> the type of the value to resolve.
 */
public abstract class XmlReferenceResolver<T> implements XmlValueResolver<T> {
    public final static Pattern REFERENCE_REGEX = Pattern.compile("([0-9a-zA-Z_-]+):([0-9a-zA-Z_-]+)");

    @Override
    public T resolve(String value) {
        T result;
        Matcher matcher = REFERENCE_REGEX.matcher(value.trim());
        if(matcher.matches()) {
            //Reference value
            String referenceType = matcher.group(1);
            String referenceId = matcher.group(2);

            //Check reference type first
            validateReferenceType(referenceType);

            //Resolve the reference from the reference id
            result = checkNotNull(resolveReference(referenceId),
                    "Unexpected null value! Resolved referenced resource cannot be null.");
        } else {
            //Simple value
            result = checkNotNull(resolveValue(value),
                    "Unexpected null value! Resolved simple value cannot be null.");
        }
        return result;
    }

    /**
     * Validates the reference type by checking if the given reference type is compatible.
     * <p>Base implementation will check if the reference type is the same of {@link #getResourceTypeName()}. </p>
     * @param type the reference type, not null.
     * @throws XmlReferenceNotCompatibleException if the given reference type is not compatible.
     */
    protected void validateReferenceType(String type) {
         String resType = getResourceTypeName();
         if(!type.equals(resType)) {
             throw new XmlReferenceNotCompatibleException(type, resType);
         }
    }

    /**
     * Returns the resource type name.
     * @return the resource type name.
     */
    protected abstract String getResourceTypeName();

    /**
     * Resolve reference by finding a resource with the given id. If the
     * resource cannot be found an {@link XmlDeserializeException} must be
     * thrown. It must throw {@link XmlReferenceNotFoundException} if the referenced resource
     * has not been declared before.
     * @param resourceId the resource identifier.
     * @return the referenced resource value, never null.
     * @throws XmlReferenceNotFoundException if there's no declared resource with the given id.
     */
    public abstract T resolveReference(String resourceId);

    /**
     * Parse the given value as a simple value (A value that is not referencing another resource).
     * @param value the value, a not null and not empty string.
     * @return the parsed value, never null.
     * @throws XmlDeserializeException if the value cannot be parsed correctly.
     */
    public abstract T resolveValue(String value);

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
        throw new IllegalArgumentException("Value is not a reference!");
    }

    /**
     * Creates a reference resolver from a simple value resolver, a
     * resource provider function and the resource type name.
     * @param valueResolver the simple value resolver used to parse the raw value.
     * @param resourceProvider the function used to obtain a resource from a given resource id.
     * @param resourceTypeName the resource type name used to check for reference compatibility.
     * @param <T> the value type.
     * @return a reference value resolver.
     */
    public static <T> XmlReferenceResolver<T> from(final XmlValueResolver<T> valueResolver,
                                                   final Function<String, T> resourceProvider,
                                                   final String resourceTypeName) {
        return new XmlReferenceResolver<T>() {
            @Override
            protected String getResourceTypeName() {
                return resourceTypeName;
            }

            @Override
            public T resolveReference(String resourceId) {
                T value = resourceProvider.apply(resourceId);
                if(value == null)
                    throw new XmlReferenceNotFoundException(
                            String.format("Cannot resolve reference with id '%s'", resourceId));
                return value;
            }

            @Override
            public T resolveValue(String value) {
                return valueResolver.resolve(value);
            }
        };
    }
}
