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

package net.touchmania.game.resource.xml.parsers.actors;

import com.badlogic.gdx.scenes.scene2d.Group;
import net.touchmania.game.util.xml.XmlParseException;
import net.touchmania.game.resource.xml.parsers.XmlLayoutParser;
import net.touchmania.game.util.xml.XmlParser;

public abstract class XmlGroupParser<T extends Group> extends XmlActorParser<T> {
    public XmlGroupParser(XmlLayoutParser layoutParser) {
        super(layoutParser);
    }

    @Override
    protected void parseChildren(T group, XmlParser.Element element) throws XmlParseException {
        for(int i = 0; i < element.getChildCount(); i++) {
            XmlParser.Element child = element.getChild(i);
            XmlActorParser<?> parser = getLayoutParser().getActorElementParser(child.getName());
            group.addActor(parser.parse(child));
        }
    }
}
