package com.example.tutron;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public int ValidateEmail(String email) {
        if (email.isEmpty()) {
            return -1;
        }
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            return -2;
        }
        return 0;
    }

    public int ValidatePaymentInfo(String cc, String cvv, String expy, String expm) {
        Calendar cal = Calendar.getInstance();
        if (cc.isEmpty() || cvv.isEmpty() || expy.isEmpty() || expm.isEmpty()) {
            return -1;
        }
        if (cc.length() != 16) {
            return -2;
        }
        if (cvv.length() != 3) {
            return -3;
        }
        if (Integer.parseInt("20" + expy) < cal.get(Calendar.YEAR)) {
            return -4;
        }
        if (Integer.parseInt(expm) > 12 || Integer.parseInt(expm) < 1) {
            return -5;
        }
        return 0;
    }

    public int ValidatePass(String pass, String confirm) {
        if (pass.isEmpty() || confirm.isEmpty()) {
            return -1;
        }
        if (!pass.equals(confirm)) {
            return -2;
        }
        return 0;
    }

    public SimpleDateFormat FormattedDateBuilder(Timestamp timestamp){
        Date d = timestamp.toDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        SimpleDateFormat formatted = new SimpleDateFormat("dd-MM-yyyy");
        formatted.setTimeZone(cal.getTimeZone());
        return formatted;
    }



}
