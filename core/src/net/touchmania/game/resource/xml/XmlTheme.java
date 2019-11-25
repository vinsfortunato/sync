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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Disposable;
import net.touchmania.game.Game;
import net.touchmania.game.resource.*;
import net.touchmania.game.resource.xml.parsers.XmlStyleParser;
import net.touchmania.game.util.Loader;
import net.touchmania.game.util.ui.DPI;
import net.touchmania.game.util.ui.TexturePath;

import java.util.*;

public class XmlTheme implements Theme {
    private Theme fallback;
    private FileHandle manifestFile;
    private XmlThemeManifest manifest;

    /* Resource maps */
    private Map<String, XmlDrawableLoader> drawables;
    private Map<String, Color> colors;
    private Map<String, Dimension> dimens;
    private Map<String, XmlFontLoader> fonts;
    private Map<String, XmlSoundLoader> sounds;
    private Map<String, XmlMusicLoader> musics;
    private Map<String, String> strings;
    private Map<String, Object> values;

    /* Supported languages */
    private List<Locale> langs;

    private int groupId = 0;
    private Map<Long, Set<Integer>> resourceGroups = new HashMap<>();
    private Map<Long, Disposable> loadedResources = new HashMap<>();

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
                XmlStyleParser parser = new XmlStyleParser(stylesDir.child(id + "_style.xml"), this);
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

    public void setFonts(Map<String, XmlFontLoader> fonts) {
        this.fonts = fonts;
    }

    @Override
    public BitmapFont getFont(String id) {
        if(fonts != null) {
            XmlFontLoader generator = fonts.get(id);
            if(generator != null) {
                try {
                    return generator.load();
                } catch (Exception e) {
                    //TODO log exception
                }
            }
        }
        return hasFallbackTheme() ? getFallbackTheme().getFont(id) : null;
    }

    public void setSounds(Map<String, XmlSoundLoader> sounds) {
        this.sounds = sounds;
    }

    @Override
    public Sound getSound(String id) {
        if(sounds != null) {
            XmlSoundLoader loader = sounds.get(id);
            if(loader != null) {
                try {
                    return loader.load();
                } catch (Exception e) {
                    //TODO log exception
                }
            }
        }
        return hasFallbackTheme() ? getFallbackTheme().getSound(id) : null;
    }

    public void setMusics(Map<String, XmlMusicLoader> musics) {
        this.musics = musics;
    }

    @Override
    public Music getMusic(String id) {
        if(musics != null) {
            XmlMusicLoader loader = musics.get(id);
            if(loader != null) {
                try {
                    return loader.load();
                } catch (Exception e) {
                    //TODO log exception
                }
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
        Iterator<Long> resIds = loadedResources.keySet().iterator();
        while(resIds.hasNext()) {
            Long resId = resIds.next();
            Set<Integer> groups = resourceGroups.get(resId);
            if(groups.remove(groupId) && groups.isEmpty()) {
                //Resource no more required, dispose
                resourceGroups.remove(resId);
                Disposable res = loadedResources.get(resId);
                res.dispose();
            }
            resIds.remove();
        }
    }

    public <R extends Disposable> R load(long id, Class<R> type, Loader<R> loader) throws Exception {
        int groupId = this.groupId;
        R res;

        if(loadedResources.containsKey(id)) {
            //Preloaded resource
            res = type.cast(loadedResources.get(id));
        } else {
            //Load resource and track
            res = loader.load();
            loadedResources.put(id, res);
        }

        //Bind resource to group
        Set<Integer> groups = resourceGroups.get(id);
        if(groups == null) {
            groups = new HashSet<>();
            resourceGroups.put(id, groups);
        }
        groups.add(groupId);

        return res;
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
