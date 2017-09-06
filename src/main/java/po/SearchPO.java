package po;

import vo.SearchVO;

import java.util.Date;

/**
 * PO of searching parameters
 * <p>
 * some attributes may be empty("")
 * <p>
 * Created by keenan on 04/03/2017.
 */
public class SearchPO {

    // might be code or name
    private String stockInfo;

    private Date startDate;

    private Date endDate;

    private String plateName;

    public SearchPO(String stockInfo, Date startDate, Date endDate) {
        this.stockInfo = stockInfo;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public SearchPO(Date startDate, Date endDate, String plateName) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.plateName = plateName;
    }

    public String getPlateName() {
        return plateName;
    }

    public void setPlateName(String plateName) {
        this.plateName = plateName;
    }

    public String getStockInfo() {
        return stockInfo;
    }

    public void setStockInfo(String stockInfo) {
        this.stockInfo = stockInfo;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
