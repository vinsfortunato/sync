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

package net.touchmania.game.ui.xml.parsers.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import net.touchmania.game.ui.xml.parsers.XmlLayoutParser;

public class XmlStackParser extends XmlWidgetGroupParser<Stack> {
    public XmlStackParser(XmlLayoutParser layoutParser) {
        super(layoutParser);
    }

    @Override
    protected Stack createActor() {
        return new Stack();
    }
}
