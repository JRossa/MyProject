package org.myproject.model.utils;

import java.util.Random;

public class RandomPasswordGenerator {
    private static final String ALPHA_CAPS  = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ALPHA       = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUM         = "0123456789";
    private static final String SPL_CHARS   = "!@#$%^&*_=+-/";
 
    
    public static String generatePswd(int nPassw) {
        // minLen > noOfCAPSAlpha + noOfDigits + noOfSplChars
        int noOfCAPSAlpha = 3;
        int noOfDigits    = 4;
        int noOfSplChars  = 1;
        int minLen        = 10;
        int maxLen        = 10;
 
        Random rnd = new Random();
        
        int index = rnd.nextInt(nPassw);
        if ((noOfCAPSAlpha + noOfDigits + noOfSplChars) > minLen)
            noOfCAPSAlpha = rnd.nextInt(minLen - (noOfDigits + noOfSplChars));
      
        for (int i = 0; i < nPassw; i++) {

            char[] pswd = generatePswd(minLen, maxLen, noOfCAPSAlpha, noOfDigits, noOfSplChars);
            
            if (i == index)
              return new String(pswd);

            }  

      return null;  
    }
    

    
    public static char[] generatePswd(int minLen, int maxLen, int noOfCAPSAlpha, int noOfDigits, int noOfSplChars) {
        
        if (minLen > maxLen)
            try {
                throw new IllegalArgumentException ("Min. Length > Max. Length!");
            } catch(IllegalArgumentException e) {
                System.out.println("Just caught an IllegalArgumentException..." + e.getMessage());
                return null;
           }
                
        if ((noOfCAPSAlpha + noOfDigits + noOfSplChars) > minLen)
            try {
                throw new IllegalArgumentException ("Min. Length should be atleast sum of (CAPS, DIGITS, SPL CHARS) Length!");
            } catch(IllegalArgumentException e) {
                System.out.println("Just caught an IllegalArgumentException..." + e.getMessage());
                return null;
            }
        
        Random rnd = new Random();
        int len = rnd.nextInt(maxLen - minLen + 1) + minLen;
        char[] pswd = new char[len];
        int index = 0;
        
        for (int i = 0; i < noOfCAPSAlpha; i++) {
            index = getNextIndex(rnd, len, pswd);
            pswd[index] = ALPHA_CAPS.charAt(rnd.nextInt(ALPHA_CAPS.length()));
        }
        for (int i = 0; i < noOfDigits; i++) {
            index = getNextIndex(rnd, len, pswd);
            pswd[index] = NUM.charAt(rnd.nextInt(NUM.length()));
        }
        for (int i = 0; i < noOfSplChars; i++) {
            index = getNextIndex(rnd, len, pswd);
            pswd[index] = SPL_CHARS.charAt(rnd.nextInt(SPL_CHARS.length()));
        }
        for(int i = 0; i < len; i++) {
            if(pswd[i] == 0) {
                pswd[i] = ALPHA.charAt(rnd.nextInt(ALPHA.length()));
            }
        }
        
     return pswd;
    }
     
    
    private static int getNextIndex(Random rnd, int len, char[] pswd) {
        int index = rnd.nextInt(len);
        
        while (pswd[index = rnd.nextInt(len)] != 0);
            return index;
    }
    
    
      
}
