package org.myproject.test.conndb.repositories;

import org.junit.Test;
import org.myproject.model.utils.Cryptor;
import org.myproject.model.utils.PasswordHash;

//public static void main(final String[] args) {
//    final String iv = "0123456789123456"; // This has to be 16 characters
//    final String secretKey = "Replace this by your secret key";
//    final Crypto crypto = new Crypto();
//
//    final String encryptedData = crypto.encrypt("This is a test message.", iv, secretKey);
//    System.out.println(encryptedData);
//
//    final String decryptedData = crypto.decrypt(encryptedData, iv, secretKey);
//    System.out.println(decryptedData);
//}

public class CryptoTest {

    
    public void Test1() throws Exception {

        Cryptor c = new Cryptor();

        c.Teste();

    }

}
