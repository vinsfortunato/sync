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

package net.touchmania.game.ui.xml;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ObjectMap;
import net.touchmania.game.ui.Layout;

public class XmlLayout implements Layout {
    private Actor rootActor;
    private ObjectMap<String, Actor> actorsLookupMap;

    public void setRootActor(Actor rootActor) {
        this.rootActor = rootActor;
    }

    @Override
    public Actor getRootActor() {
        return rootActor;
    }

    public void setActorsLookupMap(ObjectMap<String, Actor> actorsLookupMap) {
        this.actorsLookupMap = actorsLookupMap;
    }

    @Override
    public Actor findActorById(String id) {
        return actorsLookupMap != null ? actorsLookupMap.get(id) : null;
    }
}
