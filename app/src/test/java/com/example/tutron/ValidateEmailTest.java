package com.example.tutron;

import org.junit.Test;


import static org.junit.Assert.*;


public class ValidateEmailTest {


    Utils funcs = new Utils();
    String bad_email = ".foo@uca";
    String good_email = "lulw@uottawa.ca";

    @Test
    public void TestEmail_bad(){
        assertEquals(-2,funcs.ValidateEmail(bad_email));
    }
    @Test
    public void TestEmail_empty(){
        assertEquals(-1, funcs.ValidateEmail(""));
    }
    @Test
    public void TestEmail_good(){
        assertEquals(0, funcs.ValidateEmail(good_email));
    }
}
