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

package net.touchmania.game;

import com.badlogic.gdx.utils.Array;
import net.touchmania.game.song.Beatmap;
import net.touchmania.game.song.Chart;
import net.touchmania.game.song.note.Note;
import net.touchmania.game.song.note.NotePanel;
import net.touchmania.game.song.sim.DWIParser;
import net.touchmania.game.song.sim.SimParseException;

import java.io.*;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author flood2d
 */
public class DWIParserTest {

    public static void main(String[]args) {
        DWIParser parser = new DWIParser();

        StringBuilder builder = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("AFRONOVA.dwi")));
            String line;
            while((line = br.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            parser.init(builder.toString());
            Array<Chart> charts = parser.parseCharts();
            for(Chart chart : charts) {
                System.out.println(chart.type + " " + chart.difficultyClass);
            }

            Chart chart = charts.get(2);
            Beatmap beatmap = parser.parseBeatmap(chart);
            System.out.println(beatmap);
            TreeMap<Double, Note> notesMap = beatmap.getNotes(NotePanel.LEFT);
            Iterator<Map.Entry<Double, Note>> it = notesMap.entrySet().iterator();
            while(it.hasNext()) {
                System.out.println(it.next().getKey());
            }
        } catch (SimParseException e) {
            e.printStackTrace();
        }

    }
}
