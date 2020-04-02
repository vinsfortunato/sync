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

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import net.sync.game.resource.Dimension;
import net.sync.game.resource.lazy.DrawableResource;
import net.sync.game.resource.xml.XmlReferenceNotCompatibleException;
import net.sync.game.resource.xml.XmlReferenceNotFoundException;
import net.sync.game.resource.xml.XmlTheme;
import net.sync.game.resource.xml.resolvers.XmlDimensionResolver;
import net.sync.game.util.xml.XmlParseException;
import net.sync.game.util.xml.XmlParser;
import net.sync.game.util.xml.XmlValueResolver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static net.sync.game.resource.xml.resolvers.XmlPixmapFormatResolver.GLOBAL_PIXMAP_FORMAT_RESOLVER;
import static net.sync.game.resource.xml.resolvers.XmlTextureFilterResolver.GLOBAL_TEXTURE_FILTER_RESOLVER;
import static net.sync.game.resource.xml.resolvers.XmlTextureWrapResolver.GLOBAL_TEXTURE_WRAP_RESOLVER;

public class XmlDrawablesParser extends XmlMapResourceParser<net.sync.game.resource.lazy.Resource<Drawable>> {
    private final net.sync.game.resource.xml.XmlTheme theme;

    /**
     * Create a resource parser from its file.
     * @param resourceFile the resource file.
     */
    public XmlDrawablesParser(FileHandle resourceFile, XmlTheme theme) {
        super(resourceFile);
        this.theme = theme;

        //Init resolvers
        this.dimensionResolver = XmlDimensionResolver.from(theme);
        this.booleanResolver = net.sync.game.resource.xml.resolvers.XmlBooleanResolver.from(theme);
        this.colorResolver = net.sync.game.resource.xml.resolvers.XmlColorResolver.from(theme);
        this.floatResolver = net.sync.game.resource.xml.resolvers.XmlFloatResolver.from(theme);
        this.integerResolver = net.sync.game.resource.xml.resolvers.XmlIntegerResolver.from(theme);
        this.stringResolver = net.sync.game.resource.xml.resolvers.XmlStringResolver.from(theme);
    }

