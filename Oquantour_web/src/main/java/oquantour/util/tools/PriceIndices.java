package oquantour.util.tools;


/**
 * Created by keenan on 10/06/2017.
 */
public enum PriceIndices {
    OpenPrice,// 开盘价
    ClosePrice,// 收盘价
    AdjClose,// 复权收盘价
    HighPrice,// 最高价
    LowPrice, // 最低价
    Volume, // 成交量（一般都是六位数～八位数）
    Chg, // 涨跌幅
    Amount,// 成交额（一般都是七～八位数）
    kValue,// 随机指标KDJ线k值
    dValue,// 随机指标KDJ线d值
    jValue,// 随机指标KDJ线j值
    Ema12,// 12日EMA（指数平均数指标）
    Ema26,// 26日EMA（指数平均数指标）
    Up,// 布林上线
    Down,// 布林下线
    Dif,// 指数平滑移动平均线MACD线离差值
    Dea,// 指数平滑移动平均线MACD线讯号线
    Rsi; // 相对强弱指数RSI

    public static PriceIndices getIndex(String name) {
        switch (name) {
            case "调仓日开盘价":
                return OpenPrice;
            case "调仓日收盘价":
                return ClosePrice;
            case "调仓日复权收盘价":
                return AdjClose;
            case "调仓日最高价":
                return HighPrice;
            case "调仓日最低价":
                return LowPrice;
            case "调仓日成交量":
                return Volume;
            case "调仓日涨跌幅":
                return Chg;
            case "调仓日成交额":
                return Amount;
            case "KDJ线K值":
                return kValue;
            case "KDJ线D值":
                return dValue;
            case "KDJ线J值":
                return jValue;
            case "12日EMA":
                return Ema12;
            case "26日EMA":
                return Ema26;
            case "布林上线":
                return Up;
            case "布林下线":
                return Down;
            case "MACD离差值":
                return Dif;
            case "MACD线讯号":
                return Dea;
            case "RSI":
                return Rsi;
            default:
                return null;
        }
    }
}
