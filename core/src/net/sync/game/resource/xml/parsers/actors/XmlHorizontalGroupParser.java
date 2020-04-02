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

package net.sync.game.resource.xml.parsers.actors;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.utils.Align;
import net.sync.game.resource.xml.parsers.XmlLayoutParser;
import net.sync.game.util.xml.XmlParseException;

import static net.sync.game.resource.xml.resolvers.XmlAlignResolver.GLOBAL_ALIGN_RESOLVER;

public class XmlHorizontalGroupParser extends XmlWidgetGroupParser<HorizontalGroup> {
    public XmlHorizontalGroupParser(XmlLayoutParser layoutParser) {
        super(layoutParser);
    }

    @Override
    protected boolean parseAttribute(HorizontalGroup group, String name, String value) throws XmlParseException {
        if(super.parseAttribute(group, name, value)) {
            //Attribute already parsed
            return true;
        }

        switch(name) {
            case "expand":    group.expand(booleanResolver().resolve(value));                                    break;
            case "fill":      group.fill(floatResolver().resolve(value));                                        break;
            case "reverse":   group.reverse(booleanResolver().resolve(value));                                   break;
            case "round":     group.setRound(booleanResolver().resolve(value));                                  break;
            case "space":     group.space(dimensionResolver().resolve(value).getValue());                        break;
            case "wrap":      group.wrap(booleanResolver().resolve(value));                                      break;
            case "wrapSpace": group.wrapSpace(dimensionResolver().resolve(value).getValue());                    break;
            case "pad":       group.pad(dimensionResolver().resolve(value).getValue());                          break;
            case "padLeft":   group.padLeft(dimensionResolver().resolve(value).getValue());                      break;
            case "padRight":  group.padRight(dimensionResolver().resolve(value).getValue());                     break;
            case "padTop":    group.padTop(dimensionResolver().resolve(value).getValue());                       break;
            case "padBottom": group.padBottom(dimensionResolver().resolve(value).getValue());                    break;
            case "align":     group.align(GLOBAL_ALIGN_RESOLVER.resolve(value));                                 break;
            case "rowAlign": {
                int align = GLOBAL_ALIGN_RESOLVER.resolve(value);
                if(align == Align.center || align == Align.bottom || align == Align.top) {
                    group.align(GLOBAL_ALIGN_RESOLVER.resolve(value));
                    break;
                }
                throw new XmlParseException("Invalid align value! Row align should be bottom or center or top!");
            }
            default: return false; //Unrecognised attribute
        }
        return true;
    }

    @Override
    protected HorizontalGroup createActor() {
        return new HorizontalGroup();
    }
}
