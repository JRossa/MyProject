package org.myproject.test.conndb.repositories;

import java.util.Random;



import org.junit.Test;
import org.myproject.model.utils.RandomPasswordGenerator;


public class RandomPasswordGeneratorTest {
    

    public void Test1 () {
        int nPassw        = 30;
        int noOfCAPSAlpha = 3;
        int noOfDigits    = 4;
        int noOfSplChars  = 1;
        int minLen        = 10;
        int maxLen        = 15;
 
        Random rnd = new Random();
        int index = rnd.nextInt(nPassw);
        noOfCAPSAlpha = rnd.nextInt(6);
        
        for (int i = 0; i < nPassw; i++) {
            char[] pswd = RandomPasswordGenerator.generatePswd(minLen, maxLen,
                                                     noOfCAPSAlpha, noOfDigits, noOfSplChars);
            if (i == index)
              System.out.println("Len " + "(" + index + ")" + "= " + pswd.length + ", " + new String(pswd));
        }
    }
    
    @Test
    public void Test2 () {
 
       System.out.println("Passwd " + RandomPasswordGenerator.generatePswd(30));
        
    }    
}
