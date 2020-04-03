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

package net.sync.game.song;

import java.util.TreeMap;

/**
 * @author Vincenzo Fortunato
 */
public class TimingData {
    public double offset = 0.0D;
    public TreeMap<Double, Double> bpms;
    public TreeMap<Double, Double> stops;
    public TreeMap<Double, Double> delays;
    public TreeMap<Double, Double> warps;

    public Double putBpm(double beat, double bpm) {
        if(bpms == null) {
           bpms = new TreeMap<>();
        }
        return bpms.put(beat, bpm);
    }

    public Double putStop(double beat, double length) {
        if(stops == null) {
            stops = new TreeMap<>();
        }
        return stops.put(beat, length);
    }

    public Double putDelay(double beat, double length) {
        if(delays == null) {
            delays = new TreeMap<>();
        }
        return delays.put(beat, length);
    }

    public Double putWarp(double beat, double length) {
        if(warps == null) {
            warps = new TreeMap<>();
        }
        return warps.put(beat, length);
    }
}
