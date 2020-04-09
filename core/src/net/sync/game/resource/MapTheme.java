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

package net.sync.game.resource;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import net.sync.game.resource.lazy.Resource;
import net.sync.game.util.ui.DPI;
import net.sync.game.util.ui.TexturePath;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static net.sync.game.Game.backend;

/**
 * A map based implementation of {@link ResourceProvider} and {@link Theme} where resources are saved into maps.
 * <p>{@link Resource} returned by this provider are decorated with {@link TrackedResource}
 * to implement the resource grouping functionality.</p>
 */
public class MapTheme implements Theme {
    private Theme fallback;
    private FileHandle manifestFile;
    private ThemeManifest manifest;

    /* Resource maps */
    private Map<String, Resource<Drawable>> drawables = Collections.emptyMap();
    private Map<String, Color> colors = Collections.emptyMap();
    private Map<String, Dimension> dimens = Collections.emptyMap();
    private Map<String, Resource<BitmapFont>> fonts = Collections.emptyMap();
    private Map<String, Resource<Sound>> sounds = Collections.emptyMap();
    private Map<String, Resource<Music>> musics = Collections.emptyMap();
    private Map<String, String> strings = Collections.emptyMap();
    private Map<String, Object> values = Collections.emptyMap();

    /* Supported languages */
    private List<Locale> langs;

    /* Resource groups */
    private int groupId = 0;

