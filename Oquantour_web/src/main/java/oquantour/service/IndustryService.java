package oquantour.service;

import oquantour.po.StockRealTimePO;
import oquantour.po.util.ChartInfo;
import oquantour.po.util.RelationGraph;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * 行业相关
 * <p>
 * Created by keenan on 02/06/2017.
 */
public interface IndustryService {
    /**
     * 得到所有行业名称
     *
     * @return
     */
    List<String> getAllIndustriesName();

    /**
     * 得到行业板块间的最小生成树
     *
     * @return 最小生成树
     */
    RelationGraph getIndustryTree();

    /**
     * 根据行业名，日期区间获得行业内所有股票数据
     *
     * @param industryName 行业名
     * @return 行业内股票数据 key: 股票号，value: 股票数据
     */
    Map<String, StockRealTimePO> getIndustryStocks(String industryName);

    /**
     * 获得一段时间的行业收益率
     * @param industries 行业名
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 行业收益率
     */
    Map<String, List<ChartInfo>> getAllIndustryReturnRate(List<String> industries, Date startDate, Date endDate);
}
