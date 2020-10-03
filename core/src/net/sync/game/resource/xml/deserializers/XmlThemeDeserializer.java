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

import com.badlogic.gdx.files.FileHandle;
import net.sync.game.resource.MapTheme;
import net.sync.game.resource.SettableThemeManifest;
import net.sync.game.util.xml.XmlDeserializeException;
import net.sync.game.util.xml.XmlElement;
import net.sync.game.util.xml.XmlParser;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import static net.sync.game.Game.settings;
import static net.sync.game.resource.xml.resolvers.XmlGlobalResolvers.GLOBAL_INTEGER_RESOLVER;

public class XmlThemeDeserializer extends XmlResourceDeserializer<MapTheme> {
    /**
     * Creates a theme resource deserializer.
     * @param parser the XML parser.
     * @param manifestFile the manifest resource file.
     */
    public XmlThemeDeserializer(XmlParser parser, FileHandle manifestFile) {
        super(parser, manifestFile);
    }

    @Override
    public MapTheme deserialize() {
        MapTheme theme = super.deserialize();   //Deserialize theme.xml (theme manifest)
        deserializeLangs(theme);                //Deserialize langs.xml (language resources)
        deserializeValues(theme);               //Deserialize values.xml (value resources)
        deserializeColors(theme);               //Deserialize colors.xml (color resources)
        deserializeDimens(theme);               //Deserialize dimens.xml (dimension resources)
        deserializeStrings(theme);              //Deserialize strings/strings_{locale}.xml based on active locale
        deserializeFonts(theme);                //Deserialize fonts.xml (font resources)
        deserializeSounds(theme);               //Deserialize sounds.xml (sound resources)
        deserializeMusics(theme);               //Deserialize musics.xml (music resources)
        deserializeDrawables(theme);            //Deserialize drawables.xml (drawable resources)
        //deserializeLayouts(theme);            //TODO
        //deserializeStyles(theme);             //TODO
        return theme;
    }

    @Override
    public MapTheme deserialize(XmlElement root) {
        //Deserialize manifest
        SettableThemeManifest manifest = new SettableThemeManifest();
        manifest.setVersion(GLOBAL_INTEGER_RESOLVER.resolve(root.getAttribute("version")));
        manifest.setName(root.getAttribute("name"));
        manifest.setAuthor(root.getAttribute("author"));
        manifest.setWebsite(root.getAttribute("website"));
        manifest.setDescription(root.getAttribute("description"));

        //Create theme object
        MapTheme theme = new MapTheme(getFile());
        theme.setManifest(manifest);
        return theme;
    }

    @Override
    protected void validateRoot(XmlElement root) {
        if(!root.getName().equals("theme")) {
            throw new XmlDeserializeException("Unexpected xml root element name. Expected to be 'theme'!");
        }
    }

    private void deserializeLangs(MapTheme theme) {
        FileHandle langFile = getFile().sibling("langs.xml");
        if(langFile.exists()) {
            theme.setLanguages(new XmlLangsDeserializer(getParser(), langFile, theme).deserialize());
        }
    }

    private void deserializeValues(MapTheme theme) {
        FileHandle valuesFile = getFile().sibling("values.xml");
        if(valuesFile.exists()) {
            theme.setValues(new XmlValuesDeserializer(getParser(), valuesFile, theme).deserialize());
        }
    }

    private void deserializeColors(MapTheme theme) {
        FileHandle colorsFile = getFile().sibling("colors.xml");
        if(colorsFile.exists()) {
            theme.setColors(new XmlColorsDeserializer(getParser(), colorsFile, theme).deserialize());
        }
    }

    private void deserializeDimens(MapTheme theme) {
        FileHandle dimensFile = getFile().sibling("dimens.xml");
        if(dimensFile.exists()) {
            theme.setDimensions(new XmlDimensDeserializer(getParser(), dimensFile, theme).deserialize());
        }
    }

    private void deserializeStrings(MapTheme theme) {
        List<Locale> langs = theme.getLanguages();
        //Check theme supported languages. If the list containing theme languages is null no string resource
        //from the theme is parsed and fallback theme will then be used to resolve string references.
        if(langs != null) {
            //Get current game language
            Locale active = settings().getLanguage();
            int index = langs.indexOf(active);

            //Check if the theme supports the currently active game language
            if(index != -1) {
                //Current active game language is supported by the theme
                //Parse active lang strings
                FileHandle stringsFile = getStringsFile(active);
                existsOrThrow(stringsFile);
                XmlStringsDeserializer deserializer = new XmlStringsDeserializer(getParser(), stringsFile, theme);
                Map<String, String> strings = deserializer.deserialize();

                //Parse default language if active isn't already default
                if(index != 0) {
                    stringsFile = getStringsFile(langs.get(0)); //Get default theme lang
                    existsOrThrow(stringsFile);
                    deserializer = new XmlStringsDeserializer(getParser(), stringsFile, theme);
                    Map<String, String> defStrings = deserializer.deserialize();

                    //Merge maps by overriding default lang strings with active lang strings
                    defStrings.putAll(strings);
                    strings = defStrings;
                }

                theme.setStrings(strings);
            } else {
                //Current active game language is not supported by the theme
                //Parse default theme lang strings
                FileHandle stringsFile = getStringsFile(langs.get(0));
                existsOrThrow(stringsFile);
                XmlStringsDeserializer deserializer = new XmlStringsDeserializer(getParser(), stringsFile, theme);
                theme.setStrings(deserializer.deserialize());
            }
        }
    }

    private void deserializeFonts(MapTheme theme) {
        FileHandle fontsFile = getFile().sibling("fonts.xml");
        if(fontsFile.exists()) {
            theme.setFonts(new XmlFontsDeserializer(fontsFile, theme).deserialize());
        }
    }

    private void deserializeSounds(MapTheme theme) {
        FileHandle soundsFile = getFile().sibling("sounds.xml");
        if(soundsFile.exists()) {
            theme.setSounds(new XmlSoundsDeserializer(getParser(), soundsFile, theme).deserialize());
        }
    }

    private void deserializeMusics(MapTheme theme) {
        FileHandle musicsFile = getFile().sibling("musics.xml");
        if(musicsFile.exists()) {
            theme.setMusics(new XmlMusicsDeserializer(getParser(), musicsFile, theme).deserialize());
        }
    }

    private void deserializeDrawables(MapTheme theme) {
        FileHandle drawablesFile = getFile().sibling("drawables.xml");
        if(drawablesFile.exists()) {
            theme.setDrawables(new XmlDrawablesDeserializer(drawablesFile, theme).deserialize());
        }
    }

    private FileHandle getStringsFile(Locale locale) {
        return getFile()
                .sibling("strings")
                .child(String.format("strings_%s_%s.xml", locale.getLanguage(), locale.getCountry()));
    }

    private static void existsOrThrow(FileHandle file) {
        if(!file.exists()) {
            throw new XmlDeserializeException(String.format("Required file '%s' not found!", file.path()));
        }
    }
}
