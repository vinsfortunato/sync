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

    public Theme getActiveTheme() {
        return activeTheme;
    }
}
