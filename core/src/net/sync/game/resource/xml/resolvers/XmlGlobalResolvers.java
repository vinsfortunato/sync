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

package net.sync.game.resource.xml.resolvers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import net.sync.game.resource.Dimension;
import net.sync.game.util.xml.XmlValueResolver;
import java.util.Locale;

public class XmlGlobalResolvers {
    public static final XmlValueResolver<Integer> GLOBAL_ALIGN_RESOLVER = new XmlAlignResolver();
    public static final XmlValueResolver<Boolean> GLOBAL_BOOLEAN_RESOLVER = new XmlBooleanResolver();
    public static final XmlValueResolver<Color> GLOBAL_COLOR_RESOLVER = new XmlColorResolver();
    public static final XmlValueResolver<Dimension> GLOBAL_DIMENSION_RESOLVER = new XmlDimensionResolver();
    public static final XmlValueResolver<Long> GLOBAL_DURATION_RESOLVER = new XmlDurationResolver();
    public static final XmlValueResolver<Float> GLOBAL_FLOAT_RESOLVER = new XmlFloatResolver();
    public static final XmlValueResolver<String> GLOBAL_IDENTIFIER_RESOLVER = new XmlIdentifierResolver();
    public static final XmlValueResolver<Integer> GLOBAL_INTEGER_RESOLVER = new XmlIntegerResolver();
    public static final XmlValueResolver<Locale> GLOBAL_LOCALE_RESOLVER = new XmlLocaleResolver();
    public static final XmlValueResolver<Float> GLOBAL_PERCENT_RESOLVER = new XmlPercentResolver();
    public static final XmlValueResolver<Pixmap.Format> GLOBAL_PIXMAP_RESOLVER = new XmlPixmapFormatResolver();
    public static final XmlValueResolver<Texture.TextureFilter> GLOBAL_TEXTURE_FILTER_RESOLVER = new XmlTextureFilterResolver();
    public static final XmlValueResolver<Texture.TextureWrap> GLOBAL_TEXTURE_WRAP_RESOLVER = new XmlTextureWrapResolver();
    public static final XmlValueResolver<Touchable> GLOBAL_TOUCHABLE_RESOLVER = new XmlTouchableResolver();
}
