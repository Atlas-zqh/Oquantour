package oquantour.po.util;

import oquantour.po.HotStockPO;
import oquantour.po.StockPO;
import oquantour.po.StockRealTimePO;

import java.text.DecimalFormat;
import java.sql.Date;
import java.util.List;

/**
 * 市场温度
 * <p>
 * Created by keenan on 07/05/2017.
 */
public class MarketTemperature {
    // 总交易量
    private Long totalVolume;
    // 涨停股票数
    private Long limitUpStock;
    // 跌停股票数
    private Long limitDownStock;
    // 涨幅超过5%的股票数
    private Long up5perStock;
    // 跌幅超过5%的股票数
    private Long down5perStock;
    // 当日大涨股
    private Long openUp5perStock;
    // 当日大跌股
    private Long openDown5perStock;
    // 当日总股票数
    private Long totalNum;
    // 涨幅榜
    private List<StockRealTimePO> upList;
    // 跌幅榜
    private List<StockRealTimePO> downList;
    //深市涨幅榜
    private List<StockRealTimePO> szUpList;
    // 沪市涨幅榜
    private List<StockRealTimePO> ssUpList;
    // 深市跌幅榜
    private List<StockRealTimePO> szDownList;
    // 沪市跌幅榜
    private List<StockRealTimePO> ssDownList;
    // 深市成交量榜
    private List<StockRealTimePO> szVolumeList;
    // 沪市成交量榜
    private List<StockRealTimePO> ssVolumeList;
    // 深市成交额榜
    private List<StockRealTimePO> szAmountList;
    // 沪市成交e榜
    private List<StockRealTimePO> ssAmountList;
    //热股榜
    private List<HotStockPO> hotStockPOS;

    // 当前日期
    private Date date;
    // 当日市场温度
    private double temperature;

    public MarketTemperature() {
    }


    public long getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(long totalVolume) {
        this.totalVolume = totalVolume;
    }

    public Long getLimitUpStock() {
        return limitUpStock;
    }

    public void setLimitUpStock(Long limitUpStock) {
        this.limitUpStock = limitUpStock;
    }

    public Long getLimitDownStock() {
        return limitDownStock;
    }

    public void setLimitDownStock(Long limitDownStock) {
        this.limitDownStock = limitDownStock;
    }

    public Long getUp5perStock() {
        return up5perStock;
    }

    public void setUp5perStock(Long up5perStock) {
        this.up5perStock = up5perStock;
    }

    public Long getDown5perStock() {
        return down5perStock;
    }

    public void setDown5perStock(Long down5perStock) {
        this.down5perStock = down5perStock;
    }

    public Long getOpenUp5perStock() {
        return openUp5perStock;
    }

    public void setOpenUp5perStock(Long openUp5perStock) {
        this.openUp5perStock = openUp5perStock;
    }

    public Long getOpenDown5perStock() {
        return openDown5perStock;
    }

    public void setOpenDown5perStock(Long openDown5perStock) {
        this.openDown5perStock = openDown5perStock;
    }

    public Long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Long totalNum) {
        this.totalNum = totalNum;
    }

    public List<StockRealTimePO> getUpList() {
        return upList;
    }

    public void setUpList(List<StockRealTimePO> upList) {
        this.upList = upList;
    }

    public List<StockRealTimePO> getDownList() {
        return downList;
    }

    public void setDownList(List<StockRealTimePO> downList) {
        this.downList = downList;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public List<StockRealTimePO> getSzUpList() {
        return szUpList;
    }

    public void setSzUpList(List<StockRealTimePO> szUpList) {
        this.szUpList = szUpList;
    }

    public List<StockRealTimePO> getSsUpList() {
        return ssUpList;
    }

    public void setSsUpList(List<StockRealTimePO> ssUpList) {
        this.ssUpList = ssUpList;
    }

    public List<StockRealTimePO> getSzDownList() {
        return szDownList;
    }

    public void setSzDownList(List<StockRealTimePO> szDownList) {
        this.szDownList = szDownList;
    }

    public List<StockRealTimePO> getSsDownList() {
        return ssDownList;
    }

    public void setSsDownList(List<StockRealTimePO> ssDownList) {
        this.ssDownList = ssDownList;
    }

    public List<StockRealTimePO> getSzVolumeList() {
        return szVolumeList;
    }

    public void setSzVolumeList(List<StockRealTimePO> szVolumeList) {
        this.szVolumeList = szVolumeList;
    }

    public List<StockRealTimePO> getSsVolumeList() {
        return ssVolumeList;
    }

    public void setSsVolumeList(List<StockRealTimePO> ssVolumeList) {
        this.ssVolumeList = ssVolumeList;
    }

    public List<StockRealTimePO> getSzAmountList() {
        return szAmountList;
    }

    public void setSzAmountList(List<StockRealTimePO> szAmountList) {
        this.szAmountList = szAmountList;
    }

    public List<StockRealTimePO> getSsAmountList() {
        return ssAmountList;
    }

    public void setSsAmountList(List<StockRealTimePO> ssAmountList) {
        this.ssAmountList = ssAmountList;
    }

    public void setTotalVolume(Long totalVolume) {
        this.totalVolume = totalVolume;
    }

    public List<HotStockPO> getHotStockPOS() {
        return hotStockPOS;
    }

    public void setHotStockPOS(List<HotStockPO> hotStockPOS) {
        this.hotStockPOS = hotStockPOS;
    }

    /**
     * 计算用数值来表示市场温度
     *
     * @return 市场温度
     */
    public double calTemperature() {
        double upPercent = upList.size() / totalNum;//当日上涨股票占总股票的百分比
        double downPercent = downList.size() / totalNum;
        double temperature = 0.0;
        temperature += (limitUpStock - limitDownStock) * 0.5;//温度加上当日涨停股票的数量，减去跌停股票的数量
        temperature += (upPercent - downPercent) * 0.3;
        temperature += (up5perStock - down5perStock) * 0.5;
        DecimalFormat decimalFormat = new DecimalFormat("#.0");

        return Double.parseDouble(decimalFormat.format(temperature));
    }
}
