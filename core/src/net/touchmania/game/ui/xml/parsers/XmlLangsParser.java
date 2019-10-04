package net.touchmania.game.ui.xml.parsers;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import net.touchmania.game.ui.xml.resolvers.XmlBooleanResolver;
import net.touchmania.game.ui.xml.resolvers.XmlLocaleResolver;
import net.touchmania.game.util.xml.XmlParseException;
import net.touchmania.game.util.xml.XmlParser;

import java.util.Locale;

public class XmlLangsParser extends XmlResourceParser<Array<Locale>>{
    /**
     * Create a resource parser from its file.
     *
     * @param resourceFile the resource file.
     */
    public XmlLangsParser(FileHandle resourceFile) {
        super(resourceFile);
    }

    @Override
    public Array<Locale> parse(XmlParser.Element root) throws XmlParseException {
        Array<Locale> langs = new Array<>();

        for(int i = 0; i < root.getChildCount(); i++) {
            XmlParser.Element element = root.getChild(i);

            //Check root child name
            if(!element.getName().equals("lang")) {
                throw new XmlParseException(
                        String.format("Unexpected element name '%s'! Expected to be 'lang'!", element.getName()));
            }

            //Parse lang locale
            Locale lang = XmlLocaleResolver.GLOBAL_LOCALE_RESOLVER.resolve(element.getAttribute("locale"));

            //Check if lang is default
            boolean isDefault =
                    element.hasAttribute("default") &&
                    XmlBooleanResolver.GLOBAL_BOOLEAN_RESOLVER.resolve(element.getAttribute("default"));

            //Check for duplicates
            if(langs.contains(lang, false)) {
                throw new XmlParseException(
                        String.format("A language with the same locale '%s' has been already declared!", element.getAttribute("locale")));
            }

            //Add lang to the array of theme supported languages
            langs.add(lang);

            //Put default lang on array first position
            if(isDefault && langs.size > 1) {
                langs.swap(0, langs.size - 1);
            }
        }

        //Returning null means that the theme doesn't support other languages than the ones defined into the fallback theme
        return langs.size == 0 ? null : langs;
    }

    @Override
    protected void checkRoot(XmlParser.Element root) throws XmlParseException {
        if(!root.getName().equals("langs")){
            throw new XmlParseException("Unexpected xml root element name. Expected to be 'langs'");
        }
    }
}
