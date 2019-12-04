/*
 * Copyright 2019 Vincenzo Fortunato
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

package net.touchmania.game.round.judge;

public class JudgeCriteria {
    private double marvelousWindow = 0.0225f; //22.5ms
    private double perfectWindow = 0.045f;    //45ms
    private double greatWindow = 0.090f;      //90ms
    private double goodWindow = 0.135f;       //135ms
    private double booWindow = 0.180f;        //180ms
    private double mineWindow = 0.090f;       //90ms
    private double holdRecover = 0.250f;      //250ms
    private boolean holdRecoverEnabled = true;

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

    public double getHoldRecover() {
        return holdRecover;
    }

    public boolean isHoldRecoverEnabled() {
        return holdRecoverEnabled;
    }

    public double getWorstTapWindow() {
        return getBooWindow();
    }
}


