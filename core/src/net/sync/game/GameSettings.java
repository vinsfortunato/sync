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

package net.sync.game;

import net.sync.game.song.sim.SimFormat;
import net.sync.game.ui.ScreenCachePolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Vincenzo Fortunato
 */
public class GameSettings {
    /** Contains supported sim formats ordered by their priority in ascending order (formats with
     * higher priority have higher indexes).**/
    public List<net.sync.game.song.sim.SimFormat> simFormatPriorityList = new ArrayList<>();

    private net.sync.game.ui.ScreenCachePolicy screenCachePolicy = net.sync.game.ui.ScreenCachePolicy.DISPOSE_ON_HIDE;

    public GameSettings() {
        simFormatPriorityList.add(net.sync.game.song.sim.SimFormat.DWI);
        simFormatPriorityList.add(net.sync.game.song.sim.SimFormat.SSC);
        simFormatPriorityList.add(net.sync.game.song.sim.SimFormat.SM);
    }

    /**
     * Get sim format priority. Higher values are associated with formats with higher priority.
     * This will be used to pick the preferred sim format from a directory containing more then
     * one sim file.
     * @param format the sim format
     * @return the priority of the sim format.
     */
    public int getSimFormatPriority(SimFormat format) {
        return simFormatPriorityList.indexOf(format);
    }

    public ScreenCachePolicy getScreenCachePolicy() {
        return screenCachePolicy;
    }

    public GameMode getGameMode() {
        return GameMode.DANCE;
    }

    public Locale getLanguage() {
        return Locale.getDefault(); //TODO
    }
}
