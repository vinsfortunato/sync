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

package net.touchmania.game.util.ui;

/**
 * Provides information about a theme.
 */
public interface ThemeManifest {
    /**
     * Gets the theme version.
     *
     * @return the theme version where higher values means more recent version.
     */
    int getVersion();

    /**
     * Gets the name of the theme.
     *
     * @return the theme name.
     */
    String getName();

    /**
     * Gets the author(s) of the theme.
     *
     * @return the theme author(s).
     */
    String getAuthor();

    /**
     * Gets the website related to the theme.
     *
     * @return the theme website.
     */
    String getWebsite();

    /**
     * Gets a brief description about the theme.
     *
     * @return the theme description.
     */
    String getDescription();

    //TODO drawable banner/icon
}
