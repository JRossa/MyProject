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

public class Cryptor {

    public final static Key generateKey() throws NoSuchAlgorithmException {
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        SecureRandom random = new SecureRandom();
        kg.init(random);
        return kg.generateKey();
    }

    public static final String encrypt(final String message, final Key key, final IvParameterSpec iv) throws IllegalBlockSizeException,
            BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException,
            InvalidAlgorithmParameterException {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        byte[] stringBytes = message.getBytes();

        byte[] raw = cipher.doFinal(stringBytes);

        return Base64.encodeBase64String(raw);
    }

    public static final String decrypt(final String encrypted, final Key key, final IvParameterSpec iv) throws InvalidKeyException,
            NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException,
            InvalidAlgorithmParameterException {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);

        byte[] raw = Base64.decodeBase64(encrypted);

        byte[] stringBytes = cipher.doFinal(raw);

        String clearText = new String(stringBytes, "UTF8");
        return clearText;
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
        String encryptedString = Cryptor.encrypt(clearText, k, iv);
        System.out.println("Encrypted String:  " + encryptedString);
        System.out.println("Decrypted String:  " + Cryptor.decrypt(encryptedString, k, iv));
    }

    public String EncryptString(String clearText) throws Exception {
        Key k = Cryptor.generateKey();
        SecureRandom random = new SecureRandom();
        IvParameterSpec iv = new IvParameterSpec(random.generateSeed(16));

        return Cryptor.encrypt(clearText, k, iv);
    }

    public String DecryptString(String encryptedString) throws Exception {
        Key k = Cryptor.generateKey();
        SecureRandom random = new SecureRandom();
        IvParameterSpec iv = new IvParameterSpec(random.generateSeed(16));

        return Cryptor.decrypt(encryptedString, k, iv);
    }

}