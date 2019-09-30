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

package net.touchmania.game.util.ui.xml.parsers.actors;

import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import net.touchmania.game.util.ui.xml.parsers.XmlLayoutParser;
import net.touchmania.game.util.xml.XmlParseException;
import net.touchmania.game.util.xml.XmlValueResolver;

public abstract class XmlWidgetGroupParser<T extends WidgetGroup> extends XmlGroupParser<T> {
    public XmlWidgetGroupParser(XmlLayoutParser layoutParser) {
        super(layoutParser);
    }

    @Override
    protected boolean parseAttribute(T group, String name, String value) throws XmlParseException {
        if(super.parseAttribute(group, name, value)) {
            //Attribute already parsed
            return true;
        }

        switch(name) {
            case "fillParent": group.setFillParent(booleanResolver().resolve(value));                           break;
            default: return false; //Unrecognised attribute
        }

        return true;
    }
}
