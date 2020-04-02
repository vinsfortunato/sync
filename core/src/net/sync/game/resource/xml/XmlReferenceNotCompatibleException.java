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

package net.sync.game.resource.xml;

import net.sync.game.util.xml.XmlParseException;

public class XmlReferenceNotCompatibleException extends XmlParseException {

    public XmlReferenceNotCompatibleException() {}

    public XmlReferenceNotCompatibleException(String message) {
        super(message);
    }

    public XmlReferenceNotCompatibleException(String message, Throwable cause) {
        super(message, cause);
    }

    public XmlReferenceNotCompatibleException(Throwable cause) {
        super(cause);
    }

    public static XmlReferenceNotCompatibleException incompatibleType(Class<?> type, Class<?> expected) {
        return new XmlReferenceNotCompatibleException(String.format(
                "Incompatible reference! Trying to convert '%s' to '%s'.", type.getName(), expected.getName()));
    }
}
