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

package net.sync.game.util.concurrent;

import com.badlogic.gdx.utils.Disposable;
import com.google.common.base.Preconditions;
import net.sync.game.Game;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * Manages executor services by binding them to int keys and
 * disposing them when they are no longer required.
 */
public class ExecutorManager implements Disposable {
    private Map<Integer, ExecutorService> executorsMap;
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
            executorsMap = new HashMap<>();
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
