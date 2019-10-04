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

import net.touchmania.game.resource.FontGenerator;
import net.touchmania.game.resource.ResourceProvider;
import net.touchmania.game.util.xml.XmlParseException;

public abstract class XmlFontResolver extends XmlReferenceValueResolver<FontGenerator> {
    @Override
    protected String getResourceTypeName() {
        return "font";
    }

    @Override
    public FontGenerator resolveValue(String value) throws XmlParseException {
        throw new XmlParseException(String.format("Cannot resolve the value '%s'", value));
    }

    public static XmlFontResolver from(final ResourceProvider provider) {
        return new XmlFontResolver() {
            @Override
            public FontGenerator resolveReference(String resourceId) {
                return provider.getFont(resourceId);
            }
        };
    }
}
