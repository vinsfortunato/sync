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
