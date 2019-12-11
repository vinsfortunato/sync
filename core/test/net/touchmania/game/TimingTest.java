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


import net.touchmania.game.song.InvalidTimingDataException;
import net.touchmania.game.song.Timing;
import net.touchmania.game.song.TimingData;

/**
 * @author Vincenzo Fortunato
 */
public class TimingTest {

    public static void main(String[]args) {
        TimingData data = new TimingData();

        data.offset = -0.353;

        //Bpm
        data.putBpm(0.0, 185.0);
        data.putBpm(141.0, 92.5);
        data.putBpm(173.0, 185.0);
        data.putBpm(315.0, 88.0);
        data.putBpm(323.0, 85.0);
        data.putBpm(328.5, 83.0);

        //Delay
        data.putDelay(30.0, 11.0);
        data.putDelay(81.0, 2.0);

        //Stop
        data.putStop(9.0, 3.0);
        data.putStop(17.0, 3.0);
        data.putStop(30.0, 10.0);
        data.putStop(45.0, 3.0);
        data.putStop(78.0, 5.0);

        //Warp
        data.putWarp(13.0, 5.0);
        data.putWarp(42.0, 5.0);
        data.putWarp(43.0, 10.0);

        try {
            Timing timing = new Timing(data);
            //GraphVisualizer.showGraph(timing.timeGraph);

            for(double i = 0.0D; i < 162.0D; i+= 1.0D) {
                System.out.println("Time " + i + " = " + timing.getBeatAt(i) + " beat");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
