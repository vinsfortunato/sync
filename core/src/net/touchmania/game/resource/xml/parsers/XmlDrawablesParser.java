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

package net.touchmania.game.resource.xml.parsers;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import net.touchmania.game.resource.*;
import net.touchmania.game.resource.lazy.DrawableResource;
import net.touchmania.game.resource.lazy.Resource;
import net.touchmania.game.resource.xml.*;
import net.touchmania.game.resource.xml.resolvers.*;
import net.touchmania.game.util.xml.XmlParseException;
import net.touchmania.game.util.xml.XmlParser;
import net.touchmania.game.util.xml.XmlValueResolver;
import static net.touchmania.game.resource.xml.resolvers.XmlTextureFilterResolver.GLOBAL_TEXTURE_FILTER_RESOLVER;
import static net.touchmania.game.resource.xml.resolvers.XmlTextureWrapResolver.GLOBAL_TEXTURE_WRAP_RESOLVER;
import static net.touchmania.game.resource.xml.resolvers.XmlPixmapFormatResolver.GLOBAL_PIXMAP_FORMAT_RESOLVER;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class XmlDrawablesParser extends XmlMapResourceParser<net.touchmania.game.resource.lazy.Resource<Drawable>> {
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
    public Map<String, net.touchmania.game.resource.lazy.Resource<Drawable>> parse(XmlParser.Element root) throws XmlParseException {
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
                XmlParser.Element includeRoot = null;
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
    protected void parseAttributes(String id, net.touchmania.game.resource.lazy.Resource<Drawable> value, XmlParser.Element element) throws XmlParseException {
        for (ObjectMap.Entry<String, String> attribute : element.getAttributes()) {
            String key = attribute.key;
            String val = attribute.value;

            //Skip id attribute
            if(key.equals("id")) continue;

            //Parse attribute
            boolean parsed = false;
            if(value instanceof net.touchmania.game.resource.lazy.DrawableResource && parseAttribute((net.touchmania.game.resource.lazy.DrawableResource)  value, key, val)) parsed = true;
            if(value instanceof net.touchmania.game.resource.lazy.TextureResource && parseAttribute((net.touchmania.game.resource.lazy.TextureResource)   value, key, val)) parsed = true;
            if(value instanceof net.touchmania.game.resource.lazy.RegionResource && parseAttribute((net.touchmania.game.resource.lazy.RegionResource)    value, key, val)) parsed = true;
            if(value instanceof net.touchmania.game.resource.lazy.SpriteResource && parseAttribute((net.touchmania.game.resource.lazy.SpriteResource)    value, key, val)) parsed = true;
            if(value instanceof net.touchmania.game.resource.lazy.NinepatchResource && parseAttribute((net.touchmania.game.resource.lazy.NinepatchResource) value, key, val)) parsed = true;

            if(!parsed)
                throw new XmlParseException(String.format(
                        "Unrecognised attribute with name '%s' and value '%s'!", attribute.key, attribute.value));
        }
    }

    private boolean parseAttribute(net.touchmania.game.resource.lazy.DrawableResource resource, String name, String value) throws XmlParseException {
        switch(name) {
            case "leftWidth":    resource.leftWidth = floatResolver.resolve(value);                               break;
            case "rightWidth":   resource.rightWidth = floatResolver.resolve(value);                              break;
            case "topHeight":    resource.topHeight = floatResolver.resolve(value);                               break;
            case "bottomHeight": resource.bottomHeight = floatResolver.resolve(value);                            break;
            default: return false; //Unrecognised attribute
        }

        return true;
    }

    private boolean parseAttribute(net.touchmania.game.resource.lazy.TextureResource resource, String name, String value) throws XmlParseException {
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

    private boolean parseAttribute(net.touchmania.game.resource.lazy.RegionResource resource, String name, String value) throws XmlParseException {
        switch(name) {
            case "x":      resource.x = dimensionResolver.resolve(value).getIntValue();                           break;
            case "y":      resource.y = dimensionResolver.resolve(value).getIntValue();                           break;
            case "width":  resource.width = dimensionResolver.resolve(value).getIntValue();                       break;
            case "height": resource.height = dimensionResolver.resolve(value).getIntValue();                      break;
            default: return false; //Unrecognised attribute
        }

        return true;
    }

    private boolean parseAttribute(net.touchmania.game.resource.lazy.SpriteResource resource, String name, String value) throws XmlParseException {
        switch(name) { //TODO
            case "x": break;
            default: return false; //Unrecognised attribute
        }

        return true;
    }

    private boolean parseAttribute(net.touchmania.game.resource.lazy.NinepatchResource resource, String name, String value) throws XmlParseException {
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
    protected XmlReferenceValueResolver<net.touchmania.game.resource.lazy.Resource<Drawable>> getResolver(XmlParser.Element element) {
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

    private final XmlValueResolver<Dimension> dimensionResolver;
    private final XmlValueResolver<Boolean> booleanResolver;
    private final XmlValueResolver<Color> colorResolver;
    private final XmlValueResolver<Float> floatResolver;
    private final XmlValueResolver<Integer> integerResolver;
    private final XmlValueResolver<String> stringResolver;
    private final XmlReferenceValueResolver<net.touchmania.game.resource.lazy.Resource<Drawable>> drawableResolver = new XmlDrawableResolver() {
        @Override
        protected String getResourceTypeName() {
            return "drawable";
        }

        @Override
        public net.touchmania.game.resource.lazy.Resource<Drawable> resolveReference(String resourceId) throws XmlParseException {
            net.touchmania.game.resource.lazy.DrawableResource resource = (DrawableResource) getResolvedValues().get(resourceId);
            return resource != null ? resource.copy() : null;
        }

        @Override
        public net.touchmania.game.resource.lazy.Resource<Drawable> resolveValue(String value) throws XmlParseException {
            //<drawable> is used only for referencing. So no need to parse value.
            throw new XmlParseException("Illegal value. Drawable resource value must be a reference!");
        }

        @Override
        public boolean checkReferenceType(String type) {
            switch(type) {
                case "drawable":
                case "texture":
                case "region":
                case "sprite":
                case "ninepatch":
                    return true;
            }
            return false;
        }
    };
    private final XmlReferenceValueResolver<net.touchmania.game.resource.lazy.Resource<Drawable>> spriteResolver = new XmlDrawableResolver() {
        @Override
        public net.touchmania.game.resource.lazy.Resource<Drawable> resolveReference(String resourceId) throws XmlParseException {
            net.touchmania.game.resource.lazy.Resource<Drawable> resource = getResolvedValues().get(resourceId);
            if(resource instanceof net.touchmania.game.resource.lazy.SpriteResource) {
                return ((net.touchmania.game.resource.lazy.SpriteResource)resource).copy();
            }
            throw new XmlParseException(String.format(
                    "Incompatible reference! Trying to cast '%s' to SpriteResource!", resource.getClass().getName()));
        }

        @Override
        public net.touchmania.game.resource.lazy.Resource<Drawable> resolveValue(String value) throws XmlParseException {
            //TODO
            return null;
        }

        @Override
        public boolean checkReferenceType(String type) {
            switch(type) {
                case "drawable":
                case "texture":
                case "region":
                case "sprite":
                    return true;
            }
            return false;
        }
    };
    private final XmlReferenceValueResolver<net.touchmania.game.resource.lazy.Resource<Drawable>> ninepatchResolver = new XmlDrawableResolver() {
        @Override
        public net.touchmania.game.resource.lazy.Resource<Drawable> resolveReference(String resourceId) throws XmlParseException {
            net.touchmania.game.resource.lazy.Resource<Drawable> resource = getResolvedValues().get(resourceId);
            if(resource instanceof net.touchmania.game.resource.lazy.NinepatchResource) {
                return ((net.touchmania.game.resource.lazy.NinepatchResource)resource).copy();
            }
            throw new XmlParseException(String.format(
                    "Incompatible reference! Trying to cast '%s' to NinepatchResource!", resource.getClass().getName()));
        }

        @Override
        public net.touchmania.game.resource.lazy.Resource<Drawable> resolveValue(String value) throws XmlParseException {
            //TODO
            return null;
        }

        @Override
        public boolean checkReferenceType(String type) {
            switch(type) {
                case "drawable":
                case "texture":
                case "region":
                case "ninepatch":
                    return true;
            }
            return false;
        }
    };
    private final XmlReferenceValueResolver<net.touchmania.game.resource.lazy.Resource<Drawable>> textureResolver = new XmlDrawableResolver() {
        @Override
        public net.touchmania.game.resource.lazy.Resource<Drawable> resolveReference(String resourceId) throws XmlParseException {
            net.touchmania.game.resource.lazy.Resource<Drawable> resource = getResolvedValues().get(resourceId);
            if(resource instanceof net.touchmania.game.resource.lazy.TextureResource) {
                return new net.touchmania.game.resource.lazy.TextureResource((net.touchmania.game.resource.lazy.TextureResource)resource);
            }
            throw new XmlParseException(String.format(
                    "Incompatible reference! Trying to cast '%s' to TextureResource!", resource.getClass().getName()));
        }

        @Override
        public net.touchmania.game.resource.lazy.Resource<Drawable> resolveValue(String value) throws XmlParseException {
            if(value == null || value.isEmpty()) {
                throw new XmlParseException("Invalid texture file! File name cannot be null or empty!");
            }
            return new net.touchmania.game.resource.lazy.TextureResource(theme.getTexturePath(value));
        }

        @Override
        public boolean checkReferenceType(String type) {
            switch(type) {
                case "drawable":
                case "texture":
                    return true;
            }
            return false;
        }
    };
    private final XmlReferenceValueResolver<net.touchmania.game.resource.lazy.Resource<Drawable>> regionResolver = new XmlDrawableResolver() {
        @Override
        public net.touchmania.game.resource.lazy.Resource<Drawable> resolveReference(String resourceId) throws XmlParseException {
            net.touchmania.game.resource.lazy.Resource<Drawable> resource = getResolvedValues().get(resourceId);
            if(resource instanceof net.touchmania.game.resource.lazy.RegionResource) {
                return new net.touchmania.game.resource.lazy.RegionResource((net.touchmania.game.resource.lazy.RegionResource)resource);
            }
            if(resource instanceof net.touchmania.game.resource.lazy.TextureResource) {
                return new net.touchmania.game.resource.lazy.RegionResource((net.touchmania.game.resource.lazy.TextureResource)resource);
            }

            throw new XmlParseException(String.format(
                    "Incompatible reference! Trying to convert '%s' to RegionResource!", resource.getClass().getName()));
        }

        @Override
        public Resource<Drawable> resolveValue(String value) throws XmlParseException {
            if(value == null || value.isEmpty()) {
                throw new XmlParseException("Invalid texture file! File name cannot be null or empty!");
            }
            return new net.touchmania.game.resource.lazy.RegionResource(theme.getTexturePath(value));
        }

        @Override
        public boolean checkReferenceType(String type) {
            switch(type) {
                case "drawable":
                case "texture":
                case "region":
                    return true;
            }
            return false;
        }
    };
}
