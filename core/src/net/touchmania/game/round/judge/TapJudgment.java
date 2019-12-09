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

public class TapJudgment extends Judgment {
    private double timingError;
    private JudgmentClass judgmentClass;

    /**
     * @param genTime the judgment's generation time relative to the start of the music track.
     */
    public TapJudgment(double genTime, double timingError, JudgmentClass judgmentClass) {
        super(genTime);
        this.timingError = timingError;
        this.judgmentClass = judgmentClass;
    }

    public boolean isEarly() {
        return Double.compare(timingError, 0) > 0;
    }

    public boolean isLate() {
        return Double.compare(timingError, 0) < 0;
    }

    public double getTimingError() {
        return timingError;
    }

    public JudgmentClass getJudgmentClass() {
        return judgmentClass;
    }
}
