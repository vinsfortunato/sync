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

package net.touchmania.game.test;

import java.io.*;
import java.util.Random;
import java.util.zip.Deflater;

public class InflaterTest {
    public static void main(String[]args) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        Random rnd = new Random();
        for(int i = 0; i < 100000; i++) {
            dos.writeByte(rnd.nextInt(9));
            dos.writeDouble(rnd.nextDouble());
            dos.writeDouble(rnd.nextDouble());
        }
        dos.close();

        System.out.println("Uncompressed size: " + bos.toByteArray().length);

        Deflater deflater = new Deflater();
        deflater.setInput(bos.toByteArray());

        bos = new ByteArrayOutputStream(bos.size());
        deflater.finish();
        byte[] buffer = new byte[1024];
        while(!deflater.finished()) {
            int count = deflater.deflate(buffer);
            bos.write(buffer, 0, count);

        }
        bos.close();
        System.out.println("Compressed size: " + bos.toByteArray().length);
    }
}
