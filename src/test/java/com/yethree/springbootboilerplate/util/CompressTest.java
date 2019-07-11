package com.yethree.springbootboilerplate.util;

import org.apache.tomcat.util.buf.HexUtils;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import static java.util.zip.Deflater.SYNC_FLUSH;

class CompressTest {

    @Test
    void test() {
        try {
            // Encode a String into bytes
            String inputString = "blahblahblah??";
            byte[] input = inputString.getBytes(StandardCharsets.UTF_8);

            // Compress the bytes
            byte[] output = new byte[100];
            Deflater compresser = new Deflater();
            compresser.setInput(input);
            compresser.finish();
            int compressedDataLength = compresser.deflate(output, 0, input.length, SYNC_FLUSH);
            System.out.println(compressedDataLength);

            // Decompress the bytes
            Inflater decompresser = new Inflater();

            String hexString = HexUtils.toHexString(output);
            System.out.println(hexString);

            /*output = HexUtils.fromHexString("22664803000000ffff");
            output = HexUtils.fromHexString("789c2266940800");
            output = HexUtils.fromHexString("789c2cbc9b6e08");
            compressedDataLength = output.length - 4;*/

            decompresser.setInput(output, 0, compressedDataLength);
            byte[] result = new byte[100];
            int resultLength = decompresser.inflate(result);
            decompresser.end();

            // Decode the bytes into a String
            String outputString = new String(result, 0, resultLength, StandardCharsets.UTF_8);

            System.out.println(outputString);
        } catch(DataFormatException ex) {
            ex.printStackTrace();
            // handle
        }
    }
}
