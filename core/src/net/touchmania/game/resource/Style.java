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

package net.touchmania.game.resource;

public interface Style {
    /**
     * Gets the number of style attributes.
     *
     * @return the attributes count.
     */
    int getAttributesCount();

    /**
     * Gets the names of the style attributes.
     *
     * @return an iterable instance of style attribute names.
     */
    Iterable<String> getAttributeNames();

    /**
     * Gets the value of the attribute with the given name.
     *
     * @param name the attribute's name.
     * @return the value of the attribute with the given name, or null if there's no attribute with the given name.
     */
    String getAttributeValue(String name);

    /**
     * Checks if the style has an attributed with the given name.
     *
     * @param name the name of the attribute to check.
     * @return true if the style has the attribute, false otherwise.
     */
    boolean hasAttribute(String name);
}
