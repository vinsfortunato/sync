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

/**
 * Supported sim formats ordered by preference.
 *
 * @author Vincenzo Fortunato
 */
public enum SimFormat {
    SM("sm"),
    SSC("ssc"),
    DWI("dwi");

    /**
     * The file extension used to identify the sim file format.
     */
    public final String fileExtension;

    SimFormat(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    /**
     * Gets a SimParser that is able to parse a file with this format.
     * @return a clean parser instance.
     */
    public SimParser newParser() {
        switch(this) {
            case SM:
                return new SMParser();
            case SSC:
                return new SSCParser();
            case DWI:
                return new DWIParser();
        }
        throw new IllegalStateException("There is no parser for the given format");
    }

    /**
     * Gets a SimFormat from the given file extension.
     * @param fileExtension the sim format file extension.
     * @return a SimFormat or null if there's no supported
     * SimFormat with the given file extension.
     */
    public static SimFormat valueFromExtension(String fileExtension) {
        for(SimFormat format : values()) {
            if(format.fileExtension.equalsIgnoreCase(fileExtension)) {
                return format;
            }
        }
        return null;
    }
}
