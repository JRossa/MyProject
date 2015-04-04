package org.myproject.test.conndb.repositories;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.myproject.model.utils.Cryptor;
import org.myproject.model.utils.PasswordHash;


public class PasswordHashTest {

    private static final Logger LOGGER = Logger.getLogger(PasswordHashTest.class);
    
    @Test
    public void Test1() throws Exception {

        String hash = PasswordHash.md5("Bomdia");
        
        System.out.println(hash);

        System.out.println(hash.length());
    }

    @Test
    public void Test2() 
            throws NoSuchAlgorithmException, InvalidKeySpecException  {

        String hash = PasswordHash.createHash("Bomdia");
        
        System.out.println(hash);
        
        System.out.println(PasswordHash.validatePassword("Bomdia", hash));
        System.out.println(PasswordHash.validatePassword("admin", "admin"));
        
        System.out.println(hash.length());
    }
}
