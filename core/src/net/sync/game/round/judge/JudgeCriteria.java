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

package net.sync.game.round.judge;

public class JudgeCriteria {
    private double marvelousWindow = 0.0225f; //22.5ms
    private double perfectWindow = 0.045f;    //45ms
    private double greatWindow = 0.090f;      //90ms
    private double goodWindow = 0.135f;       //135ms
    private double booWindow = 0.180f;        //180ms
    private double mineWindow = 0.090f;       //90ms
    private double holdRecover = 0.320f;      //320ms
    private double rollRecover = 0.350f;      //350ms
    private boolean holdRecoverEnabled = true;
    private boolean chordCohesionEnabled = true;

    public double getMarvelousWindow() {
        return marvelousWindow;
    }

    public double getPerfectWindow() {
        return perfectWindow;
    }

    public double getGreatWindow() {
        return greatWindow;
    }

    public double getGoodWindow() {
        return goodWindow;
    }

    public double getBooWindow() {
        return booWindow;
    }

    public double getMineWindow() {
        return mineWindow;
    }

    public double getWorstTapWindow() {
        return getBooWindow();
    }

    public double getHoldRecover() {
        return holdRecover;
    }

    public double getRollRecover() {
        return rollRecover;
    }

    public boolean isHoldRecoverEnabled() {
        return holdRecoverEnabled;
    }

    public boolean isChordCohesionEnabled() {
        return chordCohesionEnabled;
    }

}


