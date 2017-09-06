package oquantour.po;

import javax.persistence.*;

/**
 * Created by island on 2017/6/4.
 */
@Entity
@Table(name = "stockbasicinfo", schema = "oquantour", catalog = "")
@IdClass(StockBasicInfoPOPK.class)
public class StockBasicInfoPO {
    /**
     * 股票号
     */
    private String stockId;
    /**
     * 季度
     */
    private String quarterOfYear;

    /*************************************************
     *                 业绩报告（主表）                *
     *************************************************/

    /**
     * 每股收益
     */
    private Double esp;
    /**
     * 每股净资产
     */
    private Double bvps;
    /**
     * 净资产收益率(%)
     */
    private Double roe;
    /**
     * 净利润(万元)
     */
    private Double netProfits;
    /**
     * 净利润同比(%)
     */
    private Double profitsYoy;

    /*************************************************
     *                    盈利能力                    *
     *************************************************/

    /**
     * 净利率(%)
     */
    private Double netProfitRatio;
    /**
     * 毛利率(%)
     */
    private Double grossProfitRate;
    /**
     * 每股收益
     */
    private Double eps;
    /**
     * 营业收入(百万元)
     */
    private Double businessIncome;
    /**
     * 每股主营业务收入(元)
     */
    private Double bips;

    /*************************************************
     *                    营运能力                    *
     *************************************************/

    /**
     * 应收账款周转率(次)
     */
    private Double arturnover;
    /**
     * 存货周转率(次)
     */
    private Double inventoryTurnover;
    /**
     * 存货周转天数(天)
     */
    private Double inventoryDays;
    /**
     * 流动资产周转率(次)
     */
    private Double currentassetTurnover;
    /**
     * 流动资产周转天数(天)
     */
    private Double currentassetDays;
    /**
     * 应收账款周转天数(天)
     */
    private Double arturndays;

    /*************************************************
     *                    成长能力                    *
     *************************************************/

    /**
     * 主营业务收入增长率(%)
     */
    private Double mbrg;
    /**
     * 净利润增长率(%)
     */
    private Double nprg;
    /**
     * 净资产增长率
     */
    private Double nav;
    /**
     * 总资产增长率
     */
    private Double targ;
    /**
     * 每股收益增长率
     */
    private Double epsg;
    /**
     * 股东权益增长率
     */
    private Double seg;

    /*************************************************
     *                    偿债能力                    *
     *************************************************/

    /**
     * 流动比率
     */
    private Double currentRatio;
    /**
     * 速动比率
     */
    private Double quickRatio;
    /**
     * 利息支付倍数
     */
    private Double icRatio;
    /**
     * 股东权益比率
     */
    private Double sheqRatio;
    /**
     * 股东权益增长率
     */
    private Double adRatio;
    /**
     * 现金比率
     */
    private Double cashRatio;

    /*************************************************
     *                    现金流量                    *
     *************************************************/

    /**
     * 经营现金净流量对销售收入比率
     */
    private Double cfSales;
    /**
     * 资产的经营现金流量回报率
     */
    private Double rateOfReturn;
    /**
     * 经营现金净流量与净利润的比率
     */
    private Double cfNm;
    /**
     * 经营现金净流量对负债比率
     */
    private Double cfLiabilities;
    /**
     * 现金流量比率
     */
    private Double cashflowRatio;

    @Id
    @Column(name = "StockID")
    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    @Id
    @Column(name = "QuarterOfYear")
    public String getQuarterOfYear() {
        return quarterOfYear;
    }

