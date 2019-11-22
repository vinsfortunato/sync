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

package net.touchmania.game.ui;

public enum ScreenCachePolicy {
    /**
     * Screens are prepared only one time and they will be
     * disposed only when the application is closed/paused.
     * Should be used when a lot of memory is available to
     * provide a smooth and faster user experience.
     */
    KEEP_IN_MEMORY,
    /**
     * Screens are automatically disposed after hiding and will
     * be prepared always before showing. Should be used to
     * minimize memory usage.
     */
    DISPOSE_ON_HIDE
}
