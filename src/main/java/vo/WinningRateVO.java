package vo;

/**
 * Created by keenan on 07/04/2017.
 */
public class WinningRateVO {
    // 形成期 或 持有期
    private int days;
    // 超额收益率
    private double extraReturnRate;
    // 策略胜率
    private double winningRate;

    public int getDays() {
        return days;
    }

    public double getExtraReturnRate() {
        return extraReturnRate;
    }

    public double getWinningRate() {
        return winningRate;
    }

    public WinningRateVO(int days, double extraReturnRate, double winningRate) {

        this.days = days;
        this.extraReturnRate = extraReturnRate;
        this.winningRate = winningRate;
    }
}
