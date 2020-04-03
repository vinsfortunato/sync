/*
 * Copyright (c) 2020 Vincenzo Fortunato.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.sync.game.resource.xml.parsers.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import net.sync.game.resource.xml.parsers.XmlLayoutParser;
import net.sync.game.resource.xml.resolvers.XmlAlignResolver;
import net.sync.game.util.xml.XmlParseException;
import net.sync.game.util.xml.XmlParser;

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
            //case "background": container.setBackground(drawableResolver().resolve(value));                       break;TODO
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
