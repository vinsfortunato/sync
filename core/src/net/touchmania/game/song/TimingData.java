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

package net.touchmania.game.song;

import java.util.TreeMap;

/**
 * @author flood2d
 */
public class TimingData {
    public double offset = 0.0D;
    public TreeMap<Double, Double> bpms;
    public TreeMap<Double, Double> stops;
    public TreeMap<Double, Double> delays;
    public TreeMap<Double, Double> warps;

    public Double putBpm(Double beat, Double bpm) {
        if(bpms == null) {
           bpms = new TreeMap<>();
        }
        return bpms.put(beat, bpm);
    }

    public Double putStop(Double beat, Double length) {
        if(stops == null) {
            stops = new TreeMap<>();
        }
        return stops.put(beat, length);
    }

    public Double putDelay(Double beat, Double length) {
        if(delays == null) {
            delays = new TreeMap<>();
        }
        return delays.put(beat, length);
    }

    public Double putWarp(Double beat, Double length) {
        if(warps == null) {
            warps = new TreeMap<>();
        }
        return warps.put(beat, length);
    }
}
