package com.example.sus.Activities.Core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static String getTimestamp() {
        DateFormat timestamp_format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
        Date currentDate = new Date();
        return timestamp_format.format(currentDate);
    }
}
