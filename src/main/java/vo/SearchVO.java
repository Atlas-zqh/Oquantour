package vo;

import java.util.Date;

/**
 * 搜索所需要的参数
 * <p>
 * Created by Pxr on 2017/3/3.
 */
public class SearchVO {
    //所需搜索股票1的名字或号码
    public String stock1;
    //所需搜索股票2的名字或号码,若不需要，则为""
    public String stock2;
    //开始日期
    public Date startDate;
    //结束日期
    public Date endDate;
    //板块名称
    public String plateName;

    public SearchVO(String stock1, String stock2, Date startDate, Date endDate) {
        this.stock1 = stock1;
        this.stock2 = stock2;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public SearchVO(Date searchDate) {
        this.startDate = searchDate;
    }

    public SearchVO(Date searchDate, String plateName) {
        this.startDate = searchDate;
        this.plateName = plateName;
    }
}
