package org.myproject.model.utils;

import myorg.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Cryptor {

	private static String key1 = "MZygpewJsCpRrfOr"; // 128 bit key
    private static String key2 = "MyProjecSecreKey";
    
    
    public final static Key generateKey() throws NoSuchAlgorithmException {
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        SecureRandom random = new SecureRandom();
        kg.init(random);
        return kg.generateKey();
    }

    public static final String encrypt2(final String message, final Key key, final IvParameterSpec iv) throws IllegalBlockSizeException,
            BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException,
            InvalidAlgorithmParameterException {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        byte[] stringBytes = message.getBytes();

        byte[] raw = cipher.doFinal(stringBytes);

        return Base64.encodeBase64String(raw);
    }

    
    public static final String decrypt2(final String encrypted, final Key key, final IvParameterSpec iv) throws InvalidKeyException,
            NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException,
            InvalidAlgorithmParameterException {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);

        byte[] raw = Base64.decodeBase64(encrypted);

        byte[] stringBytes = cipher.doFinal(raw);

        String clearText = new String(stringBytes, "UTF8");
        return clearText;
    }
    

    private static String encrypt(String key1, String key2, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(key2.getBytes("UTF-8"));

            SecretKeySpec skeySpec = new SecretKeySpec(key1.getBytes("UTF-8"), "AES");
            
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            
            byte[] encrypted = cipher.doFinal(value.getBytes());
//            System.out.println("encrypted string:" + Base64.encodeBase64String(encrypted));
            
            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    
    public static final String EncryptString(String clearText) throws Exception {
        
        return Cryptor.encrypt(key1, key2, clearText);
    }
    

    private static String decrypt(String key1, String key2, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(key2.getBytes("UTF-8"));

            SecretKeySpec skeySpec = new SecretKeySpec(key1.getBytes("UTF-8"),
                    "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    
    public static final String DecryptString(String encryptedString) throws Exception {

        return Cryptor.decrypt(key1, key2, encryptedString);
    }

    
     
    /**
     * @param args
     * @throws SQLException
     * @throws IOException
     */
    public void Teste() throws Exception {
        Key k = Cryptor.generateKey();
        SecureRandom random = new SecureRandom();
        IvParameterSpec iv = new IvParameterSpec(random.generateSeed(16));

        String clearText = "rossa.jmr@mail.exercito.pt";
        System.out.println("Clear Text      :  " + clearText);
        String encryptedString = Cryptor.encrypt2(clearText, k, iv);
        System.out.println("Encrypted String:  " + encryptedString);
        System.out.println("Decrypted String:  " + Cryptor.decrypt2(encryptedString, k, iv));
    }

}