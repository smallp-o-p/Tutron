package com.example.tutron;

import org.junit.Test;

import static org.junit.Assert.*;

public class ValidatePaymentInfoTest {

    String cc = "1234098734566789";

    String cvv = "920";

    String expy="30";

    String expm = "6";

    Utils funcs = new Utils();

    @Test
    public void ValidateEmpty(){
        assertEquals(-1, funcs.ValidatePaymentInfo("100", "", "12", "1"));
    }
    @Test
    public void ValidateCCLength(){
        assertEquals(-2, funcs.ValidatePaymentInfo("123409873456678", cvv, expy, expm));
    }

    @Test
    public void ValidateCVV(){
        assertEquals(-3, funcs.ValidatePaymentInfo(cc, "1234", expy, expm));
    }

    @Test
    public void ValidateExpY(){
        assertEquals(-4, funcs.ValidatePaymentInfo(cc, cvv, "12", expm));
    }
    @Test
    public void ValidateExpM(){
        assertEquals(-5, funcs.ValidatePaymentInfo(cc, cvv, expy, "13"));
    }
}
