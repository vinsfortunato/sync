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

package net.sync.game;


import net.sync.game.song.Timing;
import net.sync.game.song.TimingData;

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
