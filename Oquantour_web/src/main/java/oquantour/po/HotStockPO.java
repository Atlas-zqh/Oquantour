package oquantour.po;

/**
 * Created by island on 2017/6/9.
 */
public class HotStockPO {
    private String StockID;

    private StockRealTimePO stockRealTimePO;

    private int changeRank;

    private int volumeRank;

    private int amountRank;

    private int comprehensiveRank;

    private double volumeRate;

    private double amountRate;

    private double changeRate;

    private double comprehensiveRate;

    public String getStockID() {
        return StockID;
    }

    public void setStockID(String stockID) {
        StockID = stockID;
    }

    public StockRealTimePO getStockRealTimePO() {
        return stockRealTimePO;
    }

    public void setStockRealTimePO(StockRealTimePO stockRealTimePO) {
        this.stockRealTimePO = stockRealTimePO;
    }

    public int getChangeRank() {
        return changeRank;
    }

    public void setChangeRank(int changeRank) {
        this.changeRank = changeRank;
    }

    public int getVolumeRank() {
        return volumeRank;
    }

    public void setVolumeRank(int volumeRank) {
        this.volumeRank = volumeRank;
    }

    public int getAmountRank() {
        return amountRank;
    }

    public void setAmountRank(int amountRank) {
        this.amountRank = amountRank;
    }

    public int getComprehensiveRank() {
        return comprehensiveRank;
    }

    public void setComprehensiveRank(int comprehensiveRank) {
        this.comprehensiveRank = comprehensiveRank;
    }

    public void setVolumeRate(double volumeRate) {
        this.volumeRate = volumeRate;
    }

    public void setAmountRate(double amountRate) {
        this.amountRate = amountRate;
    }

    public void setChangeRate(double changeRate) {
        this.changeRate = changeRate;
    }

    public double getComprehensiveRate() {
        return comprehensiveRate;
    }

    public void calComprehensiveRate(){
        this.comprehensiveRate = volumeRate + amountRate + changeRate;
    }
}
