package net.touchmania.game.resource;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import net.touchmania.game.resource.xml.XmlTheme;
import net.touchmania.game.resource.xml.parsers.XmlThemeParser;

public class ThemeManager {

    private Theme activeTheme;

    public ThemeManager() {
        FileHandle manifestFile = Gdx.files.internal("theme/default/theme.xml");
        XmlThemeParser parser = new XmlThemeParser(manifestFile);
        try {
            activeTheme = parser.parse();
            //((XmlTheme)activeTheme).test();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public Theme getActiveTheme() {
        return activeTheme;
    }
}
