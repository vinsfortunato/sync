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

package net.touchmania.game.song.sim;

import com.badlogic.gdx.utils.Array;
import net.touchmania.game.song.Beatmap;
import net.touchmania.game.song.Chart;
import net.touchmania.game.song.DisplayBPM;
import net.touchmania.game.song.TimingData;

import java.util.List;

/**
 * Parse a sim file content. Each sim format parser must implement this interface.
 * Methods that refer to unavailable values, if specified, must return null.
 * <p> The parser implementation can hold the entire sim file content. Therefore any
 * reference to this interface must be garbage collected when it is no longer needed. </p>
 */
public interface SimParser {
    /**
     * Init the parser. Each implementation can do its own initialization
     * tasks here. The single string argument given to the method is
     * the content of the sim file.
     * @param rawContent the sim file content.
     * @throws SimParseException if the raw content cannot be parsed correctly.
     */
    void init(String rawContent) throws SimParseException;

    /**
     * It is an essential song data.
     * @return the primary title of the song.
     * @throws SimParseException if the title cannot be parsed or the sim file doesn't define a title.
     */
    String parseTitle() throws SimParseException;

    /**
     * @return the subtitle of the song (secondary title). Null if not specified.
     */
    String parseSubtitle();

    /**
     * @return the artist of the song. Null if not specified.
     */
    String parseArtist();

    /**
     * @return the genre of the song. Null if not specified.
     */
    String parseGenre();

    /**
     * @return the simfile's origin (author or pack/mix). Null if not specified.
     */
    String parseCredit();
    /**
     * @return the path to the banner image for the song. Null if not specified.
     */
    String parseBannerPath();

    /**
     * @return the path to the background image for the song. Null if not specified.
     */
    String parseBackgroundPath();

    /**
     * @return the path to the lyrics file (.lrc) to use. Null if not specified.
     */
    String parseLyricsPath();

    /**
     * @return the path to the CD title, a small image meant to show
     * the origin of the song. Null if not specified.
     */
    String parseCdTitle();

    /**
     * @return the path to the music file for this song. Null if not specified.
     */
    String parseMusicPath();

    /**
     * @return the start time of the song sample used as preview, -1.0 if the
     * value is unavailable or cannot be parsed correctly.
     */
    float parseSampleStart();

    /**
     * @return the length of the song sample used as preview, -1.0 if the
     * value is unavailable or cannot be parsed correctly.
     */
    float parseSampleLength();

    /**
     * This can be used to override the BPM shown on song selection screen.
     * <p> Check {@link DisplayBPM DisplayBPM} class for more details. </p>
     * @return an instance of {@link DisplayBPM} or null if there's no need
     * to override the default behavior or the value cannot be parsed correctly.
     */
    DisplayBPM parseDisplayBPM();

    /**
     * @return true if the song is selectable under normal conditions or the
     * tag value cannot be parsed correctly.
     */
    boolean parseSelectable();

    /**
     * Parse song global timing data (BPMs, stops, delays, warps). This timing data
     * will be applied to each song's chart if not overridden. It is an essential song data.
     * @return the timing data.
     * @throws SimParseException if the timing data cannot be parsed or there isn't
     * essential timing data like BPMs.
     */
    TimingData parseTimingData() throws SimParseException;

    /**
     * Parse the available and supported charts of the song. Beatmaps will
     * not be parsed and must be parsed when needed by using {@link #parseBeatmap(Chart)}.
     * Invalid and unsupported charts will be ignored.
     * @return an {@link Array} containing all the available charts of the song, can be empty if
     * there is no supported chart or supported charts are invalid.
     */
    List<Chart> parseCharts();

    /**
     * Parse a beatmap. This method will search the given chart into the sim file. If the
     * given chart is found the parsed beatmap related to the chart will be returned, otherwise
     * returns null. Beatmaps must be parsed only when needed because they take up
     * a lot of space.
     * @param chart the beatmap's chart.
     * @return a beatmap related to the given chart, null if the chart han not been found.
     * @throws SimParseException if the chart has been found but the beatmap cannot be parsed correctly.
     */
    Beatmap parseBeatmap(Chart chart) throws SimParseException;
}
