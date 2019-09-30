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


import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectFloatMap;
import net.touchmania.game.util.math.Line2D;

import java.util.Iterator;
import java.util.TreeMap;

/**
 * Maps beats to time. Can be used to retrieve a beat at a specific time (relative to the start
 * of the song). See {@link #getBeatAt(float)}.
 *
 * <p>Deprecated: substituted by {@link Timing}.</p>
 */
@Deprecated
public class OldTimingGraph {
    /** Contains timing segments. The key is the start time of the segment **/
    private TreeMap<Float, Line2D> timingSegments;

    /**
     * Create the timing graph.
     * @param offset the offset between the beginning of the song and the
     *               start of the note data.
     * @param bpms can't be null or empty and must have key beat 0.0f.
     * @param stops can't be null.
     * @throws InvalidTimingDataException if the given arguments are invalid.
     */
    public OldTimingGraph(float offset, ObjectFloatMap<Float> bpms, ObjectFloatMap<Float> stops) throws InvalidTimingDataException {
        if(bpms == null) {
            throw new InvalidTimingDataException("BPMs map cannot be null!");
        } else if(stops == null) {
            throw new InvalidTimingDataException("Stops map cannot be null!");
        } else if(bpms.size == 0) {
            throw new InvalidTimingDataException("BPMs map cannot be empty!");
        } else if(!bpms.containsKey(0.0f)) {
            throw new InvalidTimingDataException("BPMs map doesn't contain 0.0f key beat!");
        }

        timingSegments = new TreeMap<>();

        Array<KeyBeat> keyBeats = new Array<>();
        ObjectFloatMap.Keys<Float> keys;
        keys = bpms.keys();
        while(keys.hasNext()) {
            keyBeats.add(new KeyBeat(keys.next(), false));
        }
        keys = stops.keys();
        while(keys.hasNext()) {
            keyBeats.add(new KeyBeat(keys.next(), true));
        }
        keyBeats.sort();

        double time, bpm, m, q;

        //Calculate first segment
        time = 0.0D;
        bpm = bpms.get(0.0f, 0.0f);
        m = bpm / 60.0D;
        q = m * offset;
        timingSegments.put((float) time, new Line2D(m, q));

        //Calculate other segments
        for(int i = 1; i < keyBeats.size; i++) {
            KeyBeat keyBeat = keyBeats.get(i);
            time = (keyBeat.beat - q) / m;

            if(keyBeat.isStop) {
                timingSegments.put((float) time, new Line2D(0, keyBeat.beat));
                time += stops.get(keyBeat.beat, 0.0f);
            } else {
                bpm = bpms.get(keyBeat.beat, 0.0f);
            }

            m = bpm / 60.0D;
            q = keyBeat.beat - m * time;

            timingSegments.put((float) time, new Line2D(m, q));
        }
        //TODO values must be rounded properly.
    }

    /**
     * Gets the beat at the given time, where time is relative to the start of
     * the music track.
     * @param time the time in seconds relative to the music track.
     * @return the beat at given time.
     */
    public float getBeatAt(float time) {
        Line2D line = getTimingSegment(time);
        return line.f((double)time).floatValue();
    }

    /**
     * Gets the time at the given beat, where time is relative to the start of
     * the music track. If the beat is a stop the time returned is the start
     * time of the next timing segment (the timing function during the stop
     * can't be reverted because it is constant).
     * @param beat the beat.
     * @return the time at given beat.
     */
    public float getTimeAt(float beat) {
        Line2D line = null;
        Iterator<Float> it = timingSegments.keySet().iterator();
        float startTime; //Timing segment start time.
        float endTime; //Timing segment end time.
        if(it.hasNext()) {
            startTime = it.next(); //First timing segment.
            line = timingSegments.get(startTime);
            while(it.hasNext()) {
                endTime = it.next();
                if(beat < getBeatAt(endTime)) {
                    line = timingSegments.get(startTime);
                    if(Double.compare(line.getM(), 0.0D) == 0) {
                        line = timingSegments.get(endTime); //If it's a stop, get the next segment.
                    }
                    break;
                } else {
                    line = timingSegments.get(endTime);
                }
                startTime = endTime;
            }
        }

        if(line == null || Double.compare(line.getM(), 0.0D) == 0) {
            throw new IllegalStateException("Invalid timing graph!");
        }
        return (float) ((beat - line.getQ()) / line.getM());
    }

    /**
     * Gets BPM at the given time, where time is relative to the start of
     * the music track.
     * @param time the time in seconds relative to the music track.
     * @return the bpm at the given time, 0 if it's a stop.
     */
    public float getBpmAt(float time) {
        Line2D line = getTimingSegment(time);
        return (float) (line.getM() * 60.0D);
    }

    /**
     * Checks if there is a stop at the given time.
     * @param time the time in seconds relative to the music track.
     * @return true if there is a stop at the given time.
     */
    public boolean isStop(float time) {
        Line2D line = getTimingSegment(time);
        return Double.compare(line.getM(), 0.0D) == 0;
    }

    /**
     * Gets the duration of the timing segment. If the segment is a stop
     * the returned value is the duration of the stop.
     * @param time the time in seconds relative to the music track.
     * @return the duration in seconds of the timing segment at the given time or
     * {@link Float#POSITIVE_INFINITY} if it's the last segment.
     */
    public float getSegmentDuration(float time) {
        if(time < 0.0f) {
            time = 0.0f;
        }
        Float floorTime = timingSegments.floorKey(time);
        Float ceilingTime = timingSegments.ceilingKey(time);
        if(ceilingTime == null) {
            return Float.POSITIVE_INFINITY;
        }
        return ceilingTime - floorTime;
    }

    /**
     * Gets the start time of the timing segment.
     * @param time the time in seconds relative to the music track.
     * @return the start time of the timing segment.
     */
    public float getSegmentStartTime(float time) {
        if(time < 0.0f) {
            time = 0.0f;
        }
        return timingSegments.floorKey(time);
    }

    private Line2D getTimingSegment(float time) {
        if(time < 0.0f) {
            return timingSegments.get(0.0f);
        } else {
            return timingSegments.floorEntry(time).getValue();
        }
    }

    private class KeyBeat implements Comparable<KeyBeat> {
        public final float beat;
        public final boolean isStop;

        public KeyBeat(float beat, boolean isStop) throws InvalidTimingDataException {
            if(beat < 0.0f) {
                throw new InvalidTimingDataException("BPMs and Stops maps contain negative beat keys!");
            }
            this.beat = beat;
            this.isStop = isStop;
        }

        @Override
        public int compareTo(KeyBeat o) {
            int result = Float.compare(beat, o.beat);
            if(result == 0) {
                return isStop == o.isStop ? 0 : (isStop ? -1 : 1);
            }
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == null) return false;
            if(obj instanceof KeyBeat) {
                KeyBeat keyBeat = (KeyBeat) obj;
                return beat == keyBeat.beat && isStop == keyBeat.isStop;
            }
            return false;
        }
    }
}
