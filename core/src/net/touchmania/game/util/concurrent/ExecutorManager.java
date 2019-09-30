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

package net.touchmania.game.util.concurrent;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntMap;
import com.google.common.base.Preconditions;
import net.touchmania.game.Game;

import java.util.concurrent.ExecutorService;

/**
 * Manages executor services by binding them to int keys and
 * disposing them when they are no longer required.
 */
public class ExecutorManager implements Disposable {
    private IntMap<ExecutorService> executorsMap;
    private int idCounter = 0;

    public ExecutorManager() {
        Game.instance().getDisposer().manage(this);
    }

    /**
     * Get the executor service associated to the given id.
     *
     * @param id the service id.
     * @return the service or null if there's no service associated to the given id.
     */
    public ExecutorService getExecutor(int id) {
        return executorsMap != null ? executorsMap.get(id) : null;
    }

    /**
     * Associate an executor service to an id. If a service is already
     * associated to the given id it will be shutdown and replaced by
     * the given one.
     *
     * @param id the service id.
     * @param service the service to bind. Null is not allowed.
     * @return true if there was an executor previously associated to the given id.
     * @throws NullPointerException if the given service is null.
     */
    public boolean putExecutor(int id, ExecutorService service) {
        Preconditions.checkNotNull(service, "Null services are not allowed!");
        if(executorsMap == null) {
            executorsMap = new IntMap<>();
        }
        ExecutorService prev =  executorsMap.put(id, service);
        if(prev != null) {
            prev.shutdownNow();
            return true;
        }
        return false;
    }

    /**
     * Remove and shutdown the executor service associated to the given id (if
     * there is one).
     *
     * @param id the id of the executor to remove.
     * @return true if there was an executor with the given id and it was removed and
     * shutdown correctly, false otherwise.
     */
    public boolean removeExecutor(int id) {
        ExecutorService service = executorsMap.remove(id);
        if(service != null) {
            service.shutdownNow();
            return true;
        }
        return false;
    }

    /**
     * Generate an id that can be used to associate an executor
     * service. Using this method when binding executors prevent
     * replacing a previously associated executor.
     *
     * @return an id that can be used to associate an executor service.
     */
    public int generateId() {
        return idCounter++;
    }

    @Override
    public void dispose() {
        if(executorsMap != null) {
            for(ExecutorService service : executorsMap.values()) {
                service.shutdownNow();
            }
        }
    }
}
