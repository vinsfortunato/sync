package net.touchmania.game.resource;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import net.touchmania.game.util.ui.TexturePath;

import java.util.List;
import java.util.Locale;

public class ThemeManager {

    private Theme activeTheme;

    {
        //TODO
        activeTheme = new Theme() {
            @Override
            public Theme getFallbackTheme() {
                return null;
            }

            @Override
            public boolean hasFallbackTheme() {
                return false;
            }

            @Override
            public ThemeManifest getManifest() {
                return null;
            }

            @Override
            public List<Locale> getLanguages() {
                return null;
            }

            @Override
            public TexturePath getTexturePath(String path) {
                return null;
            }

            @Override
            public Layout getLayout(String id) {
                return null;
            }

            @Override
            public Style getStyle(String id) {
                return null;
            }

            @Override
            public Drawable getDrawable(String id) {
                return null;
            }

            @Override
            public Color getColor(String id) {
                return null;
            }

            @Override
            public Dimension getDimension(String id) {
                return null;
            }

            @Override
            public BitmapFont getFont(String id) {
                return null;
            }

            @Override
            public Sound getSound(String id) {
                return null;
            }

            @Override
            public Music getMusic(String id) {
                return null;
            }

            @Override
            public String getString(String id) {
                return null;
            }

            @Override
            public Integer getInt(String id) {
                return null;
            }

            @Override
            public Float getFloat(String id) {
                return null;
            }

            @Override
            public Boolean getBoolean(String id) {
                return null;
            }

            @Override
            public Long getDuration(String id) {
                return null;
            }

            @Override
            public Float getPercent(String id) {
                return null;
            }

            @Override
            public int startGroup() {
                return 0;
            }

            @Override
            public void endGroup(int groupId) {

            }

            @Override
            public void dispose() {

            }
        };
    }

    public Theme getActiveTheme() {
        return activeTheme;
    }
}
