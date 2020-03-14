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

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Disposable;
import net.touchmania.game.resource.lazy.Resource;

/**
 * <p>Manages and provides resources.</p>
 * <p>Get a resource by using one of the defined getter methods.
 * Some resources need to be loaded before being used. A {@link Resource}
 * object will be returned for those resource and can be used to manage the
 * required resource. The resource provider is intended to be used during rendering
 * so calling its methods will not block the calling thread.</p>
 * <p> Some resources need to be disposed when they are no longer needed.
 * Methods {@link #startGroup()} and {@link #endGroup(int)} can be used to
 * keep track of loaded resources and dispose them when they are no longer needed. </p>
 * <p> By starting a group all resources loaded after group creation will be bind to
 * the group. Resources can be added to multiple groups thus it is possible to start
 * more than one group. Loaded resources will be bind to all active groups. </p>
 * <p> A resource will be disposed when all its bind groups are ended. </p>
 * <p> A resource loaded with {@link Resource#load()} will automatically be bind
 * to the active groups. {@link #isGroupLoading(int)} can be used to check if resources
 * bind to the given group are still loading. </p>
 */
public interface ResourceProvider extends Disposable {
    /**
     * Gets the layout with the given id.
     * @param id the layout id.
     * @return the layout with the given id, or null if there's no layout with the given id.
     */
    Resource<Layout> getLayout(String id);

    /**
     * Gets the style with the given id.
     * @param id the style id.
     * @return the style with the given id, or null if there's no style with the given id.
     */
    Resource<Style> getStyle(String id);

    /**
     * Gets the drawable with the given id.
     * @param id the drawable id.
     * @return the drawable with the given id, or null if there's no drawable with the given id.
     */
    Resource<Drawable> getDrawable(String id);

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
     * Gets the font with the given id.
     * @param id the font id.
     * @return the font with the given id, or null if there's no font with the given id.
     */
    Resource<BitmapFont> getFont(String id);

    /**
     * Gets the sound with the given id.
     * @param id the sound id.
     * @return the sound with the given id, or null if there's no sound with the given id.
     */
    Resource<Sound> getSound(String id);

    /**
     * Gets the music with the given id.
     * @param id the music id.
     * @return the music with the given id, or null if there's no music with the given id.
     */
    Resource<Music> getMusic(String id);

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
    Integer getInt(String id);

    /**
     * Gets the float value with the given id.
     * @param id the value id.
     * @return the float value with the given id, or null if there's no value with the given id.
     */
    Float getFloat(String id);

    /**
     * Gets the boolean value with the given id.
     * @param id the value id.
     * @return the boolean value with the given id, or null if there's no value with the given id.
     */
    Boolean getBoolean(String id);

    /**
     * Gets the duration value with the given id.
     * @param id the value id.
     * @return the duration value with the given id, or null if there's no value with the given id.
     */
    Long getDuration(String id);

    /**
     * Gets the percent value with the given id as a float between 0 and 1 (inclusive).
     * @param id the value id.
     * @return the float value with the given id and between 0 and 1 (inclusive) where 0 is equal to 0%
     * and 1 is equal to 100%, or null if there's no value with the given id.
     */
    Float getPercent(String id);

    /**
     * Start a resource group. All resources loaded after calling this method will
     * be added to the group until {@link #endGroup(int)} is called. This can be
     * used to track resources that need to be disposed when they are no longer needed.
     * @return the group id.
     */
    int startGroup();

    /**
     * End a resource group. Resources that are present in more than one group will
     * only be removed from the group but will not be disposed.
     * @param groupId the group id.
     */
    void endGroup(int groupId);

    /**
     * Check if resources bind to the given group are still loading.
     * @param groupId the group id.
     * @return true if bind resources are still loading, false otherwise.
     */
    boolean isGroupLoading(int groupId);
}