    public MapTheme(FileHandle manifestFile) {
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

    @Override
    public Resource<Drawable> getDrawable(String id) {
        Resource<Drawable> resource = drawables.get(id);
        if(resource != null) {
            return resource;
        }
        return hasFallbackTheme() ? getFallbackTheme().getDrawable(id) : null;
    }

    @Override
    public Color getColor(String id) {
        Color resource = colors.get(id);
        if(resource != null) {
            return resource;
        }
        return hasFallbackTheme() ? getFallbackTheme().getColor(id) : null;
    }

    @Override
    public Dimension getDimension(String id) {
        Dimension resource = dimens.get(id);
        if(resource != null) {
            return resource;
        }
        return hasFallbackTheme() ? getFallbackTheme().getDimension(id) : null;
    }

    @Override
    public Resource<BitmapFont> getFont(String id) {
        Resource<BitmapFont> resource = fonts.get(id);
        if(resource != null) {
            return resource;
        }
        return hasFallbackTheme() ? getFallbackTheme().getFont(id) : null;
    }

    @Override
    public Resource<Sound> getSound(String id) {
        Resource<Sound> resource = sounds.get(id);
        if(resource != null) {
            return resource;
        }
        return hasFallbackTheme() ? getFallbackTheme().getSound(id) : null;
    }

    @Override
    public Resource<Music> getMusic(String id) {
        Resource<Music> resource = musics.get(id);
        if(resource != null) {
            return resource;
        }
        return hasFallbackTheme() ? getFallbackTheme().getMusic(id) : null;
    }

    @Override
    public String getString(String id) {
        String string = strings.get(id);
        if(string != null) {
            return string;
        }
        return hasFallbackTheme() ? getFallbackTheme().getString(id) : null;
    }

    @Override
    public Integer getInt(String id) {
        Object value = values.get(id);
        if(value instanceof Integer) {
            return (Integer) value;
        }
        return hasFallbackTheme() ? getFallbackTheme().getInt(id) : null;
    }

    @Override
    public Float getFloat(String id) {
        Object value = values.get(id);
        if(value instanceof Float) {
            return (Float) value;
        }
        return hasFallbackTheme() ? getFallbackTheme().getFloat(id) : null;
    }

    @Override
    public Boolean getBoolean(String id) {
        Object value = values.get(id);
        if(value instanceof Boolean) {
            return (Boolean) value;
        }
        return hasFallbackTheme() ? getFallbackTheme().getBoolean(id) : null;
    }

    @Override
    public Long getDuration(String id) {
        Object value = values.get(id);
        if(value instanceof Long) {
            return (Long) value;
        }
        return hasFallbackTheme() ? getFallbackTheme().getDuration(id) : null;
    }

    @Override
    public Float getPercent(String id) {
        Object value = values.get(id);
        if(value instanceof Float) {
            return (Float) value;
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

        DPI dpi = backend().getDeviceDPI();
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
                if(hasFallbackTheme() && getFallbackTheme() instanceof MapTheme) {
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

    /**
     * Gets the fallback theme. If a resource is not present into the
     * theme it will be searched into the fallback theme.
     * <p> Generally the fallback theme is the default game theme. The default theme
     * has no fallback theme. Every custom theme should have a fallback theme. </p>
     * @return the fallback theme, or null if the theme has no fallback.
     */
    public Theme getFallbackTheme() {
        return fallback;
    }

    /**
     * Checks if the theme has a fallback theme. Check {@link #getFallbackTheme()} for more info.
     * @return true if the theme has a fallback.
     */
    public boolean hasFallbackTheme() {
        return fallback != null;
    }

    /**
     * Gets the {@link ThemeManifest manifest} of the theme. It contains
     * theme information such as the name, the author etc...
     * @return a {@link ThemeManifest} instance.
     */
    public ThemeManifest getManifest() {
        return manifest;
    }

    /**
     * Gets a {@link List} containing the language locales supported by the theme.
     * The first locale in the array is the default language.
     * The array doesn't contain languages supported by the fallback theme.
     * @return a {@link List} containing supported languages, can be null if the theme
     * uses the languages defined into the fallback theme.
     */
    public List<Locale> getLanguages() {
        return langs;
    }

    @Override
    public void dispose() {
    }

    /* Setters */
    public void setDrawables(Map<String, Resource<Drawable>> drawables) {
        checkNotNull(drawables);
        this.drawables = trackResources(drawables);
    }

    public void setDimensions(Map<String, Dimension> dimens) {
        checkNotNull(dimens);
        this.dimens = dimens;
    }

    public void setFonts(Map<String, Resource<BitmapFont>> fonts) {
        checkNotNull(fonts);
        this.fonts = trackResources(fonts);
    }

    public void setColors(Map<String, Color> colors) {
        checkNotNull(colors);
        this.colors = colors;
    }

    public void setSounds(Map<String, Resource<Sound>> sounds) {
        checkNotNull(sounds);
        this.sounds = trackResources(sounds);
    }

    public void setMusics(Map<String, Resource<Music>> musics) {
        checkNotNull(musics);
        this.musics = trackResources(musics);
    }

    public void setStrings(Map<String, String> strings) {
        checkNotNull(strings);
        this.strings = strings;
    }

    public void setValues(Map<String, Object> values) {
        checkNotNull(values);
        this.values = values;
    }

    public void setManifest(ThemeManifest manifest) {
        this.manifest = manifest;
    }

    public void setLanguages(List<Locale> supportedLangs) {
        this.langs = supportedLangs;
    }

    public void setFallbackTheme(Theme fallback) {
        this.fallback = fallback;
    }

    /* Resource tracking */

    /**
     * Replaces each resource in the given map with a decorated version of it.
     * @param resources the map with values to decorate with {@link TrackedResource}.
     * @param <T> the resource type.
     * @return the same map with decorated values.
     */
    private <T> Map<String, Resource<T>> trackResources(Map<String, Resource<T>> resources) {
        for(Map.Entry<String, Resource<T>> entry : resources.entrySet()) {
            entry.setValue(new TrackedResource<>(entry.getValue()));
        }
        return resources;
    }

    /**
     * A resource decorator used to track when a resource is loaded/unloaded. Used
     * by the resource provider grouping system to unload resources when a group is ended.
     * @param <T> the resource type.
     */
    public class TrackedResource<T> implements Resource<T> {
        private Resource<T> resource;

        /**
         * Decorates the given resource with this tracker.
         * @param resource the resource to decorate.
         */
        public TrackedResource(Resource<T> resource) {
            this.resource = resource;
        }

        @Override
        public T get() {
            return resource.get();
        }

        @Override
        public boolean isAvailable() {
            return resource.isAvailable();
        }

        @Override
        public boolean isLoading() {
            return resource.isLoading();
        }

        @Override
        public void load() {
            //TODO
        }

        @Override
        public void unload() {
            //TODO
        }

        /**
         * Gets the underlying resource being decorated
         * @return the source resource being decorated.
         */
        public Resource<T> getDecoratedResource() {
            return resource;
        }
    }
}
