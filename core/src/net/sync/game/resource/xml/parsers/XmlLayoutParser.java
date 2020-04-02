/*
 * Copyright 2020 Vincenzo Fortunato
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

package net.sync.game.resource.xml.parsers;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import net.sync.game.resource.Dimension;
import net.sync.game.resource.Layout;
import net.sync.game.resource.Style;
import net.sync.game.resource.lazy.Resource;
import net.sync.game.resource.xml.XmlLayout;
import net.sync.game.resource.xml.XmlTheme;
import net.sync.game.resource.xml.parsers.actors.XmlActorParser;
import net.sync.game.resource.xml.resolvers.XmlPercentResolver;
import net.sync.game.util.xml.XmlParseException;
import net.sync.game.util.xml.XmlParser;
import net.sync.game.util.xml.XmlValueResolver;

import java.util.HashMap;
import java.util.Map;

public class XmlLayoutParser extends XmlResourceParser<XmlLayout> {
    private net.sync.game.resource.xml.XmlTheme theme;

    /* Reference resolvers */
    public final XmlValueResolver<Resource<Layout>> layoutResolver = null; //TODO
    public final XmlValueResolver<Resource<Style>> styleResolver;
    public final XmlValueResolver<Resource<Drawable>> drawableResolver;
    public final XmlValueResolver<Color> colorResolver;
    public final XmlValueResolver<Dimension> dimensionResolver;
    public final XmlValueResolver<Resource<BitmapFont>> fontResolver;
    public final XmlValueResolver<Resource<Sound>> soundResolver;
    public final XmlValueResolver<Resource<Music>> musicResolver;
    public final XmlValueResolver<String> stringResolver;
    public final XmlValueResolver<Integer> integerResolver;
    public final XmlValueResolver<Float> floatResolver;
    public final XmlValueResolver<Boolean> booleanResolver;
    public final XmlValueResolver<Long> durationResolver;
    public final XmlValueResolver<Float> percentResolver;

    /* Findable actors map */
    private Map<String, Actor> actorsLookupMap;

    /**
     * Create a resource parser from a its file.
     *
     * @param resourceFile the resource file.
     */
    public XmlLayoutParser(FileHandle resourceFile, XmlTheme theme) {
        super(resourceFile);
        this.theme = theme;

        //Create a new instance for every reference resolver.
        //this.layoutResolver = XmlLayoutResolver.from(theme);
        this.styleResolver = net.sync.game.resource.xml.resolvers.XmlStyleResolver.from(theme);
        this.drawableResolver = net.sync.game.resource.xml.resolvers.XmlDrawableResolver.from(theme);
        this.colorResolver = net.sync.game.resource.xml.resolvers.XmlColorResolver.from(theme);
        this.dimensionResolver = net.sync.game.resource.xml.resolvers.XmlDimensionResolver.from(theme);
        this.fontResolver = net.sync.game.resource.xml.resolvers.XmlFontResolver.from(theme);
        this.soundResolver = net.sync.game.resource.xml.resolvers.XmlSoundResolver.from(theme);
        this.musicResolver = net.sync.game.resource.xml.resolvers.XmlMusicResolver.from(theme);
        this.stringResolver = net.sync.game.resource.xml.resolvers.XmlStringResolver.from(theme);
        this.integerResolver = net.sync.game.resource.xml.resolvers.XmlIntegerResolver.from(theme);
        this.floatResolver = net.sync.game.resource.xml.resolvers.XmlFloatResolver.from(theme);
        this.booleanResolver = net.sync.game.resource.xml.resolvers.XmlBooleanResolver.from(theme);
        this.durationResolver = net.sync.game.resource.xml.resolvers.XmlDurationResolver.from(theme);
        this.percentResolver = XmlPercentResolver.from(theme);
    }

    @Override
    public net.sync.game.resource.xml.XmlLayout parse(XmlParser.Element root) throws XmlParseException {
        //Parse actors
        XmlParser.Element element = root.getChild(0);
        Actor actor = getActorElementParser(element.getName()).parse(element);

        //Build the layout
        net.sync.game.resource.xml.XmlLayout layout = new XmlLayout();
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
            actorsLookupMap = new HashMap<>();
        }

        if(actorsLookupMap.containsKey(id)) { //Check for duplicates
            throw new XmlParseException(String.format("Id '%s' is already associated to an actor!", id));
        }

        actorsLookupMap.put(id, actor);
    }

    /**
     * Gets a {@link net.sync.game.resource.xml.parsers.actors.XmlActorParser} that can parse an element with the given name and return
     * the corresponding {@link Actor} with properties described by the element attributes and its children elements.
     *
     * @param elementName the element name.
     * @throws XmlParseException if there's no parser that associated to the given element name.
     * @return the {@link net.sync.game.resource.xml.parsers.actors.XmlActorParser} that can parse the element with given element name.
     */
    public XmlActorParser<?> getActorElementParser(String elementName) throws XmlParseException {
        switch(elementName) {
            case "Slider" : return new net.sync.game.resource.xml.parsers.actors.XmlSliderParser(this);
            case "Container": return new net.sync.game.resource.xml.parsers.actors.XmlContainerParser(this);
            case "HorizontalGroup": return new net.sync.game.resource.xml.parsers.actors.XmlHorizontalGroupParser(this);
            case "VerticalGroup": return new net.sync.game.resource.xml.parsers.actors.XmlVerticalGroupParser(this);
            case "ScrollPane": return new net.sync.game.resource.xml.parsers.actors.XmlScrollPaneParser(this);
            case "Stack": return new net.sync.game.resource.xml.parsers.actors.XmlStackParser(this);
            case "Table": return new net.sync.game.resource.xml.parsers.actors.XmlTableParser(this);
        }
        throw new XmlParseException(String.format("Unrecognised element with name '%s'", elementName));
    }
}
