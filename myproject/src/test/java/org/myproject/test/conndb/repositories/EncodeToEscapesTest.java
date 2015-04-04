package org.myproject.test.conndb.repositories;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import org.junit.Test;

public class EncodeToEscapesTest {

@Test
public void testEncoding() {
     String src = "Hallo äöü"; // this has to be read with the right encoding
    
    src = "Faça Login";
    System.out.println(src);
    
    final CharsetEncoder asciiEncoder = Charset.forName("US-ASCII").newEncoder();
    final StringBuilder result = new StringBuilder();
    for (final Character character : src.toCharArray()) {
        if (asciiEncoder.canEncode(character)) {
            result.append(character);
        } else {
            result.append("\\u");
            result.append(Integer.toHexString(0x10000 | character).substring(1).toUpperCase());
        }
    }
    
    
    System.out.println(result);
 }
}