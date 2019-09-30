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

import com.google.common.base.Preconditions;
import net.touchmania.game.util.math.Graph2D;
import net.touchmania.game.util.math.LineGraph2D;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Maps beats to time. Can be used to retrieve a beat at a specific time (relative
 * to the start of the song).
 * @author flood2d
 */
public class Timing {
    private TimingData timingData;
    /** beat as a function of time **/
    private Graph2D beatGraph;
    /** time as a function of beat **/
    private Graph2D timeGraph;

    public Timing(TimingData timingData) throws InvalidTimingDataException {
        if(timingData == null) {
            throw new InvalidTimingDataException("Timing data cannot be null.");
        } else if(timingData.bpms == null || timingData.bpms.isEmpty()) {
            throw new InvalidTimingDataException("BPMs map cannot be empty.");
        } else if(timingData.bpms.firstKey().compareTo(0.0D) != 0){
            throw new InvalidTimingDataException("BPMs map doesn't contain 0.0 key beat.");
        }

        this.timingData = timingData;
        this.beatGraph = TimingBeatGraphBuilder.build(timingData);
        this.timeGraph = beatGraph.invert();
    }

    /**
     * Gets the beat at the given time, where time is relative to the start of
     * the music track.
     * @param time the time in seconds relative to the music track.
     * @return the beat at given time.
     */
    public double getBeatAt(double time) {
        return beatGraph.f(time);
    }

    /**
     * Gets the time at the given beat, where time is relative to the start of
     * the music track. If the beat is a stop/delay the time returned is the start
     * time of the next timing segment (the timing function during a stop/delay
     * can't be reverted because it is constant).
     * @param beat the beat.
     * @return the time at given beat.
     */
    public double getTimeAt(double beat) {
        Preconditions.checkArgument(Double.compare(beat, 0.0D) >= 0, "Beat cannot be less than 0.");
        Double time = timeGraph.f(beat);
        if(time.isNaN()) {
            throw new IllegalStateException("Invalid timing graph. Infinite pauses aren't allowed.");
        }
        return time;
    }

    /**
     * Gets BPM at the given beat.
     * @param beat the beat.
     * @return the bpm at the given beat. Doesn't return 0 if the beat is a stop/delay.
     * Use {@link Timing#isStop(double)} or {@link Timing#isDelay(double)} to check if there is
     * a pause at the given beat.
     */
    public double getBpmAt(double beat) {
        Preconditions.checkArgument(Double.compare(beat, 0.0D) >= 0, "Beat cannot be less than 0.");
        return timingData.bpms.floorEntry(beat).getValue();
    }

    /**
     * Checks if there is a pause at the given beat (stop/delay).
     * @param beat the beat.
     * @return true if there is a pause at the given beat, false otherwise.
     */
    public boolean isPause(double beat) {
        Preconditions.checkArgument(Double.compare(beat, 0.0D) >= 0, "Beat cannot be less than 0.");
        return getPauseLength(beat) > 0.0D;
    }

    /**
     * Gets the pause length at the given beat.
     * @param beat the pause beat.
     * @return the pause length in seconds, sum of delay length and stop length at the given beat.
     * Returns 0 if there's no pause at the given beat.
     */
    public double getPauseLength(double beat) {
        return getDelayLength(beat) + getStopLength(beat);
    }

    /**
     * Checks if there is a stop at the given time.
     * @param time the time.
     * @return true if there is a stop at the given time, false otherwise.
     */
    public boolean isStop(double time) {
        TreeMap<Double, Double> stopMap = timingData.stops;
        Double beat, stopLength, pauseEndTime;
        if(stopMap != null) {
            beat = getBeatAt(time);
            stopLength = stopMap.get(beat);
            if(stopLength != null) { //There is a stop at the given beat.
                pauseEndTime = getTimeAt(beat); //Can be different from time passed as argument
                return Double.compare(time, pauseEndTime - stopLength) >= 0;
            }
        }
        return false;
    }

    /**
     * Gets the stop length at the given beat.
     * @param beat the stop beat.
     * @return the stop length in seconds at the given beat.
     * Returns 0 if there's no stop at the given beat.
     */
    public double getStopLength(double beat) {
        Preconditions.checkArgument(Double.compare(beat, 0.0D) >= 0, "Beat cannot be less than 0.");
        if(timingData.stops != null) {
            Double stopLength = timingData.stops.get(beat);
            if(stopLength != null) {
                return stopLength;
            }
        }
        return 0.0D;
    }

