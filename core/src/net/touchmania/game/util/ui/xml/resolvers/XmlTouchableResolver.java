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

package net.touchmania.game.util.ui.xml.resolvers;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import net.touchmania.game.util.xml.XmlParseException;
import net.touchmania.game.util.xml.XmlValueResolver;

public class XmlTouchableResolver implements XmlValueResolver<Touchable> {
    /** Global Touchable resolver. Can be used to avoid creating a new instance of this class every time it is used. */
    public static final XmlValueResolver<Touchable> GLOBAL_TOUCHABLE_RESOLVER = new XmlTouchableResolver();

    @Override
    public Touchable resolve(String value) throws XmlParseException {
        if(value == null || value.isEmpty()) {
            throw new XmlParseException("Invalid Touchable value! Value cannot be empty or null!");
        }

        switch(value) {
            case "enabled": return Touchable.enabled;
            case "disabled": return Touchable.disabled;
            case "children_only": return Touchable.childrenOnly;
        }

        throw new XmlParseException(String.format("Cannot resolve touchable value '%s'!", value));
    }
}
