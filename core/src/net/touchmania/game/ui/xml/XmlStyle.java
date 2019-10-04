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

package net.touchmania.game.ui.xml;

import com.badlogic.gdx.utils.ObjectMap;
import net.touchmania.game.ui.Style;

public class XmlStyle implements Style {
    private ObjectMap<String, String> attributes;

    public void setAttributes(ObjectMap<String, String> attributes) {
        this.attributes = attributes;
    }

    @Override
    public int getAttributesCount() {
        return attributes != null ? attributes.size : 0;
    }

    @Override
    public Iterable<String> getAttributeNames() {
        return attributes.keys();
    }

    @Override
    public String getAttributeValue(String name) {
        return attributes != null ? attributes.get(name) : null;
    }

    @Override
    public boolean hasAttribute(String name) {
        return attributes != null && attributes.containsKey(name);
    }
}