    /**
     * Checks if there is a delay at the given time.
     * @param time the time.
     * @return true if there is a delay at the given time, false otherwise.
     */
    public boolean isDelay(double time) {
        TreeMap<Double, Double> delayMap = timingData.delays;
        TreeMap<Double, Double> stopMap = timingData.stops;
        Double beat, delayLength, stopLength, pauseEndTime;
        if(delayMap != null) {
            beat = getBeatAt(time);
            delayLength = delayMap.get(beat);
            if(delayLength != null) { //There is a delay at the given beat.
                if(stopMap != null) {
                    stopLength = stopMap.get(beat);
                    if(stopLength != null) { //There is a stop at the given beat.
                        pauseEndTime = getTimeAt(beat); //Can be different from time passed as argument
                        return Double.compare(time, pauseEndTime - stopLength) < 0;
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the delay length at the given beat.
     * @param beat the delay beat.
     * @return the delay length in seconds at the given beat.
     * Returns 0 if there's no delay at the given beat.
     */
    public double getDelayLength(double beat) {
        Preconditions.checkArgument(Double.compare(beat, 0.0D) >= 0, "Beat cannot be less than 0.");
        if(timingData.delays != null) {
            Double delayLength = timingData.delays.get(beat);
            if(delayLength != null) {
                return delayLength;
            }
        }
        return 0.0D;
    }

    /**
     * Checks if there is a warp at the given beat.
     * @param beat the beat.
     * @return true if there is a warp at the given beat, false otherwise.
     */
    public boolean isWarp(double beat) {
        Preconditions.checkArgument(Double.compare(beat, 0.0D) >= 0, "Beat cannot be less than 0.");
        TreeMap<Double, Double> warpMap = timingData.warps;
        if(warpMap != null && !warpMap.isEmpty()) {
            Map.Entry<Double, Double> floorEntry = warpMap.floorEntry(beat);
            if(floorEntry != null) {
                return beat < floorEntry.getKey() + floorEntry.getValue(); //beat < warpEndBeat
            }
        }
        return false;
    }

    /**
     * Utility class for building timing beat graph.
     */
    private static class TimingBeatGraphBuilder {
        private Set<TimingKeyBeat> keyBeats;
        private LineGraph2D beatGraph = new LineGraph2D();
        private double offset;
        private double currentTime = 0.0D;
        private double currentBeat = 0.0D;
        private double currentBps = 0.0D; //beats per second
        private double currentStop = 0.0D;
        private double currentDelay = 0.0D;
        private int warps = 0; //int instead of boolean to prevent issues with intersecting warps.

        TimingBeatGraphBuilder(double offset, Set<TimingKeyBeat> keyBeats) {
            this.offset = offset;
            this.keyBeats = keyBeats;
        }

        LineGraph2D build() {
            //Set offset
            currentTime = -offset;
            beatGraph.putPoint(currentTime, currentBeat);

            for(TimingKeyBeat keyBeat : keyBeats) {
                switch(keyBeat.type) {
                    case BPM_CHANGE:
                        putBpmChange(keyBeat.beat, keyBeat.value);
                        break;
                    case DELAY:
                        putDelay(keyBeat.beat, keyBeat.value);
                        break;
                    case STOP:
                        putStop(keyBeat.beat, keyBeat.value);
                        break;
                    case WARP_START:
                        putWarpStart(keyBeat.beat);
                        break;
                    case WARP_END:
                        putWarpEnd(keyBeat.beat);
                        break;
                }
            }

            Preconditions.checkState(!isWarping(), "Unclosed warp.");

            putPauseSegmentEnd();
            currentBeat += 1.0D;
            currentTime += 1.0D / currentBps;
            beatGraph.putPoint(currentTime, currentBeat);
            return beatGraph;
        }

        /**
         * @param beat the beat where bpm changes.
         * @param newBpm the new bpm value.
         */
        void putBpmChange(Double beat, Double newBpm) {
            if(warps == 0) {
                putPauseSegmentEnd();
                if(currentBps > 0.0D && Double.compare(beat, currentBeat) != 0) {
                    putBpmSegmentEnd(beat);
                }
            }
            currentBps = newBpm / 60.0D; //Convert to bps and update current value
        }

        /**
         * @param beat delay start beat.
         * @param length the length of the delay in seconds.
         */
        void putDelay(Double beat, Double length) {
            if(length > 0.0D) { //Ignore empty delay
                putPause(beat);
                currentDelay = length;
            }
        }

        /**
         * @param beat stop start beat.
         * @param length the length of the stop in seconds.
         */
        void putStop(Double beat, Double length) {
            if(length > 0.0D) { //Ignore empty stop
                putPause(beat);
                currentStop = length;
            }
        }

        /**
         * @param beat the pause start beat.
         */
        void putPause(Double beat) {
            if(isPaused()) {
                if(Double.compare(beat, currentBeat) != 0) {
                    putPauseSegmentEnd();
                } else {
                    return;
                }
            }
            if(isWarping()) {
                beatGraph.putJump(currentTime, beat - currentBeat, false); //Add pause start point.
                currentBeat = beat;
            } else if(Double.compare(beat, currentBeat) != 0){
                putBpmSegmentEnd(beat); //Add pause start point.
            }
        }

        /**
         * @param beat warp start beat.
         */
        void putWarpStart(Double beat) {
            putPauseSegmentEnd();
            if(!isWarping() && Double.compare(beat, currentBeat) != 0) {
                putBpmSegmentEnd(beat);
            }
            warps++;
        }

        /**
         * @param beat warp end beat.
         */
        void putWarpEnd(Double beat) {
            putPauseSegmentEnd();
            if(isWarping()) {
                warps--;
                if(!isWarping()) {
                    beatGraph.putJump(currentTime, beat - currentBeat, false);
                    currentBeat = beat;
                }
            }
        }

        /**
         * @param endBeat bpm segment end beat. It is the beat where bpm change or another type
         *                of segment starts (stop, delay, warp).
         */
        void putBpmSegmentEnd(Double endBeat) {
            currentTime += (endBeat - currentBeat) / currentBps;
            currentBeat = endBeat;
            beatGraph.putPoint(currentTime, currentBeat);
        }

        void putPauseSegmentEnd() {
            if(currentStop > 0.0D || currentDelay > 0.0D) {
                currentTime += currentStop + currentDelay; //At the same beat, delay and stop lengths are summed up.
                currentStop = 0.0D;
                currentDelay = 0.0D;
                beatGraph.putPoint(currentTime, currentBeat); //Add pause end point
            }
        }

        boolean isWarping() {
            return warps > 0;
        }

        boolean isPaused() {
            return currentStop > 0.0D || currentDelay > 0.0D;
        }

        static LineGraph2D build(TimingData data) {
            Set<TimingKeyBeat> keyBeats = new TreeSet<>();
            if(data.bpms != null) {
                for(Map.Entry<Double, Double> entry : data.bpms.entrySet()) {
                    keyBeats.add(new TimingKeyBeat(TimingKeyBeatType.BPM_CHANGE, entry.getKey(), entry.getValue()));
                }
            }
            if(data.delays != null) {
                for(Map.Entry<Double, Double> entry : data.delays.entrySet()) {
                    keyBeats.add(new TimingKeyBeat(TimingKeyBeatType.DELAY, entry.getKey(), entry.getValue()));
                }
            }
            if(data.stops != null) {
                for(Map.Entry<Double, Double> entry : data.stops.entrySet()) {
                    keyBeats.add(new TimingKeyBeat(TimingKeyBeatType.STOP, entry.getKey(), entry.getValue()));
                }
            }
            if(data.warps != null) {
                for(Map.Entry<Double, Double> entry : data.warps.entrySet()) {
                    keyBeats.add(new TimingKeyBeat(TimingKeyBeatType.WARP_START, entry.getKey(), 0.0D));
                    keyBeats.add(new TimingKeyBeat(TimingKeyBeatType.WARP_END, entry.getKey() + entry.getValue(), 0.0D));
                }

            }
            return new TimingBeatGraphBuilder(data.offset, keyBeats).build();
        }
    }

    /**
     * Timing key beats determine how beats are mapped to time.
     * <p> Note: this class has a natural ordering that is inconsistent with equals. </p>
     */
    private static class TimingKeyBeat implements Comparable<TimingKeyBeat> {
        public final TimingKeyBeatType type;
        public final Double beat;
        public final Double value;

        public TimingKeyBeat(TimingKeyBeatType type, Double beat, Double value) {
            Preconditions.checkNotNull(type, "Key beat type cannot be null.");
            Preconditions.checkArgument(Double.compare(beat, 0.0D) >= 0, "Key beat cannot be less than 0.0D.");
            this.type = type;
            this.beat = beat;
            this.value = value;
        }

        @Override
        public int compareTo(@Nonnull TimingKeyBeat o) {
            int result = Double.compare(beat, o.beat);
            if(result == 0) {
                return type.compareTo(o.type);
            }
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof TimingKeyBeat) {
                TimingKeyBeat keyBeat = (TimingKeyBeat) obj;
                return type == keyBeat.type &&
                        Double.compare(beat, keyBeat.beat) == 0;
            }
            return false;
        }
    }

    private enum TimingKeyBeatType {
        BPM_CHANGE,
        DELAY,
        STOP,
        WARP_START,
        WARP_END
    }
}
