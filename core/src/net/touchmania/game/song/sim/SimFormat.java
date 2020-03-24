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
