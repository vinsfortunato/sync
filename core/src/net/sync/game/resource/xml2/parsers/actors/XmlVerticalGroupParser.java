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

package net.sync.game.resource.xml2.parsers.actors;

import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import net.sync.game.resource.xml2.parsers.XmlLayoutParser;
import net.sync.game.resource.xml2.resolvers.XmlAlignResolver;
import net.sync.game.util.xml.XmlParseException;

public class XmlVerticalGroupParser extends XmlWidgetGroupParser<VerticalGroup> {
    public XmlVerticalGroupParser(XmlLayoutParser layoutParser) {
        super(layoutParser);
    }

    @Override
    protected boolean parseAttribute(VerticalGroup group, String name, String value) throws XmlParseException {
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
            case "align":     group.align(XmlAlignResolver.GLOBAL_ALIGN_RESOLVER.resolve(value));                                 break;
            case "columnAlign": {
                int align = XmlAlignResolver.GLOBAL_ALIGN_RESOLVER.resolve(value);
                if(align == Align.center || align == Align.left || align == Align.right) {
                    group.align(XmlAlignResolver.GLOBAL_ALIGN_RESOLVER.resolve(value));
                    break;
                }
                throw new XmlParseException("Invalid align value! Row align should be left or center or right!");
            }
            default: return false; //Unrecognised attribute
        }

        return true;
    }

    @Override
    protected VerticalGroup createActor() {
        return new VerticalGroup();
    }
}
