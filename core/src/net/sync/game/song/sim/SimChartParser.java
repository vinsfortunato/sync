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

package net.sync.game.song.sim;

import net.sync.game.song.Beatmap;

/**
 * Parse a sim chart content.
 * <p> Before calling any of the parsing methods the parser must be initialized by
 * calling {@link #init()}. </p>
 * <p> Methods that refer to unavailable values, if specified, must return null. </p>
 * <p> The parser implementation can hold the chart raw data content. Therefore any
 * reference to this interface must be garbage collected when it is no longer needed. </p>
 */
public interface SimChartParser {

    void init() throws SimParseException;

    net.sync.game.song.ChartType parseChartType() throws SimParseException;

    net.sync.game.song.DifficultyClass parseDifficultyClass() throws SimParseException;

    net.sync.game.song.TimingData parseTimingData() throws SimParseException;

    Beatmap parseBeatmap() throws SimParseException;

    /**
     * This can be used to override the BPM shown on song selection screen.
     * <p> Check {@link net.sync.game.song.DisplayBPM DisplayBPM} class for more details. </p>
     * @return an instance of {@link net.sync.game.song.DisplayBPM} or null if there's no need
     * to override the default behavior or the value cannot be parsed correctly.
     */
    net.sync.game.song.DisplayBPM parseDisplayBPM() throws SimParseException;

    int parseDifficultyMeter() throws SimParseException;

    String parseName() throws SimParseException;

    String parseDescription() throws SimParseException;

    String parseCredit() throws SimParseException;

    /**
     * Get the chart raw data hash.
     * @return the computed hash of the chart raw content.
     */
    String getHash();
}
