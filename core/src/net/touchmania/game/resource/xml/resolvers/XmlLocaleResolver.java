package net.touchmania.game.resource.xml.resolvers;

import net.touchmania.game.util.xml.XmlParseException;
import net.touchmania.game.util.xml.XmlValueResolver;

import java.util.Locale;

public class XmlLocaleResolver implements XmlValueResolver<Locale> {
    public static final XmlLocaleResolver GLOBAL_LOCALE_RESOLVER = new XmlLocaleResolver();

    @Override
    public Locale resolve(String value) throws XmlParseException {
        if(value == null || value.isEmpty()) {
            throw new XmlParseException("Invalid locale value! Value cannot be null or empty!");
        }

        //Prepare the value for parsing by removing leading/trailing spaces
        value = value.trim();

        if(value.contains("_")) {
            String parts[] = value.split("_");
            String lang = parts[0];
            String country = parts[1];
            return new Locale(lang, country);
        }

        return new Locale(value);
    }
}
