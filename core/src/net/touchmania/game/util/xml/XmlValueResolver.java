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

package net.touchmania.game.util.xml;

/**
 * Resolve a value. It is the base interface used to parse or resolve
 * attribute and element's values in xml documents.
 * @param <T> the type of the value to resolve.
 */
public interface XmlValueResolver<T> {
    /**
     * Resolve a value by parsing it a simple value or by resolving it
     * in a more complex way.
     *
     * @param value the value to resolve, can be null.
     * @return the resolved value.
     * @throws XmlParseException if the value cannot be resolved correctly.
     */
    T resolve(String value) throws XmlParseException;
}
