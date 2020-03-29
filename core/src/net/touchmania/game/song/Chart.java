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

import com.google.common.collect.ComparisonChain;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * @author Vincenzo Fortunato
 */
public class Chart implements Comparable<Chart> {
    /**
     * The song associated to the chart.
     */
    public Song song;
    /**
     * The chart type.
     */
    public ChartType type;
    /**
     * The chart's difficulty class.
     */
    public DifficultyClass difficultyClass;
    /**
     * Chart's beatmap. Can be null. Beatmap is loaded
     * only when it's necessary directly from the SIM file.
     */
    public Beatmap beatmap;
    /**
     * The chart's difficulty as numeric value.
     * Higher values are associated to higher difficulties.
     */
    public int difficultyMeter;
    /**
     * This chart's Name. ("Style - Difficulty" is the default/standard.)
     */
    public String name;
    /**
     * Describes this chart's Type/Style/Difficulty.
     */
    public String description;
    /**
     * Chart's Creators/Credits.
     */
    public String credit;
    /**
     * Other info about the chart.
     */
    public String notes;

    /**
     * For a given song there must be only one chart for a given
     * couple of type and difficulty class. The check is done by
     * comparing these two. Other stuff like name
     * or description is not considered.
     * @param obj another chart.
     * @return true if they represents the same chart.
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(obj instanceof Chart) {
            Chart o = (Chart) obj;
            return Objects.equals(type, o.type) && Objects.equals(difficultyClass, o.difficultyClass);
        }
        return false;
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
