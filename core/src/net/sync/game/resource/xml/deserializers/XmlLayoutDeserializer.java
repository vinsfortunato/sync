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

package net.sync.game.resource.xml.deserializers;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import net.sync.game.resource.Dimension;
import net.sync.game.resource.Layout;
import net.sync.game.resource.MapTheme;
import net.sync.game.resource.Style;
import net.sync.game.resource.lazy.Resource;
import net.sync.game.resource.xml.XmlLayout;
import net.sync.game.resource.xml.deserializers.actors.*;
import net.sync.game.resource.xml.resolvers.*;
import net.sync.game.util.xml.XmlDeserializeException;
import net.sync.game.util.xml.XmlParser;
import net.sync.game.util.xml.XmlValueResolver;

import java.util.HashMap;
import java.util.Map;

//TODO rewrite
public class XmlLayoutDeserializer extends XmlResourceDeserializer<XmlLayout> {
    private MapTheme theme;

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
    public XmlLayoutDeserializer(FileHandle resourceFile, MapTheme theme) {
        super(resourceFile);
        this.theme = theme;

        //Create a new instance for every reference resolver.
        //this.layoutResolver = XmlLayoutResolver.from(theme);
        this.styleResolver = XmlStyleResolver.from(theme);
        this.drawableResolver = XmlDrawableResolver.from(theme);
        this.colorResolver = XmlColorResolver.from(theme);
        this.dimensionResolver = XmlDimensionResolver.from(theme);
        this.fontResolver = XmlFontResolver.from(theme);
        this.soundResolver = XmlSoundResolver.from(theme);
        this.musicResolver = XmlMusicResolver.from(theme);
        this.stringResolver = XmlStringResolver.from(theme);
        this.integerResolver = XmlIntegerResolver.from(theme);
        this.floatResolver = XmlFloatResolver.from(theme);
        this.booleanResolver = XmlBooleanResolver.from(theme);
        this.durationResolver = XmlDurationResolver.from(theme);
        this.percentResolver = XmlPercentResolver.from(theme);
    }

    @Override
    public net.sync.game.resource.xml.XmlLayout parse(XmlParser.Element root) throws XmlDeserializeException {
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
    protected void checkRoot(XmlParser.Element root) throws XmlDeserializeException {
        if(!root.getName().equals("layout")) {
            throw new XmlDeserializeException("Unexpected xml root element name. Expected to be 'layout'");
        }

        if(root.getChildCount() > 1) {
            throw new XmlDeserializeException("Invalid layout element content! Layout element can have only one child element!");
        }
    }

    /**
     * Associates an actor to an id.
     *
     * @param id the actor id.
     * @param actor the actor to associate.
     * @throws XmlDeserializeException if another actor is already associated to the given id.
     */
    public void setFindableActor(String id, Actor actor) throws XmlDeserializeException {
        if(actorsLookupMap == null) {
            actorsLookupMap = new HashMap<>();
        }

        if(actorsLookupMap.containsKey(id)) { //Check for duplicates
            throw new XmlDeserializeException(String.format("Id '%s' is already associated to an actor!", id));
        }

        actorsLookupMap.put(id, actor);
    }

    /**
     * Gets a {@link XmlActorDeserializer} that can parse an element with the given name and return
     * the corresponding {@link Actor} with properties described by the element attributes and its children elements.
     *
     * @param elementName the element name.
     * @throws XmlDeserializeException if there's no parser that associated to the given element name.
     * @return the {@link XmlActorDeserializer} that can parse the element with given element name.
     */
    public XmlActorDeserializer<?> getActorElementParser(String elementName) throws XmlDeserializeException {
        switch(elementName) {
            case "Slider" : return new XmlSliderDeserializer(this);
            case "Container": return new XmlContainerDeserializer(this);
            case "HorizontalGroup": return new XmlHorizontalGroupDeserializer(this);
            case "VerticalGroup": return new XmlVerticalGroupParser(this);
            case "ScrollPane": return new XmlScrollPaneDeserializer(this);
            case "Stack": return new XmlStackDeserializer(this);
            case "Table": return new XmlTableDeserializer(this);
        }
        throw new XmlDeserializeException(String.format("Unrecognised element with name '%s'", elementName));
    }
}
