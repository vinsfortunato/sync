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

package net.touchmania.game.resource.xml;


import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import net.touchmania.game.resource.*;
import net.touchmania.game.resource.xml.parsers.XmlStyleParser;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class XmlTheme implements Theme {
    private Theme fallback;
    private FileHandle manifestFile;
    private XmlThemeManifest manifest;
    private Map<String, Color> colors;
    private Map<String, Dimension> dimens;
    private Map<String, Object> values;
    private Map<String, XmlFontGenerator> fonts;
    private Map<String, XmlSoundLoader> sounds;
    private Map<String, String> strings;
    private List<Locale> langs;

    public XmlTheme(FileHandle manifestFile) {
        this.manifestFile = manifestFile;
    }

    @Override
    public Layout getLayout(String id) {
        //TODO getLayout
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
        //TODO getDrawable
        return hasFallbackTheme() ? getFallbackTheme().getDrawable(id) : null;
    }

    public void setColors(Map<String, Color> colors) {
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

    public void setDimensions(Map<String, Dimension> dimens) {
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

    public void setFonts(Map<String, XmlFontGenerator> fonts) {
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

    public void setSounds(Map<String, XmlSoundLoader> sounds) {
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

    public void setStrings(Map<String, String> strings) {
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

    public void setValues(Map<String, Object> values) {
        this.values = values;
    }

    @Override
    public Integer getInt(String id) {
        if(values != null) {
            Object value = values.get(id);
            if(value instanceof Integer) {
                return (Integer) value;
            }
        }
        return hasFallbackTheme() ? getFallbackTheme().getInt(id) : null;
    }

    @Override
    public Float getFloat(String id) {
        if(values != null) {
            Object value = values.get(id);
            if(value instanceof Float) {
                return (Float) value;
            }
        }
        return hasFallbackTheme() ? getFallbackTheme().getFloat(id) : null;
    }

    @Override
    public Boolean getBoolean(String id) {
        if(values != null) {
            Object value = values.get(id);
            if(value instanceof Boolean) {
                return (Boolean) value;
            }
        }
        return hasFallbackTheme() ? getFallbackTheme().getBoolean(id) : null;
    }

    @Override
    public Long getDuration(String id) {
        if(values != null) {
            Object value = values.get(id);
            if(value instanceof Long) {
                return (Long) value;
            }
        }
        return hasFallbackTheme() ? getFallbackTheme().getDuration(id) : null;
    }

    @Override
    public Float getPercent(String id) {
        if(values != null) {
            Object value = values.get(id);
            if(value instanceof Float) {
                return (Float) value;
            }
        }
        return hasFallbackTheme() ? getFallbackTheme().getPercent(id) : null;
    }

    @Override
    public void loadDomain(String domain) {
        //TODO

        //Load drawables starting with
    }

    @Override
    public void disposeDomain(String domain) {
        //TODO
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

    public void setLanguages(List<Locale> supportedLangs) {
        this.langs = supportedLangs;
    }

    @Override
    public List<Locale> getLanguages() {
        return langs;
    }
}
