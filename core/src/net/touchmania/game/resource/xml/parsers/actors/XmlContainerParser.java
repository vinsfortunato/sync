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

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import net.touchmania.game.resource.xml.parsers.XmlLayoutParser;
import net.touchmania.game.resource.xml.resolvers.XmlAlignResolver;
import net.touchmania.game.util.xml.XmlParseException;
import net.touchmania.game.util.xml.XmlParser;

public class XmlContainerParser extends XmlWidgetGroupParser<Container<Actor>> {
    public XmlContainerParser(XmlLayoutParser layoutParser) {
        super(layoutParser);
    }

    @Override
    protected boolean parseAttribute(Container<Actor> container, String name, String value) throws XmlParseException {
        if(super.parseAttribute(container, name, value)) {
            //Attribute already parsed
            return true;
        }

        switch(name) {
            case "align":      container.align(XmlAlignResolver.GLOBAL_ALIGN_RESOLVER.resolve(value));           break;
            case "background": container.setBackground(drawableResolver().resolve(value));                       break;
            case "fillX":      if(booleanResolver().resolve(value)) container.fillX();                           break;
            case "fillY":      if(booleanResolver().resolve(value)) container.fillY();                           break;
            case "clip":       container.setClip(booleanResolver().resolve(value));                              break;
            case "round":      container.setRound(booleanResolver().resolve(value));                             break;
            case "maxHeight":  container.maxHeight(dimensionResolver().resolve(value).getValue());               break;
            case "minHeight":  container.minHeight(dimensionResolver().resolve(value).getValue());               break;
            case "prefHeight": container.prefHeight(dimensionResolver().resolve(value).getValue());              break;
            case "maxWidth":   container.maxWidth(dimensionResolver().resolve(value).getValue());                break;
            case "minWidth":   container.minWidth(dimensionResolver().resolve(value).getValue());                break;
            case "prefWidth":  container.prefWidth(dimensionResolver().resolve(value).getValue());               break;
            case "maxSize":    container.maxSize(dimensionResolver().resolve(value).getValue());                 break;
            case "minSize":    container.minSize(dimensionResolver().resolve(value).getValue());                 break;
            case "prefSize":   container.prefSize(dimensionResolver().resolve(value).getValue());                break;
            case "padLeft":    container.padLeft(dimensionResolver().resolve(value).getValue());                 break;
            case "padRight":   container.padRight(dimensionResolver().resolve(value).getValue());                break;
            case "padTop":     container.padTop(dimensionResolver().resolve(value).getValue());                  break;
            case "padBottom":  container.padBottom(dimensionResolver().resolve(value).getValue());               break;
            case "pad":        container.pad(dimensionResolver().resolve(value).getValue());                     break;
            default: return false; //Unrecognised attribute
        }

        return true;
    }

    @Override
    protected void parseChildren(Container<Actor> container, XmlParser.Element element) throws XmlParseException {
        if(element.getChildCount() != 1) {
            throw new XmlParseException("Container can only have one child!");
        }

        //Parse and set the container child.
        XmlParser.Element child = element.getChild(0);
        XmlActorParser<?> parser = getLayoutParser().getActorElementParser(child.getName());
        container.setActor(parser.parse(child));
    }

    @Override
    protected Container<Actor> createActor() {
        return new Container<>();
    }
}
