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

/**
 * Provides information about a theme.
 */
public interface ThemeManifest {
    /**
     * Gets the theme version.
     * @return the theme version where higher values means more recent version.
     */
    int getVersion();

    /**
     * Gets the name of the theme.
     * @return the theme name.
     */
    String getName();

    /**
     * Gets the author(s) of the theme.
     * @return the theme author(s).
     */
    String getAuthor();

    /**
     * Gets the website related to the theme.
     * @return the theme website.
     */
    String getWebsite();

    /**
     * Gets a brief description about the theme.
     * @return the theme description.
     */
    String getDescription();

    //TODO drawable banner/icon
}
