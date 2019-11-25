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

package net.touchmania.game.resource;

import com.badlogic.gdx.utils.Array;
import net.touchmania.game.util.ui.TexturePath;

import java.util.List;
import java.util.Locale;

/**
 * Represents a graphical theme. Provides resources used by the graphical UI.
 */
public interface Theme extends ResourceProvider {
    /**
     * Gets the fallback theme. If a resource is not present into the
     * theme it will be searched into the fallback theme.
     * <p> Generally the fallback theme is the default game theme. The default theme
     * has no fallback theme. Every custom theme should have a fallback theme. </p>
     *
     * @return the fallback theme, or null if the theme has no fallback.
     */
    Theme getFallbackTheme();

    /**
     * Checks if the theme has a fallback theme. Check {@link #getFallbackTheme()} for more info.
     *
     * @return true if the theme has a fallback.
     *
     */
    boolean hasFallbackTheme();

    /**
     * Gets the {@link ThemeManifest manifest} of the theme. It contains
     * theme information such as the name, the author etc...
     *
     * @return a {@link ThemeManifest} instance.
     */
    ThemeManifest getManifest();

    /**
     * Gets an {@link Array} containing the language locales supported by the theme.
     * The first locale in the array is the default language.
     * The array doesn't contain languages supported by the fallback theme.
     * @return an {@link Array} containing supported languages, can be null if the theme
     * uses the languages defined into the fallback theme.
     */
    List<Locale> getLanguages();

    TexturePath getTexturePath(String path);
}
