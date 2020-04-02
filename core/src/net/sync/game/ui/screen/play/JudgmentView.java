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

package net.sync.game.ui.screen.play;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import net.sync.game.Game;
import net.sync.game.resource.ResourceProvider;
import net.sync.game.resource.lazy.Resource;
import net.sync.game.round.Round;
import net.sync.game.round.judge.Judgment;
import net.sync.game.round.judge.JudgmentClass;
import net.sync.game.round.judge.TailJudgment;
import net.sync.game.round.judge.TapJudgment;

public class JudgmentView extends Widget {
    private net.sync.game.round.Round round;

    private net.sync.game.resource.lazy.Resource<Drawable> marvelousDrawable;
    private net.sync.game.resource.lazy.Resource<Drawable> perfectDrawable;
    private net.sync.game.resource.lazy.Resource<Drawable> greatDrawable;
    private net.sync.game.resource.lazy.Resource<Drawable> goodDrawable;
    private net.sync.game.resource.lazy.Resource<Drawable> booDrawable;
    private net.sync.game.resource.lazy.Resource<Drawable> missDrawable;
    private net.sync.game.resource.lazy.Resource<Drawable> okDrawable;
    private Resource<Drawable> ngDrawable;

    public JudgmentView(Round round) {
        super();
        this.round = round;

        //Init resources
        ResourceProvider resources = Game.instance().getResources();
        (marvelousDrawable = resources.getDrawable("play_dance_judgment_marvelous")).load();
        (perfectDrawable = resources.getDrawable("play_dance_judgment_perfect")).load();
        (greatDrawable = resources.getDrawable("play_dance_judgment_great")).load();
        (goodDrawable = resources.getDrawable("play_dance_judgment_good")).load();
        (booDrawable = resources.getDrawable("play_dance_judgment_boo")).load();
        (missDrawable = resources.getDrawable("play_dance_judgment_miss")).load();
        (okDrawable = resources.getDrawable("play_dance_judgment_ok")).load();
        (ngDrawable = resources.getDrawable("play_dance_judgment_ng")).load();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        //Draw judgment
        Judgment lastJudgment = round.getJudge().getLastJudgment();

        net.sync.game.round.judge.JudgmentClass c = null;
        if(lastJudgment instanceof net.sync.game.round.judge.TapJudgment) {
            net.sync.game.round.judge.TapJudgment judgment = (TapJudgment) lastJudgment;
            c = judgment.getJudgmentClass();
        } else if(lastJudgment instanceof net.sync.game.round.judge.TailJudgment) {
            net.sync.game.round.judge.TailJudgment judgment = (TailJudgment) lastJudgment;
            c = judgment.getJudgmentClass();
        }

        if(c != null) {
            Drawable drawable = getJudgmentDrawable(c);
            if(drawable == null) return; //TODO

            //Set opacity
            Color color = new Color(batch.getColor());
            color.a = 1.0f;
            batch.setColor(color);

            //Draw
            float x = getWidth() / 2 - drawable.getMinWidth() / 2;
            float y = getHeight() / 2 - drawable.getMinHeight() / 2;
            float w = drawable.getMinWidth();
            float h = drawable.getMinHeight();
            drawable.draw(batch, x, y, w, h);
        }
    }

    private Drawable getJudgmentDrawable(JudgmentClass judgmentClass) {
        switch(judgmentClass) {
            case MARVELOUS: return marvelousDrawable.get();
            case PERFECT:   return perfectDrawable.get();
            case GREAT:     return greatDrawable.get();
            case GOOD:      return goodDrawable.get();
            case BOO:       return booDrawable.get();
            case MISS:      return missDrawable.get();
            case OK:        return okDrawable.get();
            case NG:        return ngDrawable.get();
        }
        return null;
    }
}
