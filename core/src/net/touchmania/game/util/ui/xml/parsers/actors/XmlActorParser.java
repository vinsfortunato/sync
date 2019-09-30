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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.ObjectMap;
import net.touchmania.game.util.ui.Dimension;
import net.touchmania.game.util.ui.Layout;
import net.touchmania.game.util.ui.Style;
import net.touchmania.game.util.xml.XmlElementParser;
import net.touchmania.game.util.xml.XmlParseException;
import net.touchmania.game.util.ui.xml.parsers.XmlLayoutParser;
import net.touchmania.game.util.xml.XmlParser;
import net.touchmania.game.util.xml.XmlValueResolver;

import static net.touchmania.game.util.ui.xml.resolvers.XmlIdentifierResolver.GLOBAL_IDENTIFIER_RESOLVER;
import static net.touchmania.game.util.ui.xml.resolvers.XmlTouchableResolver.GLOBAL_TOUCHABLE_RESOLVER;

/**
 * Parses an actor represented by an xml element.
 *
 * @param <T> the type of the actor being parsed.
 */
public abstract class XmlActorParser<T extends Actor> implements XmlElementParser<T> {
    private XmlLayoutParser layoutParser;

    public XmlActorParser(XmlLayoutParser layoutParser) {
        this.layoutParser = layoutParser;
    }

    @Override
    public T parse(XmlParser.Element element) throws XmlParseException {
        T actor = createActor();

        //Parse style
        if(element.hasAttribute("style")) {
            Style style = styleResolver().resolve(element.getAttribute("style"));
            for(String attributeName : style.getAttributeNames()) {
                //Don't parse overridden attributes
                if(!element.hasAttribute(attributeName)) {
                    parseAttributeOrThrow(actor, attributeName, style.getAttributeValue(attributeName));
                }
            }
        }

        //Parse attributes
        for(ObjectMap.Entry<String, String> attribute : element.getAttributes().entries()) {
            String attributeName = attribute.key;
            //Ignore style attribute and parse the others
            if(!attributeName.equals("style")) {
                parseAttributeOrThrow(actor, attributeName, attribute.value);
            }
        }

        //Parse the element text content
        if(element.getText() != null) {
            parseTextContent(actor, element.getText());
        }

        //Parse the element children elements if there are any.
        if(element.getChildCount() > 0) {
            parseChildren(actor, element);
        }

        return actor;
    }

    private void parseAttributeOrThrow(T actor, String name, String value) throws XmlParseException {
        if(!parseAttribute(actor, name, value)) {
            throw new XmlParseException(String.format("Unrecognised attribute with name '%s' and value '%s'!", name, value));
        }
    }

    /**
     * Parses the actor element's attribute with the given name and value. Generally each subclass
     * should call the super method to parse attributes related to the superclass. The boolean
     * value returned by the super method call can be used to check if the attribute has been
     * already parsed and return immediately.
     *
     * @param actor the result actor.
     * @param name the attribute name.
     * @param value the attribute value.
     * @return true if the attribute has been recognised and parsed, false if it has not been recognised.
     * @throws XmlParseException if the attribute has been recognised but cannot be parsed correctly.
     */
    protected boolean parseAttribute(T actor, String name, String value) throws XmlParseException {
        switch(name) {
            case "id": getLayoutParser().setFindableActor(GLOBAL_IDENTIFIER_RESOLVER.resolve(value), actor);    break;
            case "x":         actor.setX(dimensionResolver().resolve(value).getValue());                        break;
            case "y":         actor.setY(dimensionResolver().resolve(value).getValue());                        break;
            case "width":     actor.setWidth(dimensionResolver().resolve(value).getValue());                    break;
            case "height":    actor.setHeight(dimensionResolver().resolve(value).getValue());                   break;
            case "visible":   actor.setVisible(booleanResolver().resolve(value));                               break;
            case "originX":   actor.setOriginX(dimensionResolver().resolve(value).getValue());                  break;
            case "originY":   actor.setOriginY(dimensionResolver().resolve(value).getValue());                  break;
            case "rotation":  actor.setRotation(floatResolver().resolve(value));                                break;
            case "scaleX":    actor.setScaleX(floatResolver().resolve(value));                                  break;
            case "scaleY":    actor.setScaleY(floatResolver().resolve(value));                                  break;
            case "touchable": actor.setTouchable(GLOBAL_TOUCHABLE_RESOLVER.resolve(value));                     break;
            case "debug":     actor.setDebug(booleanResolver().resolve(value));                                 break;
            case "color":     actor.setColor(colorResolver().resolve(value));                                   break;
            case "name":      actor.setName(stringResolver().resolve(value));                                   break;
            default: return false; //Unrecognised attribute
        }

        return true;
    }

    /**
     * Parses the actor element's text content (Every content that is not an element).
     * This is called only if the element has a text content.
     *
     * @param actor the result actor.
     * @param content the actor element's text content.
     * @throws XmlParseException if data cannot be parsed correctly.
     */
    protected void parseTextContent(T actor, String content) throws XmlParseException {}

    /**
     * Parses the element's children. This is called only if the element
     * has any child.
     *
     * @param actor the result actor.
     * @param element the actor element.
     * @throws XmlParseException if data cannot be parsed correctly.
     */
    protected void parseChildren(T actor, XmlParser.Element element) throws XmlParseException {}


    /**
     * Creates the actor where the parsed data will be applied.
     *
     * @return a new instance of the actor.
     */
    protected abstract T createActor();

    public XmlLayoutParser getLayoutParser() {
        return layoutParser;
    }

    /* Reference resolvers access methods */
    public XmlValueResolver<Color>      colorResolver()     { return getLayoutParser().colorResolver;       }
    public XmlValueResolver<Dimension>  dimensionResolver() { return getLayoutParser().dimensionResolver;   }
    public XmlValueResolver<String>     stringResolver()    { return getLayoutParser().stringResolver;      }
    public XmlValueResolver<Layout>     layoutResolver()    { return getLayoutParser().layoutResolver;      }
    public XmlValueResolver<Style>      styleResolver()     { return getLayoutParser().styleResolver;       }
    public XmlValueResolver<Drawable>   drawableResolver()  { return getLayoutParser().drawableResolver;    }
    public XmlValueResolver<BitmapFont> fontResolver()      { return getLayoutParser().fontResolver;        }
    public XmlValueResolver<Integer>    integerResolver()   { return getLayoutParser().integerResolver;     }
    public XmlValueResolver<Float>      floatResolver()     { return getLayoutParser().floatResolver;       }
    public XmlValueResolver<Boolean>    booleanResolver()   { return getLayoutParser().booleanResolver;     }
    public XmlValueResolver<Long>       durationResolver()  { return getLayoutParser().durationResolver;    }
    public XmlValueResolver<Float>      percentResolver()   { return getLayoutParser().percentResolver;     }
}