    @Override
    public Map<String, net.sync.game.resource.lazy.Resource<Drawable>> parse(XmlParser.Element root) throws XmlParseException {
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
    protected void parseAttributes(String id, net.sync.game.resource.lazy.Resource<Drawable> value, XmlParser.Element element) throws XmlParseException {
        for (ObjectMap.Entry<String, String> attribute : element.getAttributes()) {
            String key = attribute.key;
            String val = attribute.value;

            //Skip id attribute
            if(key.equals("id")) continue;

            //Parse attribute
            boolean parsed = false;
            if(value instanceof net.sync.game.resource.lazy.DrawableResource && parseAttribute((net.sync.game.resource.lazy.DrawableResource)  value, key, val)) parsed = true;
            if(value instanceof net.sync.game.resource.lazy.TextureResource && parseAttribute((net.sync.game.resource.lazy.TextureResource)   value, key, val)) parsed = true;
            if(value instanceof net.sync.game.resource.lazy.RegionResource && parseAttribute((net.sync.game.resource.lazy.RegionResource)    value, key, val)) parsed = true;
            if(value instanceof net.sync.game.resource.lazy.SpriteResource && parseAttribute((net.sync.game.resource.lazy.SpriteResource)    value, key, val)) parsed = true;
            if(value instanceof net.sync.game.resource.lazy.NinepatchResource && parseAttribute((net.sync.game.resource.lazy.NinepatchResource) value, key, val)) parsed = true;

            if(!parsed)
                throw new XmlParseException(String.format(
                        "Unrecognised attribute with name '%s' and value '%s'!", attribute.key, attribute.value));
        }
    }

    private boolean parseAttribute(net.sync.game.resource.lazy.DrawableResource resource, String name, String value) throws XmlParseException {
        switch(name) {
            case "leftWidth":    resource.leftWidth = floatResolver.resolve(value);                               break;
            case "rightWidth":   resource.rightWidth = floatResolver.resolve(value);                              break;
            case "topHeight":    resource.topHeight = floatResolver.resolve(value);                               break;
            case "bottomHeight": resource.bottomHeight = floatResolver.resolve(value);                            break;
            default: return false; //Unrecognised attribute
        }

        return true;
    }

    private boolean parseAttribute(net.sync.game.resource.lazy.TextureResource resource, String name, String value) throws XmlParseException {
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

    private boolean parseAttribute(net.sync.game.resource.lazy.RegionResource resource, String name, String value) throws XmlParseException {
        switch(name) {
            case "x":      resource.x = dimensionResolver.resolve(value).getIntValue();                           break;
            case "y":      resource.y = dimensionResolver.resolve(value).getIntValue();                           break;
            case "width":  resource.width = dimensionResolver.resolve(value).getIntValue();                       break;
            case "height": resource.height = dimensionResolver.resolve(value).getIntValue();                      break;
            default: return false; //Unrecognised attribute
        }

        return true;
    }

    private boolean parseAttribute(net.sync.game.resource.lazy.SpriteResource resource, String name, String value) throws XmlParseException {
        switch(name) { //TODO
            case "x": break;
            default: return false; //Unrecognised attribute
        }

        return true;
    }

    private boolean parseAttribute(net.sync.game.resource.lazy.NinepatchResource resource, String name, String value) throws XmlParseException {
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
    protected net.sync.game.resource.xml.resolvers.XmlReferenceResolver<net.sync.game.resource.lazy.Resource<Drawable>> getResolver(XmlParser.Element element) {
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
    private final net.sync.game.resource.xml.resolvers.XmlReferenceResolver<net.sync.game.resource.lazy.Resource<Drawable>> drawableResolver = new net.sync.game.resource.xml.resolvers.XmlDrawableResolver() {
        @Override
        public net.sync.game.resource.lazy.Resource<Drawable> resolveReference(String resourceId) throws net.sync.game.resource.xml.XmlReferenceNotFoundException, net.sync.game.resource.xml.XmlReferenceNotCompatibleException {
            net.sync.game.resource.lazy.Resource<Drawable> resource = getResolvedValueOrThrow(resourceId);

            if(resource instanceof net.sync.game.resource.lazy.DrawableResource) {
                return ((net.sync.game.resource.lazy.DrawableResource)resource).copy();
            }

            throw net.sync.game.resource.xml.XmlReferenceNotCompatibleException.incompatibleType(resource.getClass(), DrawableResource.class);
        }

        @Override
        public net.sync.game.resource.lazy.Resource<Drawable> resolveValue(String value) throws XmlParseException {
            //<drawable> is used only for referencing. So no need to parse value.
            throw new XmlParseException("Illegal value. Drawable resource value must be a reference!");
        }

        @Override
        public void checkReferenceType(String type) throws net.sync.game.resource.xml.XmlReferenceNotCompatibleException {
            switch(type) {
                case "drawable":
                case "texture":
                case "region":
                case "sprite":
                case "ninepatch":
                    return;
            }
            throw new net.sync.game.resource.xml.XmlReferenceNotCompatibleException("Incompatible drawable reference type!");
        }
    };

    /* Sprite resolver */
    private final net.sync.game.resource.xml.resolvers.XmlReferenceResolver<net.sync.game.resource.lazy.Resource<Drawable>> spriteResolver = new net.sync.game.resource.xml.resolvers.XmlDrawableResolver() {
        @Override
        public net.sync.game.resource.lazy.Resource<Drawable> resolveReference(String resourceId) throws net.sync.game.resource.xml.XmlReferenceNotFoundException, net.sync.game.resource.xml.XmlReferenceNotCompatibleException {
            net.sync.game.resource.lazy.Resource<Drawable> resource = getResolvedValueOrThrow(resourceId);

            if(resource instanceof net.sync.game.resource.lazy.SpriteResource)
                return ((net.sync.game.resource.lazy.SpriteResource)resource).copy();

            throw net.sync.game.resource.xml.XmlReferenceNotCompatibleException.incompatibleType(resource.getClass(), net.sync.game.resource.lazy.SpriteResource.class);
        }

        @Override
        public net.sync.game.resource.lazy.Resource<Drawable> resolveValue(String value) throws XmlParseException {
            //TODO
            return null;
        }

        @Override
        public void checkReferenceType(String type) throws net.sync.game.resource.xml.XmlReferenceNotCompatibleException {
            switch(type) {
                case "drawable":
                case "texture":
                case "region":
                case "sprite":
                    return;
            }
            throw new net.sync.game.resource.xml.XmlReferenceNotCompatibleException("Incompatible sprite reference type!");
        }
    };

    /* Ninepatch Resolver */
    private final net.sync.game.resource.xml.resolvers.XmlReferenceResolver<net.sync.game.resource.lazy.Resource<Drawable>> ninepatchResolver = new net.sync.game.resource.xml.resolvers.XmlDrawableResolver() {
        @Override
        public net.sync.game.resource.lazy.Resource<Drawable> resolveReference(String resourceId) throws net.sync.game.resource.xml.XmlReferenceNotFoundException, net.sync.game.resource.xml.XmlReferenceNotCompatibleException {
            net.sync.game.resource.lazy.Resource<Drawable> resource = getResolvedValueOrThrow(resourceId);

            if(resource instanceof net.sync.game.resource.lazy.NinepatchResource)
                return ((net.sync.game.resource.lazy.NinepatchResource)resource).copy();

            throw net.sync.game.resource.xml.XmlReferenceNotCompatibleException.incompatibleType(resource.getClass(), net.sync.game.resource.lazy.NinepatchResource.class);
        }

        @Override
        public net.sync.game.resource.lazy.Resource<Drawable> resolveValue(String value) throws XmlParseException {
            //TODO
            return null;
        }

        @Override
        public void checkReferenceType(String type) throws net.sync.game.resource.xml.XmlReferenceNotCompatibleException {
            switch(type) {
                case "drawable":
                case "texture":
                case "region":
                case "ninepatch":
                    return;
            }
            throw new net.sync.game.resource.xml.XmlReferenceNotCompatibleException("Incompatible ninepatch reference type!");
        }
    };

    /* Texture resolver */
    private final net.sync.game.resource.xml.resolvers.XmlReferenceResolver<net.sync.game.resource.lazy.Resource<Drawable>> textureResolver = new net.sync.game.resource.xml.resolvers.XmlDrawableResolver() {
        @Override
        public net.sync.game.resource.lazy.Resource<Drawable> resolveReference(String resourceId) throws net.sync.game.resource.xml.XmlReferenceNotFoundException, net.sync.game.resource.xml.XmlReferenceNotCompatibleException {
            net.sync.game.resource.lazy.Resource<Drawable> resource = getResolvedValueOrThrow(resourceId);

            if(resource instanceof net.sync.game.resource.lazy.TextureResource)
                return new net.sync.game.resource.lazy.TextureResource((net.sync.game.resource.lazy.TextureResource)resource);

            throw net.sync.game.resource.xml.XmlReferenceNotCompatibleException.incompatibleType(resource.getClass(), net.sync.game.resource.lazy.TextureResource.class);
        }

        @Override
        public net.sync.game.resource.lazy.Resource<Drawable> resolveValue(String value) {
            return new net.sync.game.resource.lazy.TextureResource(theme.getTexturePath(value));
        }

        @Override
        public void checkReferenceType(String type) throws net.sync.game.resource.xml.XmlReferenceNotCompatibleException {
            switch(type) {
                case "drawable":
                case "texture":
                    return;
            }
            throw new net.sync.game.resource.xml.XmlReferenceNotCompatibleException("Incompatible texture reference type!");
        }
    };

    /* Region resolver */
    private final net.sync.game.resource.xml.resolvers.XmlReferenceResolver<net.sync.game.resource.lazy.Resource<Drawable>> regionResolver = new net.sync.game.resource.xml.resolvers.XmlDrawableResolver() {
        @Override
        public net.sync.game.resource.lazy.Resource<Drawable> resolveReference(String resourceId) throws XmlReferenceNotFoundException, net.sync.game.resource.xml.XmlReferenceNotCompatibleException {
            net.sync.game.resource.lazy.Resource<Drawable> resource = getResolvedValueOrThrow(resourceId);

            if(resource instanceof net.sync.game.resource.lazy.RegionResource)
                return new net.sync.game.resource.lazy.RegionResource((net.sync.game.resource.lazy.RegionResource)resource);
            if(resource instanceof net.sync.game.resource.lazy.TextureResource)
                return new net.sync.game.resource.lazy.RegionResource((net.sync.game.resource.lazy.TextureResource)resource);

            throw net.sync.game.resource.xml.XmlReferenceNotCompatibleException.incompatibleType(resource.getClass(), net.sync.game.resource.lazy.RegionResource.class);
        }

        @Override
        public net.sync.game.resource.lazy.Resource<Drawable> resolveValue(String value) throws XmlParseException {
            return new net.sync.game.resource.lazy.RegionResource(theme.getTexturePath(value));
        }

        @Override
        public void checkReferenceType(String type) throws net.sync.game.resource.xml.XmlReferenceNotCompatibleException {
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
