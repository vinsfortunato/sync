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

import java.util.regex.Pattern;

/**
 * Resolves id values used to bind resources. Id values can contain
 * only alphanumeric characters plus the characters '_' and '-' and can have only
 * leading/trailing whitespaces that will be removed when resolved.
 */
public class XmlIdentifierResolver implements XmlValueResolver<String> {
    /** Global Identifier resolver. Can be used to avoid creating a new instance of this class every time it is used. */
    public static final XmlValueResolver<String> GLOBAL_IDENTIFIER_RESOLVER = new XmlIdentifierResolver();

    private static final Pattern ID_REGEX = Pattern.compile("[0-9a-zA-Z_-]+");

    @Override
    public String resolve(String value) throws XmlParseException {
        if(value == null) {
            throw new XmlParseException("Id not found!");
        }
        value = value.trim();
        if (!ID_REGEX.matcher(value).matches()) {
            throw new XmlParseException("Invalid id! Id can contain only alphanumeric characters plus _ and -!");
        }
        return value;
    }
}
