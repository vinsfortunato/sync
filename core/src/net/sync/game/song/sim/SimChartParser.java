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
