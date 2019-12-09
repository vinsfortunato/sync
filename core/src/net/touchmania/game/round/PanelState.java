/*
 * Copyright 2019 Vincenzo Fortunato
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

package net.touchmania.game.round;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;

import java.util.Map;
import java.util.TreeMap;

/**
 * Contains panels states over time. Released is the default initial state.
 */
public class PanelState {
    private IntMap<TreeMap<Double, Boolean>> panelStates = new IntMap<>();
    private Array<PanelStateListener> listeners = new Array<>();

    /**
     * Set the panel state at the given time. If the panel
     * state at the given time is the same as the one that is being set
     * it will be ignored. Listeners will be notified only
     * if the state actually change.
     * @param panel the panel
     * @param time the time in seconds
     * @param pressed the state to set
     */
    public void setState(int panel, double time, boolean pressed) {
        //Get the map containing the states at each time or create it if necessary
        TreeMap<Double, Boolean> states = panelStates.get(panel);
        if(states == null) {
            states = new TreeMap<>();
            panelStates.put(panel, states);
        }

        if(states.isEmpty() && !pressed) {
            //By default the initial state is released.
            //Ignore setting release state when panel has never been pressed.
            return;
        }

        //Check if the state actually change
        Map.Entry<Double, Boolean> floorEntry = states.floorEntry(time);
        if(floorEntry == null || floorEntry.getValue() != pressed) {
            //State changes
            states.put(time, pressed);

            //Notify listeners
            for(PanelStateListener listener : listeners) {
                listener.onPanelStateChange(panel, time, pressed);
            }
        }
    }

    /**
     * Convenience method for {@link #setState(int, double, boolean)}.
     * Set the panel pressed at the given time.
     * @param panel the panel
     * @param time the time in seconds
     */
    public void setPressed(int panel, double time) {
        setState(panel, time, true);
    }

    /**
     * Convenience method for {@link #setState(int, double, boolean)}.
     * Set the panel released at the given time.
     * @param panel the panel
     * @param time the time in seconds
     */
    public void setReleased(int panel, double time) {
        setState(panel, time, false);
    }

    /**
     * Checks if the given panel is pressed at the given time.
     * @param panel the panel
     * @param time the time in seconds
     * @return true if the panel is pressed at the given time
     */
    public boolean isPressedAt(int panel, double time) {
        TreeMap<Double, Boolean> states = panelStates.get(panel);
        if(states != null) {
            Map.Entry<Double, Boolean> entry = states.floorEntry(time);
            return entry != null && entry.getValue();
        }
        return false;
    }

    /**
     * Checks if the given panel is released at the given time.
     * @param panel the panel
     * @param time the time in seconds
     * @return true if the panel is released at the given time
     */
    public boolean isReleasedAt(int panel, double time) {
        return !isPressedAt(panel, time);
    }

    /**
     * Gets the last time a pressed state occurred at the given time.
     * @param panel the panel
     * @param time the time in seconds.
     * @return the last time in seconds when a pressed state occurred at the
     * given time, or {@link Double#MAX_VALUE} if a pressed state never occurred.
     */
    public double getLastTimePressedAt(int panel, double time) {
        TreeMap<Double, Boolean> states = panelStates.get(panel);
        if(states != null && !states.isEmpty()) {
            Map.Entry<Double, Boolean> state = states.floorEntry(time);
            if(state != null) {
                boolean pressed = state.getValue();
                if(pressed) {
                    return state.getKey();
                } else {
                    state = states.lowerEntry(state.getKey());
                    return state.getKey();
                }
            }
        }
        return Double.MAX_VALUE;
    }

    /**
     * Gets the last time a released state occurred at the given time.
     * @param panel the panel
     * @param time the time in seconds.
     * @return the last time in seconds when a released state occurred at the
     * given time, or {@link Double#MIN_VALUE} if the panel has never been pressed.
     */
    public double getLastTimeReleasedAt(int panel, double time) {
        TreeMap<Double, Boolean> states = panelStates.get(panel);
        if(states != null && !states.isEmpty()) {
            Map.Entry<Double, Boolean> state = states.floorEntry(time);
            if(state != null) {
                boolean pressed = state.getValue();
                if(!pressed) {
                    return state.getKey();
                } else {
                    state = states.lowerEntry(state.getKey());
                    return state.getKey();
                }
            }
        }
        return Double.MIN_VALUE;
    }

    /**
     * Adds the given listener.
     * @param listener the listener
     * @return true if the listener has been added, false otherwise (listener already registered)
     */
    public boolean addListener(PanelStateListener listener) {
        if(!listeners.contains(listener, true)) {
            listeners.add(listener);
            return true;
        }
        return false;
    }

    /**
     * Removes the given listener.
     * @param listener the listener
     * @return true if the listener has been removed, false otherwise (listener not registered)
     */
    public boolean removeListener(PanelStateListener listener) {
        return listeners.removeValue(listener, true);
    }

    /**
     * Removes all listeners
     */
    public void clearListeners() {
        listeners.clear();
    }

    public interface PanelStateListener {
        /**
         * Called when the given panel change state at the given time.
         * @param panel the panel
         * @param time the time in seconds when the change occurs
         * @param pressed the updated state, true if pressed, false if released
         */
        void onPanelStateChange(int panel, double time, boolean pressed);
    }
}
