package oquantour.po;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 沪深股票列表
 * Created by island on 5/23/17.
 */
@Entity
public class StockInfoPO {
    /**
     * 代码
     */
    private String stockId;
    /**
     * 名称
     */
    private String stockName;
    /**
     * 板块
     */
    private String plate;
    /**
     * 搜索次数
     */
    private Integer searchCount;
    /**
     * 行业
     */
    private String industry;
    /**
     * 地区
     */
    private String area;
    /**
     * 市盈率
     */
    private Double pe;
    /**
     * 流通股本(亿)
     */
    private Double outstanding;
    /**
     * 总股本(亿)
     */
    private Double totals;
    /**
     * 总资产(万)
     */
    private Double totalAssets;
    /**
     * 流动资产
     */
    private Double liquidAssets;
    /**
     * 固定资产
     */
    private Double fixedAssets;
    /**
     * 公积金
     */
    private Double reserved;
    /**
     * 每股公积金
     */
    private Double reservedPerShare;
    /**
     * 每股收益
     */
    private Double esp;
    /**
     * 每股净资
     */
    private Double bvps;
    /**
     * 市净率
     */
    private Double pb;
    /**
     * 上市日期
     */
    private String timeToMarket;
    /**
     * 未分利润
     */
    private Double undp;
    /**
     * 每股未分配
     */
    private Double perundp;
    /**
     * 收入同比(%)
     */
    private Double rev;
    /**
     * 利润同比(%)
     */
    private Double profit;
    /**
     * 毛利率(%)
     */
    private Double gpr;
    /**
     * 净利润率(%)
     */
    private Double npr;
    /**
     * 股东人数
     */
    private Integer holders;

    @Id
    @Column(name = "StockID")
    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    @Basic
    @Column(name = "StockName")
    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    @Basic
    @Column(name = "Plate")
    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    @Basic
    @Column(name = "SearchCount")
    public Integer getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(Integer searchCount) {
        this.searchCount = searchCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StockInfoPO that = (StockInfoPO) o;

        if (stockId != null ? !stockId.equals(that.stockId) : that.stockId != null) return false;
        if (stockName != null ? !stockName.equals(that.stockName) : that.stockName != null) return false;
        if (plate != null ? !plate.equals(that.plate) : that.plate != null) return false;
        if (searchCount != null ? !searchCount.equals(that.searchCount) : that.searchCount != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = stockId != null ? stockId.hashCode() : 0;
        result = 31 * result + (stockName != null ? stockName.hashCode() : 0);
        result = 31 * result + (plate != null ? plate.hashCode() : 0);
        result = 31 * result + (searchCount != null ? searchCount.hashCode() : 0);
        return result;
    }

    @Basic
    @Column(name = "Industry")
    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    @Basic
    @Column(name = "Area")
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @Basic
    @Column(name = "Pe")
    public Double getPe() {
        return pe;
    }

    public void setPe(Double pe) {
        this.pe = pe;
    }

    @Basic
    @Column(name = "Outstanding")
    public Double getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(Double outstanding) {
        this.outstanding = outstanding;
    }

    @Basic
    @Column(name = "Totals")
    public Double getTotals() {
        return totals;
    }

    public void setTotals(Double totals) {
        this.totals = totals;
    }

    @Basic
    @Column(name = "TotalAssets")
    public Double getTotalAssets() {
        return totalAssets;
    }

    public void setTotalAssets(Double totalAssets) {
        this.totalAssets = totalAssets;
    }

    @Basic
    @Column(name = "LiquidAssets")
    public Double getLiquidAssets() {
        return liquidAssets;
    }

    public void setLiquidAssets(Double liquidAssets) {
        this.liquidAssets = liquidAssets;
    }

    @Basic
    @Column(name = "FixedAssets")
    public Double getFixedAssets() {
        return fixedAssets;
    }

    public void setFixedAssets(Double fixedAssets) {
        this.fixedAssets = fixedAssets;
    }

    @Basic
    @Column(name = "Reserved")
    public Double getReserved() {
        return reserved;
    }

    public void setReserved(Double reserved) {
        this.reserved = reserved;
    }

    @Basic
    @Column(name = "ReservedPerShare")
    public Double getReservedPerShare() {
        return reservedPerShare;
    }

    public void setReservedPerShare(Double reservedPerShare) {
        this.reservedPerShare = reservedPerShare;
    }

    @Basic
    @Column(name = "Esp")
    public Double getEsp() {
        return esp;
    }

    public void setEsp(Double esp) {
        this.esp = esp;
    }

    @Basic
    @Column(name = "Bvps")
    public Double getBvps() {
        return bvps;
    }

    public void setBvps(Double bvps) {
        this.bvps = bvps;
    }

    @Basic
    @Column(name = "Pb")
    public Double getPb() {
        return pb;
    }

    public void setPb(Double pb) {
        this.pb = pb;
    }

    @Basic
    @Column(name = "TimeToMarket")
    public String getTimeToMarket() {
        return timeToMarket;
    }

    public void setTimeToMarket(String timeToMarket) {
        this.timeToMarket = timeToMarket;
    }

    @Basic
    @Column(name = "Undp")
    public Double getUndp() {
        return undp;
    }

    public void setUndp(Double undp) {
        this.undp = undp;
    }

    @Basic
    @Column(name = "Perundp")
    public Double getPerundp() {
        return perundp;
    }

    public void setPerundp(Double perundp) {
        this.perundp = perundp;
    }

    @Basic
    @Column(name = "Rev")
    public Double getRev() {
        return rev;
    }

    public void setRev(Double rev) {
        this.rev = rev;
    }

    @Basic
    @Column(name = "Profit")
    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    @Basic
    @Column(name = "Gpr")
    public Double getGpr() {
        return gpr;
    }

    public void setGpr(Double gpr) {
        this.gpr = gpr;
    }

    @Basic
    @Column(name = "Npr")
    public Double getNpr() {
        return npr;
    }

    public void setNpr(Double npr) {
        this.npr = npr;
    }

    @Basic
    @Column(name = "Holders")
    public Integer getHolders() {
        return holders;
    }

    public void setHolders(Integer holders) {
        this.holders = holders;
    }
}
