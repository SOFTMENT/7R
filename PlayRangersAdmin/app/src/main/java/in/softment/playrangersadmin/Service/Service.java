package in.softment.playrangersadmin.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Service {


    public static  String convertDate(Date date) {
        date.setTime(date.getTime()+19800000);
        String pattern = "dd-MMM-yyyy, hh:mm aa";
        DateFormat df = new SimpleDateFormat(pattern, Locale.getDefault());
        return  df.format(date);
    }
}
