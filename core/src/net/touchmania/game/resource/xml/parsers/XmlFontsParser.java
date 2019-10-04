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
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.ObjectMap;
import net.touchmania.game.resource.Dimension;
import net.touchmania.game.resource.xml.XmlFontGenerator;
import net.touchmania.game.resource.xml.XmlTheme;
import net.touchmania.game.resource.xml.resolvers.*;
import net.touchmania.game.util.xml.XmlParseException;
import net.touchmania.game.util.xml.XmlParser;
import net.touchmania.game.util.xml.XmlValueResolver;

public class XmlFontsParser extends XmlMapResourceParser<XmlFontGenerator> {
    private final XmlValueResolver<Dimension> dimensionResolver;
    private final XmlValueResolver<Boolean> booleanResolver;
    private final XmlValueResolver<Color> colorResolver;
    private final XmlValueResolver<Float> floatResolver;
    private final XmlValueResolver<Integer> integerResolver;
    private final XmlValueResolver<String> stringResolver;
    private final XmlValueResolver<Texture.TextureFilter> filterResolver;
    private final XmlValueResolver<FreeTypeFontGenerator.Hinting> hintingResolver;
    private final XmlReferenceValueResolver<XmlFontGenerator> fontGeneratorResolver;

    /**
     * Create a resource parser from its file.
     *
     * @param resourceFile the resource file.
     */
    public XmlFontsParser(FileHandle resourceFile, XmlTheme theme) {
        super(resourceFile);

        //Create a new instance for every reference resolver.
        this.dimensionResolver = XmlDimensionResolver.from(theme);
        this.booleanResolver = XmlBooleanResolver.from(theme);
        this.colorResolver = XmlColorResolver.from(theme);
        this.floatResolver = XmlFloatResolver.from(theme);
        this.integerResolver = XmlIntegerResolver.from(theme);
        this.stringResolver = XmlStringResolver.from(theme);
        this.filterResolver = new XmlTextureFilterResolver();
        this.hintingResolver = new XmlHintingResolver();
        this.fontGeneratorResolver = new XmlFontGeneratorResolver();
    }

    @Override
    protected void parseAttributes(String id, XmlFontGenerator value, XmlParser.Element element) throws XmlParseException {
        for (ObjectMap.Entry<String, String> attribute : element.getAttributes()) {
            if (!attribute.key.equals("id") && !parseFontAttribute(value, attribute.key, attribute.value)) {
                throw new XmlParseException(String.format(
                        "Unrecognised attribute with name '%s' and value '%s'!", attribute.key, attribute.value));
            }
        }
    }

    @Override
    protected void checkRoot(XmlParser.Element root) throws XmlParseException {
        if(!root.getName().equals("fonts")) {
            throw new XmlParseException("Unexpected xml root element name. Expected to be 'fonts'!");
        }
    }

    @Override
    protected void checkRootChild(XmlParser.Element element) throws XmlParseException {
        if(!element.getName().equals("font")) {
            throw new XmlParseException(String.format("Unexpected element name '%s'! Expected to be 'font'!", element.getName()));
        }
    }

    @Override
    protected XmlReferenceValueResolver<XmlFontGenerator> getResolver(XmlParser.Element element) {
        return fontGeneratorResolver;
    }

