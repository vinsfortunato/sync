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

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Represents a graphical layout. All layout actors have a common
 * {@link #getRootActor() root actor} that is the layout starting point.
 * <p> An identifier can be associated to an
 * actor. Actors associated to an id can be retrieved by using {@link #findActorById(String)}. </p>
 */
public interface Layout {
    /**
     * Gets the layout root actor. A layout must have only one root actor!
     *
     * @return the layout root actor, or null if the layout is empty.
     */
    Actor getRootActor();

    /**
     * Gets the layout actor associated to the given id.
     *
     * @param id the actor id.
     * @return the actor associated to the given id, or null if there's no
     * such actor.
     */
    Actor findActorById(String id);
}
