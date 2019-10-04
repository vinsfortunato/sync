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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import net.touchmania.game.Game;
import net.touchmania.game.util.FileUtils;
import net.touchmania.game.ui.xml.resolvers.XmlIntegerResolver;
import net.touchmania.game.util.xml.XmlParseException;
import net.touchmania.game.ui.xml.*;
import net.touchmania.game.util.xml.XmlParser;

import java.util.Locale;

public class XmlThemeParser extends XmlResourceParser<XmlTheme> {
    /**
     * Create a resource parser from its file.
     *
     * @param resourceFile the resource file.
     */
    public XmlThemeParser(FileHandle resourceFile) {
        super(resourceFile);
    }

    @Override
    public XmlTheme parse() throws Exception {
        XmlTheme theme = super.parse(); //Parse theme.xml (theme manifest)
        parseColors(theme); //Parse colors.xml (colors resource)
        parseDimens(theme); //Parse dimens.xml (dimensions resource)
        parseValues(theme); //Parse values.xml (values resource)
        parseLangs(theme); //Parse langs.xml (languages resource)
        parseFonts(theme); //Parse fonts.xml (fonts resource)
        parseSounds(theme); //Parse sounds.xml (sounds resource)
        parseStrings(theme); //Parse strings/strings_{locale}.xml based on active/supported locale (strings resources)
        return theme;
    }

    @Override
    public XmlTheme parse(XmlParser.Element root) throws XmlParseException {
        //Parse manifest
        XmlThemeManifest manifest = new XmlThemeManifest();
        manifest.setVersion(XmlIntegerResolver.GLOBAL_INT_RESOLVER.resolve(root.getAttribute("version")));
        manifest.setName(root.getAttribute("name"));
        manifest.setAuthor(root.getAttribute("author"));
        manifest.setWebsite(root.getAttribute("website"));
        manifest.setDescription(root.getAttribute("description"));

        //Create theme object
        XmlTheme theme = new XmlTheme(getResourceFile());
        theme.setManifest(manifest);
        return theme;
    }

    @Override
    protected void checkRoot(XmlParser.Element root) throws XmlParseException {
        if(!root.getName().equals("theme")) {
            throw new XmlParseException("Unexpected xml root element name. Expected to be 'theme'!");
        }
    }

    private void parseColors(XmlTheme theme) throws Exception {
        FileHandle colorsFile = getResourceFile().sibling("colors.xml");
        if(colorsFile.exists()) {
            theme.setColors(new XmlColorsParser(colorsFile).parse());
        }
    }

    private void parseDimens(XmlTheme theme) throws Exception {
        FileHandle dimensFile = getResourceFile().sibling("dimens.xml");
        if(dimensFile.exists()) {
            theme.setDimensions(new XmlDimensParser(dimensFile).parse());
        }
    }
    
    private void parseValues(XmlTheme theme) throws Exception {
        FileHandle valuesFile = getResourceFile().sibling("values.xml");
        if(valuesFile.exists()) {
            theme.setValues(new XmlValuesParser(valuesFile).parse());
        }
    }

    private void parseFonts(XmlTheme theme) throws Exception {
        FileHandle fontsFile = getResourceFile().sibling("fonts.xml");
        if(fontsFile.exists()) {
            theme.setFonts(new XmlFontsParser(fontsFile, theme).parse());
        }
    }

    private void parseSounds(XmlTheme theme) throws Exception {
        FileHandle soundsFile = getResourceFile().sibling("sounds.xml");
        if(soundsFile.exists()) {
            theme.setSounds(new XmlSoundsParser(soundsFile, theme).parse());
        }
    }

    private void parseLangs(XmlTheme theme) throws Exception {
        FileHandle langFile = getResourceFile().sibling("langs.xml");
        if(langFile.exists()) {
            theme.setLanguages(new XmlLangsParser(langFile).parse());
        }
    }

    private void parseStrings(XmlTheme theme) throws Exception {
        Array<Locale> langs = theme.getLanguages();
        //Check theme supported languages. If the array containing theme languages is null no string resource
        //from the theme is parsed and fallback theme will then be used to resolve string references.
        if(langs != null) {
            //Get current game language
            Locale active = Game.instance().getSettings().getLanguage();
            int index = langs.indexOf(active, false);

            //Check if the theme supports the currently active game language
            if(index != -1) {
                //Current active game language is supported by the theme
                //Parse active lang strings
                FileHandle stringsFile = getStringsFile(active);
                FileUtils.checkFilePresence(stringsFile);
                XmlStringsParser parser = new XmlStringsParser(stringsFile);
                ObjectMap<String, String> strings = parser.parse();

                //Parse default language if active isn't already default
                if(index != 0) {
                    stringsFile = getStringsFile(langs.first()); //Get default theme lang
                    FileUtils.checkFilePresence(stringsFile);
                    parser = new XmlStringsParser(stringsFile);
                    ObjectMap<String, String> defStrings = parser.parse();

                    //Merge maps by overriding default lang strings with active lang strings
                    defStrings.putAll(strings);
                    strings = defStrings;
                }

                theme.setStrings(strings);
            } else {
                //Current active game language is not supported by the theme
                //Parse default theme lang strings
                FileHandle stringsFile = getStringsFile(langs.first());
                FileUtils.checkFilePresence(stringsFile);
                XmlStringsParser parser = new XmlStringsParser(stringsFile);
                theme.setStrings(parser.parse());
            }
        }
    }

    private FileHandle getStringsFile(Locale locale) {
        return getResourceFile()
                .sibling("strings")
                .child(String.format("strings_%s_%s.xml", locale.getLanguage(), locale.getCountry()));
    }
}
