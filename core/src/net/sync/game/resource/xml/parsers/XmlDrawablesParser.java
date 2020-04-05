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

package net.sync.game.resource.xml.parsers;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import net.sync.game.resource.Dimension;
import net.sync.game.resource.lazy.*;
import net.sync.game.resource.xml.XmlReferenceNotCompatibleException;
import net.sync.game.resource.xml.XmlReferenceNotFoundException;
import net.sync.game.resource.xml.XmlTheme;
import net.sync.game.resource.xml.resolvers.*;
import net.sync.game.util.xml.XmlParseException;
import net.sync.game.util.xml.XmlParser;
import net.sync.game.util.xml.XmlValueResolver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static net.sync.game.resource.xml.resolvers.XmlPixmapFormatResolver.GLOBAL_PIXMAP_FORMAT_RESOLVER;
import static net.sync.game.resource.xml.resolvers.XmlTextureFilterResolver.GLOBAL_TEXTURE_FILTER_RESOLVER;
import static net.sync.game.resource.xml.resolvers.XmlTextureWrapResolver.GLOBAL_TEXTURE_WRAP_RESOLVER;

public class XmlDrawablesParser extends XmlMapResourceParser<Resource<Drawable>> {
    private final XmlTheme theme;

    /**
     * Create a resource parser from its file.
     * @param resourceFile the resource file.
     */
    public XmlDrawablesParser(FileHandle resourceFile, XmlTheme theme) {
        super(resourceFile);
        this.theme = theme;

        //Init resolvers
        this.dimensionResolver = XmlDimensionResolver.from(theme);
        this.booleanResolver = XmlBooleanResolver.from(theme);
        this.colorResolver = XmlColorResolver.from(theme);
        this.floatResolver = XmlFloatResolver.from(theme);
        this.integerResolver = XmlIntegerResolver.from(theme);
        this.stringResolver = XmlStringResolver.from(theme);
    }

    @Override
    public Map<String, Resource<Drawable>> parse(XmlParser.Element root) throws XmlParseException {
        //Merge xml included files into root
        mergeIncludes(root);
        return super.parse(root);
    }

    private void mergeIncludes(XmlParser.Element root) throws XmlParseException {
        //Included files are located inside the drawables dir
        FileHandle includesDir = getResourceFile().sibling("drawables");

        //Map each include element index to the included xml root element
        Map<Integer, XmlParser.Element> includes = new HashMap<>();
        for(int i = 0; i < root.getChildCount(); i++) {
            XmlParser.Element element = root.getChild(i);
            if(element.getName().equals("include")) {
                //Check include file
                if(element.getText().isEmpty()) {
                    throw new XmlParseException("Empty include file name!");
                }
                FileHandle includeFile = includesDir.child(element.getText());
                if(!includeFile.exists()) {
                    throw new XmlParseException("Include file not found!");
                }

                //Parse include file
                XmlParser parser = new XmlParser();
                XmlParser.Element includeRoot;
                try {
                    includeRoot = parser.parse(includeFile);
                } catch (IOException e) {
                    throw new XmlParseException("Cannot parse include xml file!");
                }
                if(includeRoot == null) {
                    throw new XmlParseException("Empty include xml resource file!");
                }
                //Include root should be the same as the root
                checkRoot(includeRoot);

                //Map included xml root element to the include element index
                includes.put(i, includeRoot);
            }
        }

        //Merge include files by removing each include element and adding children of
        //the parsed include xml root at its index.
        int increasedSize = 0;
        Array<XmlParser.Element> children = root.getChildren();
        for(Integer i : includes.keySet()) {
            Array<XmlParser.Element> elems = includes.get(i).getChildren();
            children.addAll(elems, i + increasedSize, elems.size);
            children.removeIndex(i + increasedSize);
            increasedSize += elems.size - 1;
        }
    }

    @Override
    protected void parseAttributes(String id, Resource<Drawable> value, XmlParser.Element element) throws XmlParseException {
        for (ObjectMap.Entry<String, String> attribute : element.getAttributes()) {
            String key = attribute.key;
            String val = attribute.value;

            //Skip id attribute
            if(key.equals("id")) continue;

            //Parse attribute
            boolean parsed = false;
            if(value instanceof DrawableResource && parseAttribute((DrawableResource) value, key, val))     parsed = true;
            if(value instanceof TextureResource && parseAttribute((TextureResource) value, key, val))       parsed = true;
            if(value instanceof RegionResource && parseAttribute((RegionResource) value, key, val))         parsed = true;
            if(value instanceof SpriteResource && parseAttribute((SpriteResource) value, key, val))         parsed = true;
            if(value instanceof NinepatchResource && parseAttribute((NinepatchResource) value, key, val))   parsed = true;

            if(!parsed)
                throw new XmlParseException(String.format(
                        "Unrecognised attribute with name '%s' and value '%s'!", attribute.key, attribute.value));
        }
    }

