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

import net.touchmania.game.resource.Dimension;
import net.touchmania.game.resource.ResourceProvider;
import net.touchmania.game.resource.xml.XmlReferenceNotFoundException;
import net.touchmania.game.util.xml.XmlParseException;

public abstract class XmlDimensionResolver extends XmlReferenceResolver<Dimension> {
    @Override
    protected String getResourceTypeName() {
        return "dimen";
    }

    @Override
    public Dimension resolveValue(String value) throws XmlParseException {
        try {
            return new Dimension(Float.parseFloat(value));
        } catch(NumberFormatException e) {
            throw new XmlParseException("Invalid dimension value!");
        }
    }

    public static XmlDimensionResolver from(final ResourceProvider provider) {
        return new XmlDimensionResolver() {
            @Override
            public Dimension resolveReference(String resourceId) throws XmlReferenceNotFoundException {
                Dimension dimen = provider.getDimension(resourceId);

                if(dimen == null)
                    throw new XmlReferenceNotFoundException(
                            String.format("Cannot resolve reference with id '%s'", resourceId));

                return dimen;
            }
        };
    }
}
