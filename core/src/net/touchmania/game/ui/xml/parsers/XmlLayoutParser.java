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

package net.touchmania.game.ui.xml.parsers;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.ObjectMap;
import net.touchmania.game.ui.Style;
import net.touchmania.game.ui.xml.parsers.actors.*;
import net.touchmania.game.ui.xml.resolvers.*;
import net.touchmania.game.util.xml.XmlParseException;
import net.touchmania.game.ui.Dimension;
import net.touchmania.game.ui.Layout;
import net.touchmania.game.ui.xml.*;
import net.touchmania.game.util.xml.XmlParser;
import net.touchmania.game.util.xml.XmlValueResolver;

public class XmlLayoutParser extends XmlResourceParser<XmlLayout> {
    private XmlTheme theme;

    /* Reference resolvers */
    public final XmlValueResolver<Color> colorResolver;
    public final XmlValueResolver<Dimension> dimensionResolver;
    public final XmlValueResolver<String> stringResolver = null; //TODO
    public final XmlValueResolver<Layout> layoutResolver = null; //TODO
    public final XmlValueResolver<Style> styleResolver = null; //TODO
    public final XmlValueResolver<Drawable> drawableResolver = null; //TODO
    public final XmlValueResolver<BitmapFont> fontResolver = null; //TODO
    public final XmlValueResolver<Integer> integerResolver;
    public final XmlValueResolver<Float> floatResolver;
    public final XmlValueResolver<Boolean> booleanResolver;
    public final XmlValueResolver<Long> durationResolver;
    public final XmlValueResolver<Float> percentResolver;

    /* Findable actors map */
    private ObjectMap<String, Actor> actorsLookupMap;

    /**
     * Create a resource parser from a its file.
     *
     * @param resourceFile the resource file.
     */
    public XmlLayoutParser(FileHandle resourceFile, XmlTheme theme) {
        super(resourceFile);
        this.theme = theme;

        //Create a new instance for every reference resolver.
        this.colorResolver = XmlColorResolver.from(theme);
        this.dimensionResolver = XmlDimensionResolver.from(theme);
        this.integerResolver = XmlIntegerResolver.from(theme);
        this.floatResolver = XmlFloatResolver.from(theme);
        this.booleanResolver = XmlBooleanResolver.from(theme);
        this.durationResolver = XmlDurationResolver.from(theme);
        this.percentResolver = XmlPercentResolver.from(theme);
    }

    @Override
    public XmlLayout parse(XmlParser.Element root) throws XmlParseException {
        //Parse actors
        XmlParser.Element element = root.getChild(0);
        Actor actor = getActorElementParser(element.getName()).parse(element);

        //Build the layout
        XmlLayout layout = new XmlLayout();
        layout.setRootActor(actor);
        layout.setActorsLookupMap(actorsLookupMap);
        return layout;
    }

    @Override
    protected void checkRoot(XmlParser.Element root) throws XmlParseException {
        if(!root.getName().equals("layout")) {
            throw new XmlParseException("Unexpected xml root element name. Expected to be 'layout'");
        }

        if(root.getChildCount() > 1) {
            throw new XmlParseException("Invalid layout element content! Layout element can have only one child element!");
        }
    }

    /**
     * Associates an actor to an id.
     *
     * @param id the actor id.
     * @param actor the actor to associate.
     * @throws XmlParseException if another actor is already associated to the given id.
     */
    public void setFindableActor(String id, Actor actor) throws XmlParseException {
        if(actorsLookupMap == null) {
            actorsLookupMap = new ObjectMap<>();
        }

        if(actorsLookupMap.containsKey(id)) { //Check for duplicates
            throw new XmlParseException(String.format("Id '%s' is already associated to an actor!", id));
        }

        actorsLookupMap.put(id, actor);
    }

    /**
     * Gets a {@link XmlActorParser} that can parse an element with the given name and return
     * the corresponding {@link Actor} with properties described by the element attributes and its children elements.
     *
     * @param elementName the element name.
     * @throws XmlParseException if there's no parser that associated to the given element name.
     * @return the {@link XmlActorParser} that can parse the element with given element name.
     */
    public XmlActorParser<?> getActorElementParser(String elementName) throws XmlParseException {
        switch(elementName) {
            case "Slider" : return new XmlSliderParser(this);
            case "Container": return new XmlContainerParser(this);
            case "HorizontalGroup": return new XmlHorizontalGroupParser(this);
            case "VerticalGroup": return new XmlVerticalGroupParser(this);
            case "ScrollPane": return new XmlScrollPaneParser(this);
            case "Stack": return new XmlStackParser(this);
            case "Table": return new XmlTableParser(this);
        }

        throw new XmlParseException(String.format("Unrecognised element with name '%s'", elementName));
    }
}
