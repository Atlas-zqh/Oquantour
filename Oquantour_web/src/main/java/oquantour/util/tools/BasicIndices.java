package oquantour.util.tools;

/**
 * 基本面指标
 * <p>
 * Created by keenan on 31/05/2017.
 */
public enum BasicIndices {
    pe,//市盈率
    eps_yoy,//每股收益同比(%)
    pb,//市净率
    rev,//收入同比(%)
    profit,//利润同比(%)
    npr,//净利润率(%)
    roe,//净资产收益率(%)
    profits_yoy,//净利润同比(%)
    net_profit_ratio,//净利率(%)
    gross_profit_rate,//毛利率(%)
    arturnover,//应收账款周转率(次)
    inventory_turnover,//存货周转率(次)
    mbrg,//主营业务收入增长率(%)
    nprg,//净利润增长率(%)
    nav,//净资产增长率
    targ,//总资产增长率
    epsg,//每股收益增长率
    currentratio,//流动比率
    quickratio,//速动比率
    cashratio,//现金比率
    sheqratio,//股东权益比率
    adratio,//股东权益增长率
    cf_sales,//经营现金净流量对销售收入比率
    rateofreturn,//资产的经营现金流量回报率
    cf_nm,//经营现金净流量与净利润的比率
    cf_liabilities,//经营现金净流量对负债比率
    cashflowratio;//现金流量比率

    public static BasicIndices getIndex(String name) {
        switch (name) {
            case "市盈率":
                return pe;
            case "每股收益同比":
                return eps_yoy;
            case "市净率":
                return pb;
            case "收入同比":
                return rev;
            case "利润同比":
                return profit;
            case "净利润率":
                return npr;
            case "净资产收益率":
                return roe;
            case "净利润同比":
                return profits_yoy;
            case "净利率":
                return net_profit_ratio;
            case "毛利率":
                return gross_profit_rate;
            case "应收账款周转率":
                return arturnover;
            case "存货周转率":
                return inventory_turnover;
            case "主营业务收入增长率":
                return mbrg;
            case "净利润增长率":
                return nprg;
            case "净资产增长率":
                return nav;
            case "总资产增长率":
                return targ;
            case "每股收益增长率":
                return epsg;
            case "流动比率":
                return currentratio;
            case "速动比率":
                return quickratio;
            case "现金比率":
                return cashratio;
            case "股东权益比率":
                return sheqratio;
            case "股东权益增长率":
                return adratio;
            case "经营现金净流量对销售收入比率":
                return cf_sales;
            case "资产的经营现金流量回报率":
                return rateofreturn;
            case "经营现金净流量与净利润的比率":
                return cf_nm;
            case "经营现金净流量对负债比率":
                return cf_liabilities;
            case "现金流量比率":
                return cashflowratio;
            default:
                return null;
        }
    }


}
