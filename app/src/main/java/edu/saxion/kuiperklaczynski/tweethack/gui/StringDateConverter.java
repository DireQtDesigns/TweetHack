package edu.saxion.kuiperklaczynski.tweethack.gui;

import android.text.format.DateUtils;
import android.util.Log;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by leonk on 27/04/2016.
 */
public class StringDateConverter {

    public static final String TAG = "TweetHax_SDateConverter"; //Log Tag

    /**public static long millisFromJSONString(String twitterFormat) { //Deprecated af
        Date d = new Date();
        String[] dateTimeArray = twitterFormat.split(" ");
        String dayWeekly = dateTimeArray[0];
        String stringMonth = dateTimeArray[1];
        int dayMonthly = Integer.parseInt(dateTimeArray[2]);
        int intMonth = monthValue(stringMonth);
        int year = Integer.parseInt(dateTimeArray[5]);
        String[]  timeArray = dateTimeArray[3].split(":");
        int hours = Integer.parseInt(timeArray[0]);
        int minutes = Integer.parseInt(timeArray[1]);
        int seconds = Integer.parseInt(timeArray[0]);
        dateMillis += (year * 365)
        return dateMillis;
    }*/

    public static String agoString(long currentMillis, Date given) {
        String s = "";
        long givenMillis = given.getTime();
        long difference = currentMillis - givenMillis;
        if(Settings.DEBUG == Settings.GUI || Settings.DEBUG == Settings.ALL) Log.d(TAG, "agoString, Difference: "+difference+"     Current: "+System.currentTimeMillis()+"     given: "+given.getTime());
        if(difference > DateUtils.YEAR_IN_MILLIS) { //Yeah, see the above method...
            s =  ""+(Math.round(difference / DateUtils.YEAR_IN_MILLIS))+"y ago";
        } else if(difference > 30.5 * (DateUtils.DAY_IN_MILLIS)) { //Meh, itll do
            s =  ""+(Math.round(difference /  30.5 * (DateUtils.DAY_IN_MILLIS)))+"m ago";
        } else if(difference > DateUtils.DAY_IN_MILLIS) {
            s =  ""+(Math.round(difference / DateUtils.DAY_IN_MILLIS))+"d ago";
        } else if(difference > DateUtils.HOUR_IN_MILLIS) {
            s =  "" + (Math.round(difference / DateUtils.HOUR_IN_MILLIS)) + "h ago";
        } else if(difference > DateUtils.MINUTE_IN_MILLIS) {
            s =  "" + (Math.round(difference / DateUtils.MINUTE_IN_MILLIS)) + "m ago";
        } else if(difference > DateUtils.SECOND_IN_MILLIS) {
            s =  "" + (Math.round(difference / DateUtils.SECOND_IN_MILLIS)) + "s ago";
        }
        if(Settings.DEBUG == Settings.GUI || Settings.DEBUG == Settings.ALL) Log.d(TAG, "agoString, Output: "+s);
        return s;
    }

    public static Date dateFromJSONString(String twitterFormat) {
        String LARGE_TWITTER_DATE_FORMAT = "EEE MMM dd HH:mm:ss Z yyyy";
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat(LARGE_TWITTER_DATE_FORMAT);
        format.setLenient(true);
        try {
            date = format.parse(twitterFormat);
            Log.d(TAG, "dateFromJSONString: Original: "+twitterFormat+" Filter: "+LARGE_TWITTER_DATE_FORMAT+" Final: "+date);
        } catch (ParseException e) {
            Log.e(TAG, "dateFromJSONString: ", e);
        }
        return date;
    }

    public static int monthValue(String monthString) {
        int value = 0;
        switch(monthString.toLowerCase()) {
            case "jan": return 1;
            case "feb": return 2;
            case "mar": return 3;
            case "apr": return 4;
            case "may": return 5;
            case "jun": return 6;
            case "jul": return 7;
            case "aug": return 8;
            case "sep": return 9;
            case "oct": return 10;
            case "nov": return 11;
            case "dec": return 12;
        }
        Log.e(TAG, "monthValue: Invalid month, given String: "+ monthString+" returns 0");
        return 0; //invalid, shouldn't happen.
    }

}
