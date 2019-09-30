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

package net.touchmania.game.util.ui.xml;


import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import net.touchmania.game.util.math.MathUtils;
import net.touchmania.game.util.ui.*;
import net.touchmania.game.util.ui.xml.parsers.XmlStyleParser;

import java.util.Locale;

public class XmlTheme implements Theme {
    private Theme fallback;
    private FileHandle manifestFile;
    private XmlThemeManifest manifest;
    private ObjectMap<String, Color> colors;
    private ObjectMap<String, Dimension> dimens;
    private ObjectMap<String, Object> values;
    private ObjectMap<String, XmlFontGenerator> fonts;
    private ObjectMap<String, XmlSoundLoader> sounds;
    private ObjectMap<String, String> strings;
    private Array<Locale> langs;

    public XmlTheme(FileHandle manifestFile) {
        this.manifestFile = manifestFile;
    }

    @Override
    public Layout getLayout(String id) {
        return hasFallbackTheme() ? getFallbackTheme().getLayout(id) : null;
    }

    @Override
    public Style getStyle(String id) {
        FileHandle stylesDir = manifestFile.sibling("styles");
        if(stylesDir.exists()) {
            try {
                XmlStyleParser parser = new XmlStyleParser(stylesDir.child(id + "_style.xml"));
                return parser.parse();
            } catch (Exception e) {
                //TODO log exception
            }
        }
        return hasFallbackTheme() ? getFallbackTheme().getStyle(id) : null;
    }

    @Override
    public Drawable getDrawable(String id) {
        return hasFallbackTheme() ? getFallbackTheme().getDrawable(id) : null;
    }

    public void setColors(ObjectMap<String, Color> colors) {
        this.colors = colors;
    }

    @Override
    public Color getColor(String id) {
        if(colors != null) {
            Color color = colors.get(id);
            if(color != null) {
                return color;
            }
        }
        return hasFallbackTheme() ? getFallbackTheme().getColor(id) : null;
    }

    public void setDimensions(ObjectMap<String, Dimension> dimens) {
        this.dimens = dimens;
    }
    @Override
    public Dimension getDimension(String id) {
        if(dimens != null) {
            Dimension dimen = dimens.get(id);
            if(dimen != null) {
                return dimen;
            }
        }
        return hasFallbackTheme() ? getFallbackTheme().getDimension(id) : null;
    }

    public void setFonts(ObjectMap<String, XmlFontGenerator> fonts) {
        this.fonts = fonts;
    }

    @Override
    public FontGenerator getFont(String id) {
        if(fonts != null) {
            XmlFontGenerator generator = fonts.get(id);
            if(generator != null) {
                return generator;
            }
        }
        return hasFallbackTheme() ? getFallbackTheme().getFont(id) : null;
    }

    public void setSounds(ObjectMap<String, XmlSoundLoader> sounds) {
        this.sounds = sounds;
    }

    @Override
    public SoundLoader getSound(String id) {
        if(sounds != null) {
            XmlSoundLoader loader = sounds.get(id);
            if(loader != null) {
                return loader;
            }
        }
        return hasFallbackTheme() ? getFallbackTheme().getSound(id) : null;
    }

    public void setStrings(ObjectMap<String, String> strings) {
        this.strings = strings;
    }

    @Override
    public String getString(String id) {
        if(strings != null) {
            String string = strings.get(id);
            if(string != null) {
                return string;
            }
        }
        return hasFallbackTheme() ? getFallbackTheme().getString(id) : null;
    }

    public void setValues(ObjectMap<String, Object> values) {
        this.values = values;
    }

    @Override
    public Integer getIntValue(String id) {
        if(values != null) {
            Object value = values.get(id);
            if(value instanceof Integer) {
                return (Integer) value;
            }
        }
        return hasFallbackTheme() ? getFallbackTheme().getIntValue(id) : null;
    }

    @Override
    public Float getFloatValue(String id) {
        if(values != null) {
            Object value = values.get(id);
            if(value instanceof Float) {
                return (Float) value;
            }
        }
        return hasFallbackTheme() ? getFallbackTheme().getFloatValue(id) : null;
    }

    @Override
    public Boolean getBooleanValue(String id) {
        if(values != null) {
            Object value = values.get(id);
            if(value instanceof Boolean) {
                return (Boolean) value;
            }
        }
        return hasFallbackTheme() ? getFallbackTheme().getBooleanValue(id) : null;
    }

    @Override
    public Long getDurationValue(String id) {
        if(values != null) {
            Object value = values.get(id);
            if(value instanceof Long) {
                return (Long) value;
            }
        }
        return hasFallbackTheme() ? getFallbackTheme().getDurationValue(id) : null;
    }

    @Override
    public Float getPercentValue(String id) {
        if(values != null) {
            Object value = values.get(id);
            if(value instanceof Float) {
                return (Float) value;
            }
        }
        return hasFallbackTheme() ? getFallbackTheme().getPercentValue(id) : null;
    }

    public void setManifest(XmlThemeManifest manifest) {
        this.manifest = manifest;
    }

    @Override
    public Theme getFallbackTheme() {
        return fallback;
    }

    @Override
    public boolean hasFallbackTheme() {
        return fallback != null;
    }

    public void setFallbackTheme(Theme fallback) {
        this.fallback = fallback;
    }

    public XmlThemeManifest getManifest() {
        return manifest;
    }

    public void setLanguages(Array<Locale> supportedLangs) {
        this.langs = supportedLangs;
    }

    @Override
    public Array<Locale> getLanguages() {
        return langs;
    }
}
