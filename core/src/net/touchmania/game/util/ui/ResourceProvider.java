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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public interface ResourceProvider {
    /**
     * Gets the layout with the given id. The id of a layout is declared into the layout resource file name before
     * '_layout.xml'.
     * @param id the layout id.
     * @return the layout with the given id, or null if there's no layout with the given id.
     */
    Layout getLayout(String id);

    /**
     * Gets the style with the given id. The id of a style is declared into the style resource file name before
     * '_style.xml'.
     * @param id the style id.
     * @return the style with the given id, or null if there's no style with the given id.
     */
    Style getStyle(String id);

    /**
     * Gets the drawable with the given id.
     * @param id the drawable id.
     * @return the drawable with the given id, or null if there's no drawable with the given id.
     */
    Drawable getDrawable(String id);

    /**
     * Gets the color with the given id.
     * @param id the color id.
     * @return the color with the given id, or null if there's no color with the given id.
     */
    Color getColor(String id);

    /**
     * Gets the dimension with the given id.
     * @param id the dimension id.
     * @return the dimension with the given id, or null if there's no dimension with the given id.
     */
    Dimension getDimension(String id);

    /**
     * Gets the font generator with the given id. Bitmap font must be generated using the provided
     * generator only when needed.
     * @param id the font id.
     * @return the generator of the font with the given id, or null if there's no font with the given id.
     */
    FontGenerator getFont(String id);

    /**
     * Gets the sound loaded with the given id. Sound must be loaded using the provided loader only when
     * needed.
     * @param id the sound id.
     * @return the loader of the sound with the given id, or null if there's no sound with the given id.
     */
    SoundLoader getSound(String id);

    /**
     * Gets the string with the given id.
     * @param id the string id.
     * @return the string with the given id, or null if there's no string with the given id.
     */
    String getString(String id);

    /**
     * Gets the int value with the given id.
     * @param id the value id.
     * @return the int value with the given id, or null if there's no value with the given id.
     */
    Integer getIntValue(String id);

    /**
     * Gets the float value with the given id.
     * @param id the value id.
     * @return the float value with the given id, or null if there's no value with the given id.
     */
    Float getFloatValue(String id);

    /**
     * Gets the boolean value with the given id.
     * @param id the value id.
     * @return the boolean value with the given id, or null if there's no value with the given id.
     */
    Boolean getBooleanValue(String id);

    /**
     * Gets the duration value with the given id.
     * @param id the value id.
     * @return the duration value with the given id, or null if there's no value with the given id.
     */
    Long getDurationValue(String id);

    /**
     * Gets the percent value with the given id as a float between 0 and 1 (inclusive).
     * @param id the value id.
     * @return the float value with the given id and between 0 and 1 (inclusive) where 0 is equal to 0%
     * and 1 is equal to 100%, or null if there's no value with the given id.
     */
    Float getPercentValue(String id);
}
