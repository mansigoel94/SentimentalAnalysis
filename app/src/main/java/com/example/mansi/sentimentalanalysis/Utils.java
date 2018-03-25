package com.example.mansi.sentimentalanalysis;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static String convertTimeStampToFormattedDate(long time, String format) {
        return new SimpleDateFormat(format).format(new Date(time));
    }
}