    private boolean parseAttribute(DrawableResource resource, String name, String value) throws XmlParseException {
        switch(name) {
            case "leftWidth":    resource.leftWidth = floatResolver.resolve(value);                               break;
            case "rightWidth":   resource.rightWidth = floatResolver.resolve(value);                              break;
            case "topHeight":    resource.topHeight = floatResolver.resolve(value);                               break;
            case "bottomHeight": resource.bottomHeight = floatResolver.resolve(value);                            break;
            default: return false; //Unrecognised attribute
        }

        return true;
    }

    private boolean parseAttribute(TextureResource resource, String name, String value) throws XmlParseException {
        switch(name) {
            case "minFilter":  resource.minFilter = GLOBAL_TEXTURE_FILTER_RESOLVER.resolve(value);                break;
            case "maxFilter":  resource.magFilter = GLOBAL_TEXTURE_FILTER_RESOLVER.resolve(value);                break;
            case "uWrap":      resource.uWrap = GLOBAL_TEXTURE_WRAP_RESOLVER.resolve(value);                      break;
            case "vWrap":      resource.vWrap = GLOBAL_TEXTURE_WRAP_RESOLVER.resolve(value);                      break;
            case "format":     resource.format = GLOBAL_PIXMAP_FORMAT_RESOLVER.resolve(value);                    break;
            case "useMipMaps": resource.useMipMaps = booleanResolver.resolve(value);                              break;
            default: return false; //Unrecognised attribute
        }

        return true;
    }

    private boolean parseAttribute(RegionResource resource, String name, String value) throws XmlParseException {
        switch(name) {
            case "x":      resource.x = dimensionResolver.resolve(value).getIntValue();                           break;
            case "y":      resource.y = dimensionResolver.resolve(value).getIntValue();                           break;
            case "width":  resource.width = dimensionResolver.resolve(value).getIntValue();                       break;
            case "height": resource.height = dimensionResolver.resolve(value).getIntValue();                      break;
            default: return false; //Unrecognised attribute
        }

        return true;
    }

    private boolean parseAttribute(SpriteResource resource, String name, String value) throws XmlParseException {
        switch(name) { //TODO
            case "x": break;
            default: return false; //Unrecognised attribute
        }

        return true;
    }

    private boolean parseAttribute(NinepatchResource resource, String name, String value) throws XmlParseException {
        switch(name) { //TODO
            case "x": break;
            default: return false; //Unrecognised attribute
        }

        return true;
    }

    @Override
    protected void checkRoot(XmlParser.Element root) throws XmlParseException {
        if(!root.getName().equals("drawables")) {
            throw new XmlParseException("Unexpected xml root element name. Expected to be 'drawables'!");
        }
    }

    @Override
    protected void checkRootChild(XmlParser.Element element) throws XmlParseException {
        switch(element.getName()) {
            case "texture":
            case "region":
            case "sprite":
            case "ninepatch":
            case "drawable":
                return;
            default:
                throw new XmlParseException(String.format("Unexpected element name '%s'!", element.getName()));
        }
    }

    @Override
    protected XmlReferenceResolver<Resource<Drawable>> getResolver(XmlParser.Element element) {
        switch (element.getName()) {
            case "texture":     return textureResolver;
            case "region":      return regionResolver;
            case "sprite":      return spriteResolver;
            case "drawable":    return drawableResolver;
            case "ninepatch":   return ninepatchResolver;
            default:
                throw new IllegalArgumentException("Unexpected element name!");
        }
    }

    /* Resolvers */
    private final XmlValueResolver<Dimension> dimensionResolver;
    private final XmlValueResolver<Boolean> booleanResolver;
    private final XmlValueResolver<Color> colorResolver;
    private final XmlValueResolver<Float> floatResolver;
    private final XmlValueResolver<Integer> integerResolver;
    private final XmlValueResolver<String> stringResolver;

    /* Drawable resolver */
    private final XmlReferenceResolver<Resource<Drawable>> drawableResolver = new XmlDrawableResolver() {
        @Override
        public Resource<Drawable> resolveReference(String resourceId) throws XmlReferenceNotFoundException, XmlReferenceNotCompatibleException {
            Resource<Drawable> resource = getResolvedValueOrThrow(resourceId);

            if(resource instanceof DrawableResource) {
                return ((DrawableResource)resource).copy();
            }

            throw XmlReferenceNotCompatibleException.incompatibleType(resource.getClass(), DrawableResource.class);
        }

        @Override
        public Resource<Drawable> resolveValue(String value) throws XmlParseException {
            //<drawable> is used only for referencing. So no need to parse value.
            throw new XmlParseException("Illegal value. Drawable resource value must be a reference!");
        }

        @Override
        public void checkReferenceType(String type) throws XmlReferenceNotCompatibleException {
            switch(type) {
                case "drawable":
                case "texture":
                case "region":
                case "sprite":
                case "ninepatch":
                    return;
            }
            throw new XmlReferenceNotCompatibleException("Incompatible drawable reference type!");
        }
    };

