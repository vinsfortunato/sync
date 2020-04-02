/*
 * Copyright 2020 Vincenzo Fortunato
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

package net.sync.game.song;

import com.google.common.collect.ComparisonChain;
import net.sync.game.song.sim.SimChartParser;
import net.sync.game.song.sim.SimParser;

import javax.annotation.Nonnull;
import java.util.Objects;

public class Chart implements Comparable<Chart> {
    /** Uniquely identified this chart. */
    public String id;
    /** The hash of the sim file chart data that generated this chart.
     * It is used to get the appropriate {@link SimChartParser} when
     * calling {@link SimParser#getChartParser(String)}. */
    public String hash;
    /** The song associated to the chart */
    public Song song;
    /** The chart type */
    public ChartType type;
    /**  The chart's difficulty class */
    public DifficultyClass difficultyClass;
    /** The chart's difficulty as numeric value. */
    public int difficultyMeter;
    /** The name of the chart  */
    public String name;
    /** A chart description. */
    public String description;
    /** Chart's credits such as chart artist and contributors */
    public String credit;
    /** Chart's display BPM */
    public DisplayBPM displayBPM;
    /** Chart's timing data */
    public TimingData timingData = new TimingData();
    /** Chart's beatmap. Can be null. Beatmap is loaded
     *  only when it's necessary directly from the SIM file. */
    public Beatmap beatmap;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chart chart = (Chart) o;
        return Objects.equals(id, chart.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * A list of chart is ordered using their type and
     * difficulty class. Chart type has higher priority.
     */
    @Override
    public int compareTo(@Nonnull Chart o) {
        return ComparisonChain.start()
                .compare(type, o.type)
                .compare(difficultyClass, o.difficultyClass)
                .result();
    }
}
