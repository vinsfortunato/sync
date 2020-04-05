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
import net.sync.game.Game;
import net.sync.game.resource.xml.XmlTheme;
import net.sync.game.resource.xml.XmlThemeManifest;
import net.sync.game.resource.xml.resolvers.XmlIntegerResolver;
import net.sync.game.util.xml.XmlParseException;
import net.sync.game.util.xml.XmlParser;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class XmlThemeParser extends XmlResourceParser<XmlTheme> {
    /**
     * Create a theme parser from its manifest file.
     * @param resourceFile the theme manifest file.
     */
    public XmlThemeParser(FileHandle resourceFile) {
        super(resourceFile);
    }

    @Override
    public XmlTheme parse() throws Exception {
        XmlTheme theme = super.parse(); //Parse theme.xml (theme manifest)
        parseLangs(theme);              //Parse langs.xml (language resources)
        parseValues(theme);             //Parse values.xml (value resources)
        parseColors(theme);             //Parse colors.xml (color resources)
        parseDimens(theme);             //Parse dimens.xml (dimension resources)
        parseStrings(theme);            //Parse strings/strings_{locale}.xml based on active locale (strings resources)
        parseFonts(theme);              //Parse fonts.xml (font resources)
        parseSounds(theme);             //Parse sounds.xml (sound resources)
        parseMusics(theme);             //Parse musics.xml (music resources)
        parseDrawables(theme);          //Parse drawables.xml (drawable resources)
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

    private void parseLangs(XmlTheme theme) throws Exception {
        FileHandle langFile = getResourceFile().sibling("langs.xml");
        if(langFile.exists()) {
            theme.setLanguages(new XmlLangsParser(langFile, theme).parse());
        }
    }

    private void parseValues(XmlTheme theme) throws Exception {
        FileHandle valuesFile = getResourceFile().sibling("values.xml");
        if(valuesFile.exists()) {
            theme.setValues(new XmlValuesParser(valuesFile, theme).parse());
        }
    }

    private void parseColors(XmlTheme theme) throws Exception {
        FileHandle colorsFile = getResourceFile().sibling("colors.xml");
        if(colorsFile.exists()) {
            theme.setColors(new XmlColorsParser(colorsFile, theme).parse());
        }
    }

    private void parseDimens(XmlTheme theme) throws Exception {
        FileHandle dimensFile = getResourceFile().sibling("dimens.xml");
        if(dimensFile.exists()) {
            theme.setDimensions(new XmlDimensParser(dimensFile, theme).parse());
        }
    }

    private void parseStrings(XmlTheme theme) throws Exception {
        List<Locale> langs = theme.getLanguages();
        //Check theme supported languages. If the array containing theme languages is null no string resource
        //from the theme is parsed and fallback theme will then be used to resolve string references.
        if(langs != null) {
            //Get current game language
            Locale active = Game.instance().getSettings().getLanguage();
            int index = langs.indexOf(active);

            //Check if the theme supports the currently active game language
            if(index != -1) {
                //Current active game language is supported by the theme
                //Parse active lang strings
                FileHandle stringsFile = getStringsFile(active);
                existsOrThrow(stringsFile);
                XmlStringsParser parser = new XmlStringsParser(stringsFile, theme);
                Map<String, String> strings = parser.parse();

                //Parse default language if active isn't already default
                if(index != 0) {
                    stringsFile = getStringsFile(langs.get(0)); //Get default theme lang
                    existsOrThrow(stringsFile);
                    parser = new XmlStringsParser(stringsFile, theme);
                    Map<String, String> defStrings = parser.parse();

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
                XmlStringsParser parser = new XmlStringsParser(stringsFile, theme);
                theme.setStrings(parser.parse());
            }
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

    private void parseMusics(XmlTheme theme) throws Exception {
        FileHandle musicsFile = getResourceFile().sibling("musics.xml");
        if(musicsFile.exists()) {
            theme.setMusics(new XmlMusicsParser(musicsFile, theme).parse());
        }
    }

    private void parseDrawables(XmlTheme theme) throws Exception {
        FileHandle drawablesFile = getResourceFile().sibling("drawables.xml");
        if(drawablesFile.exists()) {
            theme.setDrawables(new XmlDrawablesParser(drawablesFile, theme).parse());
        }
    }

    private FileHandle getStringsFile(Locale locale) {
        return getResourceFile()
                .sibling("strings")
                .child(String.format("strings_%s_%s.xml", locale.getLanguage(), locale.getCountry()));
    }

    private static void existsOrThrow(FileHandle file) throws FileNotFoundException {
        if(!file.exists()) {
            throw new FileNotFoundException(String.format("Required file '%s' not found!", file.path()));
        }
    }
}
