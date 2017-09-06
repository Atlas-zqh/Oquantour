package po;

import java.util.Date;

/**
 * Created by Pxr on 2017/3/31.
 */
public class PlatePO {
    private Date date;

    private Double returnRate;

    public Date getDate() {
        return date;
    }

    public Double getReturnRate() {
        return returnRate;
    }

    public PlatePO(Date date, Double returnRate) {
        this.date = date;
        this.returnRate = returnRate;
    }
}
