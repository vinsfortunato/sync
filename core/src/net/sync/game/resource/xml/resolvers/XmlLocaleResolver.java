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

import net.sync.game.util.xml.XmlParseException;
import net.sync.game.util.xml.XmlValueResolver;

import java.util.Locale;

public class XmlLocaleResolver implements XmlValueResolver<Locale> {
    public static final XmlLocaleResolver GLOBAL_LOCALE_RESOLVER = new XmlLocaleResolver();

    @Override
    public Locale resolve(String value) throws XmlParseException {
        //Prepare the value for parsing by removing leading/trailing spaces
        value = value.trim();

        if(value.contains("_")) {
            String parts[] = value.split("_");
            String lang = parts[0];
            String country = parts[1];
            return new Locale(lang, country);
        }

        return new Locale(value);
    }
}
