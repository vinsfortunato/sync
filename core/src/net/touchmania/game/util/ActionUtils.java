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

package net.touchmania.game.util;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

/**
 * @author flood2d
 */
@Deprecated
public final class ActionUtils {
    /**
     * Executes the given action backwards. If the action isn't ended then
     * begin the backward execution from the current action state.
     * @param temporalAction
     * @param actor
     */
    public static void rollback(TemporalAction temporalAction, Actor actor) {
        temporalAction.setReverse(!temporalAction.isReverse());
        if(actor.getActions().contains(temporalAction, true)) {
            temporalAction.setTime(temporalAction.getDuration() - temporalAction.getTime());
        } else {
            temporalAction.restart();
            actor.addAction(temporalAction);
        }
    }

    /**
     * Executes the given action backwards. If the action isn't ended then
     * begin the backward execution from the current action state.
     * @param alphaAction
     * @param actor
     * @param start the start alpha.
     */
    public static void rollback(AlphaAction alphaAction, Actor actor, float start) {
        alphaAction.getColor().a = start;
        rollback(alphaAction, actor);
    }
}