    /**
     * Parses the font element's attribute with the given name and value.
     * @param generator the result generator.
     * @param name the attribute name.
     * @param value the attribute value.
     * @return true if the attribute has been recognised and parsed, false if it has not been recognised.
     * @throws XmlParseException if the attribute has been recognised but cannot be parsed correctly.
     */
    private boolean parseFontAttribute(XmlFontGenerator generator, String name, String value) throws XmlParseException {
        FreeTypeFontGenerator.FreeTypeFontParameter p = generator.parameter;
        switch(name) {
            case "size":           p.size = dimensionResolver.resolve(value).getIntValue();                      break;
            case "mono":           p.mono = booleanResolver.resolve(value);                                      break;
            case "color":          p.color = colorResolver.resolve(value);                                       break;
            case "gamma":          p.gamma = floatResolver.resolve(value);                                       break;
            case "renderCount":    p.renderCount = integerResolver.resolve(value);                               break;
            case "borderWidth":    p.borderWidth = dimensionResolver.resolve(value).getValue();                  break;
            case "borderColor":    p.borderColor = colorResolver.resolve(value);                                 break;
            case "borderStraight": p.borderStraight = booleanResolver.resolve(value);                            break;
            case "borderGamma":    p.borderGamma = floatResolver.resolve(value);                                 break;
            case "shadowOffsetX":  p.shadowOffsetX = dimensionResolver.resolve(value).getIntValue();             break;
            case "shadowOffsetY":  p.shadowOffsetY = dimensionResolver.resolve(value).getIntValue();             break;
            case "shadowColor":    p.shadowColor = colorResolver.resolve(value);                                 break;
            case "spaceX":         p.spaceX = dimensionResolver.resolve(value).getIntValue();                    break;
            case "spaceY":         p.spaceY = dimensionResolver.resolve(value).getIntValue();                    break;
            case "kerning":        p.kerning = booleanResolver.resolve(value);                                   break;
            case "flip":           p.flip = booleanResolver.resolve(value);                                      break;
            case "genMipMaps":     p.genMipMaps = booleanResolver.resolve(value);                                break;
            case "incremental":    p.incremental = booleanResolver.resolve(value);                               break;
            case "minFilter":      p.minFilter = filterResolver.resolve(value);                                  break;
            case "magFilter":      p.magFilter = filterResolver.resolve(value);                                  break;
            case "characters":     p.characters = stringResolver.resolve(value);                                 break;
            case "hinting":        p.hinting = hintingResolver.resolve(value);                                   break;
            default: return false; //Unrecognised font attribute
        }
        return true;
    }

    private class XmlFontGeneratorResolver extends XmlReferenceValueResolver<XmlFontGenerator> {
        @Override
        protected String getResourceTypeName() {
            return "font";
        }

        /*
         * Referencing a declared font is like extending it.
         * Attributes of the extended font can be overridden.*/
        @Override
        public XmlFontGenerator resolveReference(String resourceId) {
            //Font definition is extending another declared font
            XmlFontGenerator generator = getResolvedValues().get(resourceId);
            return generator != null ? generator.copy() : null;
        }

        @Override
        public XmlFontGenerator resolveValue(String value) throws XmlParseException {
            if(value == null || value.isEmpty()) {
                throw new XmlParseException("Invalid font file! File name cannot be null or empty!");
            }
            return new XmlFontGenerator(getResourceFile().sibling(value));
        }
    }

    private static class XmlTextureFilterResolver implements XmlValueResolver<Texture.TextureFilter> {
        @Override
        public Texture.TextureFilter resolve(String value) throws XmlParseException {
            if(value == null || value.isEmpty()) {
                throw new XmlParseException("Invalid filter value! Value cannot be null or empty!");
            }

            switch(value.trim().toLowerCase()) {
                case "nearest": return Texture.TextureFilter.Nearest;
                case "linear": return Texture.TextureFilter.Linear;
                case "mipmap": return Texture.TextureFilter.MipMap;
                case "mipmapnearestnearest": return Texture.TextureFilter.MipMapNearestNearest;
                case "mipmapnearestlinear": return Texture.TextureFilter.MipMapNearestLinear;
                case "mipmaplinearlinear": return Texture.TextureFilter.MipMapLinearLinear;
                case "mipmaplinearnearest": return Texture.TextureFilter.MipMapLinearNearest;
            }

            throw new XmlParseException(String.format("Invalid align format for value '%s'!", value));
        }
    }

    private static class XmlHintingResolver implements XmlValueResolver<FreeTypeFontGenerator.Hinting> {
        @Override
        public FreeTypeFontGenerator.Hinting resolve(String value) throws XmlParseException {
            if(value == null || value.isEmpty()) {
                throw new XmlParseException("Invalid hinting value! Value cannot be null or empty!");
            }

            switch(value.trim().toLowerCase()) {
                case "none": return FreeTypeFontGenerator.Hinting.None;
                case "slight": return FreeTypeFontGenerator.Hinting.Slight;
                case "medium": return FreeTypeFontGenerator.Hinting.Medium;
                case "full": return FreeTypeFontGenerator.Hinting.Full;
                case "autoslight": return FreeTypeFontGenerator.Hinting.AutoSlight;
                case "automedium": return FreeTypeFontGenerator.Hinting.AutoMedium;
                case "autofull": return FreeTypeFontGenerator.Hinting.AutoFull;
            }

            throw new XmlParseException(String.format("Invalid align format for value '%s'!", value));
        }
    }
}
