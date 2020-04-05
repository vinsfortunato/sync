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

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Parse a sim file content. Each sim format parser must implement this interface.
 * <p> Before calling any of the parsing methods the parser must be initialized by
 * calling {@link #init(SimFile)}. </p>
 * <p> Methods that refer to unavailable values, if specified, must return null. </p>
 * <p> The parser implementation can hold the entire sim file content. Therefore any
 * reference to this interface must be garbage collected when it is no longer needed. </p>
 */
public interface SimParser {
    /**
     * Init the parser. Each implementation can do its own initialization
     * tasks here.
     * @param file the sim file.
     * @throws SimParseException if the sim file cannot be parsed correctly.
     */
    void init(SimFile file) throws SimParseException;

    /**
     * It is an essential song data.
     * @return the primary title of the song.
     * @throws SimParseException if the title cannot be parsed or the sim file doesn't define a title.
     */
    String parseTitle() throws SimParseException;

    /**
     * @return the subtitle of the song (secondary title). Null if not specified.
     */
    String parseSubtitle() throws SimParseException;

    /**
     * @return the artist of the song. Null if not specified.
     */
    String parseArtist() throws SimParseException;

    /**
     * @return the genre of the song. Null if not specified.
     */
    String parseGenre() throws SimParseException;

    /**
     * @return the simfile's origin (author or pack/mix). Null if not specified.
     */
    String parseCredit() throws SimParseException;
    /**
     * @return the path to the banner image for the song. Null if not specified.
     */
    String parseBannerPath() throws SimParseException;

    /**
     * @return the path to the background image for the song. Null if not specified.
     */
    String parseBackgroundPath() throws SimParseException;

    /**
     * @return the path to the lyrics file (.lrc) to use. Null if not specified.
     */
    String parseLyricsPath() throws SimParseException;

    /**
     * @return the path to the CD title, a small image meant to show
     * the origin of the song. Null if not specified.
     */
    String parseAlbum() throws SimParseException;

    /**
     * @return the path to the music file for this song. Null if not specified.
     */
    String parseMusicPath() throws SimParseException;

    /**
     * @return the start time of the song sample used as preview, -1.0 if the
     * value is unavailable or cannot be parsed correctly.
     */
    float parseSampleStart() throws SimParseException;

    /**
     * @return the length of the song sample used as preview, -1.0 if the
     * value is unavailable or cannot be parsed correctly.
     */
    float parseSampleLength() throws SimParseException;

    /**
     * @return true if the song is selectable under normal conditions or the
     * tag value cannot be parsed correctly.
     */
    boolean parseSelectable() throws SimParseException;

    /**
     * Gets all the available chart parsers.
     * @return the available chart parsers
     */
    List<SimChartParser> getChartParsers();

    /**
     * Gets a parser for the given chart data hash.
     * @param hash the hash of the chart data.
     * @return the parser for the given chart, or null if there is no matching parser.
     */
    SimChartParser getChartParser(String hash);

    /**
     * Calls the given Callable and returns default value if a {@link SimParseException} is
     * thrown by the Callable {@link Callable#call()} method.
     * @param callable the callable.
     * @return the parsed value or default value if a {@link SimParseException} is thrown
     * @throws Exception if an exception that is not  {@link SimParseException} is thrown
     */
    static <T> T parseOrDefault(Callable<T> callable, T defaultValue) throws Exception {
        try {
            return callable.call();
        } catch(SimParseException e) {
            return defaultValue;
        }
    }
}
