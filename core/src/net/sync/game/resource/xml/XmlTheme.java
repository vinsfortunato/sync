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

package net.sync.game.resource.xml;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import net.sync.game.Game;
import net.sync.game.resource.Dimension;
import net.sync.game.resource.Layout;
import net.sync.game.resource.Style;
import net.sync.game.resource.Theme;
import net.sync.game.resource.lazy.Resource;
import net.sync.game.util.ui.DPI;
import net.sync.game.util.ui.TexturePath;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class XmlTheme implements Theme {
    private Theme fallback;
    private FileHandle manifestFile;
    private XmlThemeManifest manifest;

    /* Resource maps */
    private Map<String, Resource<Drawable>> drawables;
    private Map<String, Color> colors;
    private Map<String, Dimension> dimens;
    private Map<String, Resource<BitmapFont>> fonts;
    private Map<String, Resource<Sound>> sounds;
    private Map<String, Resource<Music>> musics;
    private Map<String, String> strings;
    private Map<String, Object> values;

    /* Supported languages */
    private List<Locale> langs;

    /* Resource groups */
    private int groupId = 0;

    public XmlTheme(FileHandle manifestFile) {
        this.manifestFile = manifestFile;
    }

    @Override
    public Resource<Layout> getLayout(String id) {
        //TODO getLayout
        return hasFallbackTheme() ? getFallbackTheme().getLayout(id) : null;
    }

    @Override
    public Resource<Style> getStyle(String id) {
        /** TODO
        FileHandle stylesDir = manifestFile.sibling("styles");
        if(stylesDir.exists()) {
            try {
                XmlStyleParser parser = new XmlStyleParser(stylesDir.child(id + "_style.xml"), this);
                return parser.parse();
            } catch (Exception e) {
                //TODO log exception
            }
        }
         **/
        return hasFallbackTheme() ? getFallbackTheme().getStyle(id) : null;
    }

    public void setDrawables(Map<String, Resource<Drawable>> drawables) {
        this.drawables = drawables;
    }

    @Override
    public Resource<Drawable> getDrawable(String id) {
        if(drawables != null) {
            Resource<Drawable> resource = drawables.get(id);
            if(resource != null) {
                return resource;
            }
        }
        return hasFallbackTheme() ? getFallbackTheme().getDrawable(id) : null;
    }

    public void setColors(Map<String, Color> colors) {
        this.colors = colors;
    }

    @Override
    public Color getColor(String id) {
        if(colors != null) {
            Color resource = colors.get(id);
            if(resource != null) {
                return resource;
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
            Dimension resource = dimens.get(id);
            if(resource != null) {
                return resource;
            }
        }
        return hasFallbackTheme() ? getFallbackTheme().getDimension(id) : null;
    }

    public void setFonts(Map<String, Resource<BitmapFont>> fonts) {
        this.fonts = fonts;
    }

    @Override
    public Resource<BitmapFont> getFont(String id) {
        if(fonts != null) {
            Resource<BitmapFont> resource = fonts.get(id);
            if(resource != null) {
                return resource;
            }
        }
        return hasFallbackTheme() ? getFallbackTheme().getFont(id) : null;
    }

    public void setSounds(Map<String, Resource<Sound>> sounds) {
        this.sounds = sounds;
    }

    @Override
    public Resource<Sound> getSound(String id) {
        if(sounds != null) {
            Resource<Sound> resource = sounds.get(id);
            if(resource != null) {
                return resource;
            }
        }
        return hasFallbackTheme() ? getFallbackTheme().getSound(id) : null;
    }

    public void setMusics(Map<String, Resource<Music>> musics) {
        this.musics = musics;
    }

    @Override
    public Resource<Music> getMusic(String id) {
        if(musics != null) {
            Resource<Music> resource = musics.get(id);
            if(resource != null) {
                return resource;
            }
        }

        return hasFallbackTheme() ? getFallbackTheme().getMusic(id) : null;
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
    public int startGroup() {
        int groupId = ++this.groupId;
        //TODO check groupid is available
        return groupId;
    }

    @Override
    public void endGroup(int groupId) {

    }

    @Override
    public boolean isGroupLoading(int groupId) {
        return false;
    }

    @Override
    public TexturePath getTexturePath(String path) {
        //Resolve texture path
        FileHandle drawablesDir = manifestFile.sibling("drawables");
        FileHandle textureFile;

        DPI dpi = Game.instance().getBackend().getDeviceDPI();
        String dpiDirName = null;
        switch(dpi) {
            case LOW:       dpiDirName = "low";         break;
            case MEDIUM:    dpiDirName = "medium";      break;
            case HIGH:      dpiDirName = "high";        break;
        }

        //Search inside drawables/<dpi>/ dir first
        textureFile = drawablesDir.child(dpiDirName).child(path);
        if(!textureFile.exists()) {
            //Dpi specific texture not found
            //Search inside drawables/ dir
            textureFile = drawablesDir.child(path);
            if(!textureFile.exists()) {
                //Texture not found in this theme
                if(hasFallbackTheme() && getFallbackTheme() instanceof XmlTheme) {
                    //Search inside fallback theme
                    return getFallbackTheme().getTexturePath(path);
                } else {
                    //Return absolute texture path
                    return () -> Gdx.files.absolute(path);
                }
            }
        }

        final FileHandle f = textureFile;
        return () -> f;
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

    @Override
    public void dispose() {
    }
}
