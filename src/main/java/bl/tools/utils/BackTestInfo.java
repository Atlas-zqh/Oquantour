package bl.tools.utils;

import java.util.Date;

/**
 * Created by st on 2017/3/29.
 */
public class BackTestInfo extends ValueInfo {

    public BackTestInfo(Date theDate, double theValue) {
        date = theDate;
        value = theValue;
    }
}