    /* Sprite resolver */
    private final XmlReferenceResolver<Resource<Drawable>> spriteResolver = new XmlDrawableResolver() {
        @Override
        public Resource<Drawable> resolveReference(String resourceId) throws XmlReferenceNotFoundException, XmlReferenceNotCompatibleException {
            Resource<Drawable> resource = getResolvedValueOrThrow(resourceId);

            if(resource instanceof SpriteResource)
                return ((SpriteResource)resource).copy();

            throw XmlReferenceNotCompatibleException.incompatibleType(resource.getClass(), SpriteResource.class);
        }

        @Override
        public Resource<Drawable> resolveValue(String value) throws XmlParseException {
            //TODO
            return null;
        }

        @Override
        public void checkReferenceType(String type) throws XmlReferenceNotCompatibleException {
            switch(type) {
                case "drawable":
                case "texture":
                case "region":
                case "sprite":
                    return;
            }
            throw new XmlReferenceNotCompatibleException("Incompatible sprite reference type!");
        }
    };

    /* Ninepatch Resolver */
    private final XmlReferenceResolver<Resource<Drawable>> ninepatchResolver = new XmlDrawableResolver() {
        @Override
        public Resource<Drawable> resolveReference(String resourceId) throws XmlReferenceNotFoundException, XmlReferenceNotCompatibleException {
            Resource<Drawable> resource = getResolvedValueOrThrow(resourceId);

            if(resource instanceof NinepatchResource)
                return ((NinepatchResource)resource).copy();

            throw XmlReferenceNotCompatibleException.incompatibleType(resource.getClass(), NinepatchResource.class);
        }

        @Override
        public Resource<Drawable> resolveValue(String value) throws XmlParseException {
            //TODO
            return null;
        }

        @Override
        public void checkReferenceType(String type) throws XmlReferenceNotCompatibleException {
            switch(type) {
                case "drawable":
                case "texture":
                case "region":
                case "ninepatch":
                    return;
            }
            throw new XmlReferenceNotCompatibleException("Incompatible ninepatch reference type!");
        }
    };

    /* Texture resolver */
    private final XmlReferenceResolver<Resource<Drawable>> textureResolver = new XmlDrawableResolver() {
        @Override
        public Resource<Drawable> resolveReference(String resourceId) throws XmlReferenceNotFoundException, XmlReferenceNotCompatibleException {
            Resource<Drawable> resource = getResolvedValueOrThrow(resourceId);

            if(resource instanceof TextureResource)
                return new TextureResource((TextureResource)resource);

            throw XmlReferenceNotCompatibleException.incompatibleType(resource.getClass(), TextureResource.class);
        }

        @Override
        public Resource<Drawable> resolveValue(String value) {
            return new TextureResource(theme.getTexturePath(value));
        }

        @Override
        public void checkReferenceType(String type) throws XmlReferenceNotCompatibleException {
            switch(type) {
                case "drawable":
                case "texture":
                    return;
            }
            throw new XmlReferenceNotCompatibleException("Incompatible texture reference type!");
        }
    };

    /* Region resolver */
    private final XmlReferenceResolver<Resource<Drawable>> regionResolver = new XmlDrawableResolver() {
        @Override
        public Resource<Drawable> resolveReference(String resourceId) throws XmlReferenceNotFoundException, XmlReferenceNotCompatibleException {
            Resource<Drawable> resource = getResolvedValueOrThrow(resourceId);

            if(resource instanceof RegionResource)
                return new RegionResource((RegionResource)resource);
            if(resource instanceof TextureResource)
                return new RegionResource((TextureResource)resource);

            throw XmlReferenceNotCompatibleException.incompatibleType(resource.getClass(), RegionResource.class);
        }

        @Override
        public Resource<Drawable> resolveValue(String value) throws XmlParseException {
            return new RegionResource(theme.getTexturePath(value));
        }

        @Override
        public void checkReferenceType(String type) throws XmlReferenceNotCompatibleException {
            switch(type) {
                case "drawable":
                case "texture":
                case "region":
                    return;
            }
            throw new XmlReferenceNotCompatibleException("Incompatible region reference type!");
        }
    };
}
