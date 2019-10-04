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

package net.touchmania.game.ui.xml.resolvers;

import net.touchmania.game.ui.ResourceProvider;
import net.touchmania.game.util.xml.XmlParseException;
import net.touchmania.game.util.xml.XmlValueResolver;

public abstract class XmlFloatResolver extends XmlReferenceValueResolver<Float> {
    /** Global Float primitive resolver. */
    public static XmlValueResolver<Float> GLOBAL_FLOAT_RESOLVER = new XmlValueResolver<Float>() {

        @Override
        public Float resolve(String value) throws XmlParseException {
            if(value != null) {
                try {
                    return Float.parseFloat(value);
                } catch(NumberFormatException e) {}
            }
            throw new XmlParseException(String.format("Invalid float value '%s'!", value));
        }
    };

    @Override
    protected String getResourceTypeName() {
        return "value";
    }

    @Override
    public Float resolveValue(String value) throws XmlParseException {
        if(value != null) {
            try {
                return Float.parseFloat(value);
            } catch(NumberFormatException e) {}
        }
        throw new XmlParseException(String.format("Invalid float value '%s'!", value));
    }

    public static XmlFloatResolver from(final ResourceProvider provider) {
        return new XmlFloatResolver() {
            @Override
            public Float resolveReference(String resourceId) throws XmlParseException {
                return provider.getFloat(resourceId);
            }
        };
    }
}
