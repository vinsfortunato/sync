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
import com.badlogic.gdx.utils.Array;
import net.touchmania.game.resource.xml.*;
import net.touchmania.game.resource.xml.resolvers.XmlReferenceValueResolver;
import net.touchmania.game.util.xml.XmlParseException;
import net.touchmania.game.util.xml.XmlParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class XmlDrawableParser  extends XmlMapResourceParser<XmlDrawableLoader> {
    private XmlReferenceValueResolver<XmlDrawableLoader> drawableResolver = new XmlDrawableLoaderResolver();
    private XmlReferenceValueResolver<XmlDrawableLoader> spriteResolver = new XmlSpriteDrawableLoaderResolver();
    private XmlReferenceValueResolver<XmlDrawableLoader> ninepatchResolver = new XmlNinepatchDrawableLoaderResolver();
    private XmlReferenceValueResolver<XmlDrawableLoader> textureResolver = new XmlTextureDrawableLoaderResolver();
    private XmlReferenceValueResolver<XmlDrawableLoader> regionResolver = new XmlRegionDrawableLoaderResolver();

    /**
     * Create a resource parser from its file.
     * @param resourceFile the resource file.
     */
    public XmlDrawableParser(FileHandle resourceFile, XmlTheme theme) {
        super(resourceFile);
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

        /* Referencing a declared drawable is like extending it.
         * Attributes of the extended drawable can be overridden.*/
        @Override
        public XmlDrawableLoader resolveReference(String resourceId) throws XmlParseException {
            //Drawable definition is extending another predeclared drawable
            XmlDrawableLoader loader = getResolvedValues().get(resourceId);
            return loader != null ? loader.copy() : null;
        }

        @Override
        public XmlDrawableLoader resolveValue(String value) throws XmlParseException {
            //TODO.. <drawable> is used only for referencing. So no need to parse value.
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
