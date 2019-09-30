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

import net.touchmania.game.song.DifficultyClass;

/**
 * Utility class used by every sim parser implementation.
 * @author flood2d
 */
public final class SimParserUtils {
    /**
     * Parse a float value.
     * @param value  the value to parse
     * @return the parsed value
     * @throws SimParseException if the value cannot be parsed correctly.
     */
    public static float parseFloat(String value) throws SimParseException {
        try {
            return Float.parseFloat(value);
        } catch(NumberFormatException e) {
            throw new SimParseException("The value cannot be parsed as float!");
        }
    }

    /**
     * Parse a double value.
     * @param value  the value to parse
     * @return the parsed value
     * @throws SimParseException if the value cannot be parsed correctly.
     */
    public static  double parseDouble(String value) throws SimParseException {
        try {
            return Double.parseDouble(value);
        } catch(NumberFormatException e) {
            throw new SimParseException("The value cannot be parsed as double!");
        }
    }

    /**
     * Parse a int value.
     * @param value  the value to parse
     * @return the parsed value
     * @throws SimParseException if the value cannot be parsed correctly.
     */
    public static int parseInt(String value) throws SimParseException {
        try {
            return Integer.parseInt(value);
        } catch(NumberFormatException e) {
            throw new SimParseException("The value cannot be parsed as integer!");
        }
    }

    /**
     * Parse difficulty class.
     * @param value the value to parse
     * @return the difficulty class.
     * @throws SimParseException if the difficulty class cannot be recognised.
     */
    public static DifficultyClass parseDifficultyClass(String value) throws SimParseException {
        switch(value.toUpperCase()) {
            case "BEGINNER":
                return DifficultyClass.BEGINNER;
            case "EASY":
            case "BASIC":
            case "LIGHT":
                return DifficultyClass.EASY;
            case "MEDIUM":
            case "ANOTHER":
            case "TRICK":
            case "STANDARD":
            case "DIFFICULT":
                return DifficultyClass.MEDIUM;
            case "HARD":
            case "SSR":
            case "MANIAC":
            case "HEAVY":
                return DifficultyClass.HARD;
            case "SMANIAC":
            case "CHALLENGE":
            case "EXPERT":
            case "ONI":
                return DifficultyClass.CHALLENGE;
            case "EDIT":
                return DifficultyClass.EDIT;
        }

        throw new SimParseException("Unrecognised difficulty class!");
    }
}
