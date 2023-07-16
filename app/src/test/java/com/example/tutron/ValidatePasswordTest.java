package com.example.tutron;

import static org.junit.Assert.assertEquals;

import org.junit.Test;



public class ValidatePasswordTest {

    Utils funcs = new Utils();

    String pass = "password";

    @Test
    public void TestPassword_empty(){
        assertEquals(-1, funcs.ValidatePass("", ""));
    }
    @Test
    public void TestPassword_pass_not_confirm(){
        assertEquals(-2, funcs.ValidatePass(pass, "password2"));
    }
    @Test
    public void TestPassword_good(){
        assertEquals(0, funcs.ValidatePass(pass, "password"));
    }
}
