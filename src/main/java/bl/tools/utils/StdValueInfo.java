package bl.tools.utils;

import po.PlatePO;

import java.util.Date;

/**
 * 用于画基准收益率折线图的类
 * <p>
 * Created by keenan on 26/03/2017.
 */
public class StdValueInfo extends ValueInfo {

    public StdValueInfo(Date theDate, double theValue) {
        date = theDate;
        value = theValue;
    }

    public StdValueInfo(PlatePO platePO) {
        date = platePO.getDate();
        value = platePO.getReturnRate() / 100;
    }
}
