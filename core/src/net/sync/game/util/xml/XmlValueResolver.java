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

package net.sync.game.util.xml;

/**
 * Resolve a value. It is the base interface used to parse or resolve
 * attribute and element's values in xml documents.
 * @param <T> the type of the value to resolve.
 */
public interface XmlValueResolver<T> {
    /**
     * Resolve a value by parsing a simple value or by resolving it
     * in a more complex way.
     * @param value the value to resolve, not null, not empty.
     * @return the resolved value, never null.
     * @throws XmlDeserializeException if the value cannot be resolved correctly.
     */
    T resolve(String value);
}