    public void setQuarterOfYear(String quarterOfYear) {
        this.quarterOfYear = quarterOfYear;
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
    @Column(name = "Roe")
    public Double getRoe() {
        return roe;
    }

    public void setRoe(Double roe) {
        this.roe = roe;
    }

    @Basic
    @Column(name = "Net_profits")
    public Double getNetProfits() {
        return netProfits;
    }

    public void setNetProfits(Double netProfits) {
        this.netProfits = netProfits;
    }

    @Basic
    @Column(name = "Profits_yoy")
    public Double getProfitsYoy() {
        return profitsYoy;
    }

    public void setProfitsYoy(Double profitsYoy) {
        this.profitsYoy = profitsYoy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StockBasicInfoPO that = (StockBasicInfoPO) o;

        if (stockId != null ? !stockId.equals(that.stockId) : that.stockId != null) return false;
        if (quarterOfYear != null ? !quarterOfYear.equals(that.quarterOfYear) : that.quarterOfYear != null)
            return false;
        if (esp != null ? !esp.equals(that.esp) : that.esp != null) return false;
        if (bvps != null ? !bvps.equals(that.bvps) : that.bvps != null) return false;
        if (roe != null ? !roe.equals(that.roe) : that.roe != null) return false;
        if (netProfits != null ? !netProfits.equals(that.netProfits) : that.netProfits != null) return false;
        if (profitsYoy != null ? !profitsYoy.equals(that.profitsYoy) : that.profitsYoy != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = stockId != null ? stockId.hashCode() : 0;
        result = 31 * result + (quarterOfYear != null ? quarterOfYear.hashCode() : 0);
        result = 31 * result + (esp != null ? esp.hashCode() : 0);
        result = 31 * result + (bvps != null ? bvps.hashCode() : 0);
        result = 31 * result + (roe != null ? roe.hashCode() : 0);
        result = 31 * result + (netProfits != null ? netProfits.hashCode() : 0);
        result = 31 * result + (profitsYoy != null ? profitsYoy.hashCode() : 0);
        return result;
    }

    @Basic
    @Column(name = "Net_profit_ratio")
    public Double getNetProfitRatio() {
        return netProfitRatio;
    }

    public void setNetProfitRatio(Double netProfitRatio) {
        this.netProfitRatio = netProfitRatio;
    }

    @Basic
    @Column(name = "Gross_profit_rate")
    public Double getGrossProfitRate() {
        return grossProfitRate;
    }

    public void setGrossProfitRate(Double grossProfitRate) {
        this.grossProfitRate = grossProfitRate;
    }

    @Basic
    @Column(name = "Eps")
    public Double getEps() {
        return eps;
    }

    public void setEps(Double eps) {
        this.eps = eps;
    }

    @Basic
    @Column(name = "Business_income")
    public Double getBusinessIncome() {
        return businessIncome;
    }

    public void setBusinessIncome(Double businessIncome) {
        this.businessIncome = businessIncome;
    }

    @Basic
    @Column(name = "Bips")
    public Double getBips() {
        return bips;
    }

    public void setBips(Double bips) {
        this.bips = bips;
    }

    @Basic
    @Column(name = "Arturnover")
    public Double getArturnover() {
        return arturnover;
    }

    public void setArturnover(Double arturnover) {
        this.arturnover = arturnover;
    }

    @Basic
    @Column(name = "Inventory_turnover")
    public Double getInventoryTurnover() {
        return inventoryTurnover;
    }

    public void setInventoryTurnover(Double inventoryTurnover) {
        this.inventoryTurnover = inventoryTurnover;
    }

    @Basic
    @Column(name = "Inventory_days")
    public Double getInventoryDays() {
        return inventoryDays;
    }

    public void setInventoryDays(Double inventoryDays) {
        this.inventoryDays = inventoryDays;
    }

    @Basic
    @Column(name = "Currentasset_turnover")
    public Double getCurrentassetTurnover() {
        return currentassetTurnover;
    }

    public void setCurrentassetTurnover(Double currentassetTurnover) {
        this.currentassetTurnover = currentassetTurnover;
    }

    @Basic
    @Column(name = "Currentasset_days")
    public Double getCurrentassetDays() {
        return currentassetDays;
    }

    public void setCurrentassetDays(Double currentassetDays) {
        this.currentassetDays = currentassetDays;
    }

    @Basic
    @Column(name = "Arturndays")
    public Double getArturndays() {
        return arturndays;
    }

    public void setArturndays(Double arturndays) {
        this.arturndays = arturndays;
    }

    @Basic
    @Column(name = "Mbrg")
    public Double getMbrg() {
        return mbrg;
    }

    public void setMbrg(Double mbrg) {
        this.mbrg = mbrg;
    }

    @Basic
    @Column(name = "Nprg")
    public Double getNprg() {
        return nprg;
    }

    public void setNprg(Double nprg) {
        this.nprg = nprg;
    }

    @Basic
    @Column(name = "Nav")
    public Double getNav() {
        return nav;
    }

    public void setNav(Double nav) {
        this.nav = nav;
    }

    @Basic
    @Column(name = "Targ")
    public Double getTarg() {
        return targ;
    }

    public void setTarg(Double targ) {
        this.targ = targ;
    }

    @Basic
    @Column(name = "Epsg")
    public Double getEpsg() {
        return epsg;
    }

    public void setEpsg(Double epsg) {
        this.epsg = epsg;
    }

    @Basic
    @Column(name = "Seg")
    public Double getSeg() {
        return seg;
    }

    public void setSeg(Double seg) {
        this.seg = seg;
    }

    @Basic
    @Column(name = "CurrentRatio")
    public Double getCurrentRatio() {
        return currentRatio;
    }

    public void setCurrentRatio(Double currentRatio) {
        this.currentRatio = currentRatio;
    }

    @Basic
    @Column(name = "QuickRatio")
    public Double getQuickRatio() {
        return quickRatio;
    }

    public void setQuickRatio(Double quickRatio) {
        this.quickRatio = quickRatio;
    }

    @Basic
    @Column(name = "IcRatio")
    public Double getIcRatio() {
        return icRatio;
    }

    public void setIcRatio(Double icRatio) {
        this.icRatio = icRatio;
    }

    @Basic
    @Column(name = "SheqRatio")
    public Double getSheqRatio() {
        return sheqRatio;
    }

    public void setSheqRatio(Double sheqRatio) {
        this.sheqRatio = sheqRatio;
    }

    @Basic
    @Column(name = "AdRatio")
    public Double getAdRatio() {
        return adRatio;
    }

    public void setAdRatio(Double adRatio) {
        this.adRatio = adRatio;
    }

    @Basic
    @Column(name = "CashRatio")
    public Double getCashRatio() {
        return cashRatio;
    }

    public void setCashRatio(Double cashRatio) {
        this.cashRatio = cashRatio;
    }

    @Basic
    @Column(name = "Cf_sales")
    public Double getCfSales() {
        return cfSales;
    }

    public void setCfSales(Double cfSales) {
        this.cfSales = cfSales;
    }

    @Basic
    @Column(name = "RateOfReturn")
    public Double getRateOfReturn() {
        return rateOfReturn;
    }

    public void setRateOfReturn(Double rateOfReturn) {
        this.rateOfReturn = rateOfReturn;
    }

    @Basic
    @Column(name = "Cf_nm")
    public Double getCfNm() {
        return cfNm;
    }

    public void setCfNm(Double cfNm) {
        this.cfNm = cfNm;
    }

    @Basic
    @Column(name = "Cf_liabilities")
    public Double getCfLiabilities() {
        return cfLiabilities;
    }

    public void setCfLiabilities(Double cfLiabilities) {
        this.cfLiabilities = cfLiabilities;
    }

    @Basic
    @Column(name = "CashflowRatio")
    public Double getCashflowRatio() {
        return cashflowRatio;
    }

    public void setCashflowRatio(Double cashflowRatio) {
        this.cashflowRatio = cashflowRatio;
    }
}
