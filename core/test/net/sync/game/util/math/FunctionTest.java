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

package net.sync.game.util.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FunctionTest {

    @Test
    public void testStepFunction() {
        //StepFunction double -> boolean with default value set to false
        StepFunction<Double, Boolean> function = new StepFunction<>(false);

        function.putStep(1.0D, true);
        function.putStep(2.0D, false);
        function.putStep(3.0D, true);
        function.putStep(3.5D, false);

        assertEquals(function.f(0.99999D), false);
        assertEquals(function.f(1.0D), true);
        assertEquals(function.f(1.5D), true);
        assertEquals(function.f(2.1D), false);
        assertEquals(function.f(3.0D), true);
        assertEquals(function.f(3.0001D), true);
        assertEquals(function.f(3.56D), false);
    }
}
