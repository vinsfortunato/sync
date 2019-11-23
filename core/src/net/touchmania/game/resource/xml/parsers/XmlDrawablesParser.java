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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import net.touchmania.game.resource.Dimension;
import net.touchmania.game.resource.xml.*;
import net.touchmania.game.resource.xml.resolvers.*;
import net.touchmania.game.util.xml.XmlParseException;
import net.touchmania.game.util.xml.XmlParser;
import net.touchmania.game.util.xml.XmlValueResolver;
import static net.touchmania.game.resource.xml.resolvers.XmlTextureFilterResolver.GLOBAL_TEXTURE_FILTER_RESOLVER;
import static net.touchmania.game.resource.xml.resolvers.XmlTextureWrapResolver.GLOBAL_TEXTURE_WRAP_RESOLVER;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class XmlDrawablesParser extends XmlMapResourceParser<XmlDrawableLoader> {
    private final XmlValueResolver<Dimension> dimensionResolver;
    private final XmlValueResolver<Boolean> booleanResolver;
    private final XmlValueResolver<Color> colorResolver;
    private final XmlValueResolver<Float> floatResolver;
    private final XmlValueResolver<Integer> integerResolver;
    private final XmlValueResolver<String> stringResolver;
    private final XmlReferenceValueResolver<XmlDrawableLoader> drawableResolver;
    private final XmlReferenceValueResolver<XmlDrawableLoader> spriteResolver;
    private final XmlReferenceValueResolver<XmlDrawableLoader> ninepatchResolver;
    private final XmlReferenceValueResolver<XmlDrawableLoader> textureResolver;
    private final XmlReferenceValueResolver<XmlDrawableLoader> regionResolver;

    /**
     * Create a resource parser from its file.
     * @param resourceFile the resource file.
     */
    public XmlDrawablesParser(FileHandle resourceFile, XmlTheme theme) {
        super(resourceFile);

        //Init resolvers
        this.dimensionResolver = XmlDimensionResolver.from(theme);
        this.booleanResolver = XmlBooleanResolver.from(theme);
        this.colorResolver = XmlColorResolver.from(theme);
        this.floatResolver = XmlFloatResolver.from(theme);
        this.integerResolver = XmlIntegerResolver.from(theme);
        this.stringResolver = XmlStringResolver.from(theme);
        this.drawableResolver = new XmlDrawableLoaderResolver();
        this.spriteResolver = new XmlSpriteDrawableLoaderResolver();
        this.ninepatchResolver = new XmlNinepatchDrawableLoaderResolver();
        this.textureResolver = new XmlTextureDrawableLoaderResolver();
        this.regionResolver = new XmlRegionDrawableLoaderResolver();
    }

    @Override
    public Map<String, XmlDrawableLoader> parse(XmlParser.Element root) throws XmlParseException {
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
            increasedSize += elems.size;
            children.removeIndex(i + increasedSize);
            increasedSize -= 1;
        }
    }

    @Override
    protected void parseAttributes(String id, XmlDrawableLoader value, XmlParser.Element element) throws XmlParseException {
        for (ObjectMap.Entry<String, String> attribute : element.getAttributes()) {
            //Skip id attribute
            if(attribute.key.equals("id")) continue;

            //Parse attribute
            if(value instanceof XmlTextureLoader && parseAttribute((XmlTextureLoader) value, attribute.key, attribute.value)) continue;
            if(value instanceof XmlRegionLoader  && parseAttribute((XmlRegionLoader)  value, attribute.key, attribute.value)) continue;

            throw new XmlParseException(String.format(
                    "Unrecognised attribute with name '%s' and value '%s'!", attribute.key, attribute.value));
        }
    }

    private boolean parseAttribute(XmlTextureLoader loader, String name, String value) throws XmlParseException {
        switch(name) {
            case "minFilter": loader.minFilter = GLOBAL_TEXTURE_FILTER_RESOLVER.resolve(value);                 break;
            case "maxFilter": loader.magFilter = GLOBAL_TEXTURE_FILTER_RESOLVER.resolve(value);                 break;
            case "uWrap":     loader.uWrap = GLOBAL_TEXTURE_WRAP_RESOLVER.resolve(value);                       break;
            case "vWrap":     loader.vWrap = GLOBAL_TEXTURE_WRAP_RESOLVER.resolve(value);                       break;
            default: return false; //Unrecognised attribute
        }

        return true;
    }

    private boolean parseAttribute(XmlRegionLoader loader, String name, String value) throws XmlParseException {
        switch(name) {
            case "x": loader.x = dimensionResolver.resolve(value).getIntValue();                                break;
            case "y": loader.y = dimensionResolver.resolve(value).getIntValue();                                break;
            case "width": loader.width = dimensionResolver.resolve(value).getIntValue();                        break;
            case "height": loader.height = dimensionResolver.resolve(value).getIntValue();                      break;
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
            case "sprite":
            case "ninepatch":
            case "texture":
            case "region":
            case "drawable":
                return;
            default:
                throw new XmlParseException(String.format("Unexpected element name '%s'!", element.getName()));
        }
    }

    @Override
    protected XmlReferenceValueResolver<XmlDrawableLoader> getResolver(XmlParser.Element element) {
        switch (element.getName()) {
            case "sprite":
                return spriteResolver;
            case "ninepatch":
                return ninepatchResolver;
            case "texture":
                return textureResolver;
            case "region":
                return regionResolver;
            case "drawable":
                return drawableResolver;
            default:
                throw new IllegalArgumentException("Unexpected element name!");
        }
    }

    private class XmlDrawableLoaderResolver extends XmlReferenceValueResolver<XmlDrawableLoader> {
        @Override
        protected String getResourceTypeName() {
            return "drawable";
        }

        @Override
        public XmlDrawableLoader resolveReference(String resourceId) throws XmlParseException {
            XmlDrawableLoader loader = getResolvedValues().get(resourceId);
            return loader != null ? loader.copy() : null;
        }

        @Override
        public XmlDrawableLoader resolveValue(String value) throws XmlParseException {
            //<drawable> is used only for referencing. So no need to parse value.
            throw new XmlParseException("Unexpected behavior.");
        }
    }

    private class XmlTextureDrawableLoaderResolver extends XmlDrawableLoaderResolver {
        @Override
        public XmlDrawableLoader resolveReference(String resourceId) throws XmlParseException {
            return null;
        }

        @Override
        public XmlDrawableLoader resolveValue(String value) throws XmlParseException {
            return null;
        }
    }

    private class XmlSpriteDrawableLoaderResolver extends XmlDrawableLoaderResolver {
        @Override
        public XmlDrawableLoader resolveReference(String resourceId) throws XmlParseException {
            return null;
        }

        @Override
        public XmlDrawableLoader resolveValue(String value) throws XmlParseException {
            return null;
        }
    }

    private class XmlNinepatchDrawableLoaderResolver extends XmlDrawableLoaderResolver {
        @Override
        public XmlDrawableLoader resolveReference(String resourceId) throws XmlParseException {
            return null;
        }

        @Override
        public XmlDrawableLoader resolveValue(String value) throws XmlParseException {
            return null;
        }
    }

    private class XmlRegionDrawableLoaderResolver extends XmlDrawableLoaderResolver {
        @Override
        public XmlDrawableLoader resolveReference(String resourceId) throws XmlParseException {
            return null;
        }

        @Override
        public XmlDrawableLoader resolveValue(String value) throws XmlParseException {
            return null;
        }
    }
}
