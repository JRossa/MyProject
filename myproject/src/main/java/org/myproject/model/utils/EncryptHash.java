package org.myproject.model.utils;

import java.security.MessageDigest;

public class EncryptHash {

    public EncryptHash () {
        
    }
    
    private static String EncryptPassword(String password, String hashType) {
        String encryptedPassword;
        MessageDigest msgDig = null;
        String salt  = "MyProJECT!Random$SaltValue#WithSpecialCharacters12@$@4&#%^$*";
        
        password = password + salt;
        
        try {
            
            // ATENÇÃO: Retirar no final
            //msgDig = MessageDigest.getInstance("MD5");
            //MessageDigest msgDig = MessageDigest.getInstance("SHA-256");
            msgDig = MessageDigest.getInstance(hashType);

            msgDig.update(password.getBytes("UTF-8"));
            
            // Gera caracteres não alfanuméricos
            //encryptedPassword = new String(msgDig.digest());
            
            byte array[] = msgDig.digest(password.getBytes()); 
            StringBuffer sb = new StringBuffer();
            
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }

            encryptedPassword = sb.toString();
            System.out.println("Password : " + encryptedPassword + "   " + encryptedPassword.length());

        } catch (Exception ex) {
            encryptedPassword = "";
        }

        return encryptedPassword;
    }

    public static String md5(String txt) {
        return EncryptHash.EncryptPassword(txt, "MD5");
    }

    public static String sha1(String txt) {
        return EncryptHash.EncryptPassword(txt, "SHA1");
    }

    public static String sha2(String txt) {
        return EncryptHash.EncryptPassword(txt, "SHA-256");
    }
}
