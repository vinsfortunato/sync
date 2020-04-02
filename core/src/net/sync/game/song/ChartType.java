/*
 * Copyright 2020 Vincenzo Fortunato
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

package net.sync.game.song;

import net.sync.game.GameMode;

/**
 * Chart type in the format "GameMode_Style". Enums the supported game styles.
 * @author Vincenzo Fortunato
 */
public enum ChartType {
    DANCE_SINGLE(GameMode.DANCE, 4),
    PUMP_SINGLE(GameMode.PUMP, 5);

    public final GameMode gameMode;
    public final int panels;

    ChartType(GameMode gameMode, int panels) {
        this.gameMode = gameMode;
        this.panels = panels;
    }
}
