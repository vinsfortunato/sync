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

import net.sync.game.util.ui.TexturePath;

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
     * @return the fallback theme, or null if the theme has no fallback.
     */
    Theme getFallbackTheme();

    /**
     * Checks if the theme has a fallback theme. Check {@link #getFallbackTheme()} for more info.
     * @return true if the theme has a fallback.
     */
    boolean hasFallbackTheme();

    /**
     * Gets the {@link net.sync.game.resource.ThemeManifest manifest} of the theme. It contains
     * theme information such as the name, the author etc...
     * @return a {@link net.sync.game.resource.ThemeManifest} instance.
     */
    ThemeManifest getManifest();

    /**
     * Gets a {@link List} containing the language locales supported by the theme.
     * The first locale in the array is the default language.
     * The array doesn't contain languages supported by the fallback theme.
     * @return a {@link List} containing supported languages, can be null if the theme
     * uses the languages defined into the fallback theme.
     */
    List<Locale> getLanguages();

    TexturePath getTexturePath(String path);
}
