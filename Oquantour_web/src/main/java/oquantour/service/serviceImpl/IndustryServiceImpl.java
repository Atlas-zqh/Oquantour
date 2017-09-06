package oquantour.service.serviceImpl;

import oquantour.data.dao.IndustryDao;
import oquantour.po.StockRealTimePO;
import oquantour.po.util.ChartInfo;
import oquantour.po.util.Edge;
import oquantour.po.util.RelationGraph;
import oquantour.service.IndustryService;
import oquantour.service.servicehelper.analysis.industry.IndustryRelativity;
import oquantour.util.tools.CalendarUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.*;

/**
 * Created by keenan on 02/06/2017.
 */
@Service
@Transactional
public class IndustryServiceImpl implements IndustryService {

    @Autowired
    private IndustryDao industryDao;

    /**
     * 得到所有行业名称
     *
     * @return
     */
    @Override
    public List<String> getAllIndustriesName() {
        return industryDao.getAllIndustries();
    }

    /**
     * 根据行业名，日期区间获得行业内所有股票数据
     *
     * @param industryName 行业名
     * @return 行业内股票数据 key: 股票号，value: 股票数据
     */
    @Override
    public Map<String, StockRealTimePO> getIndustryStocks(String industryName) {
        return industryDao.getStocksInIndustry(industryName);
    }

    /**
     * 得到行业板块间的最小生成树
     *
     * @return 最小生成树
     */
    @Override
    @SuppressWarnings("Duplicates")
    public RelationGraph getIndustryTree() {
        List<String> list = industryDao.getAllIndustries();

        String[] industries = list.toArray(new String[1]);
        Date today = CalendarUtil.getToday();
        Date oneYear = CalendarUtil.getDateOneYearBefore(today);

        List<List<Double>> rr = new ArrayList<>();
        Map<String, List<ChartInfo>> chartInfos = new HashMap<>();
        for (String s : industries) {
            List<ChartInfo> chartInfo = industryDao.getIndustryReturnRate(s, oneYear, today);
            chartInfos.put(s, chartInfo);
            List<Double> doubles = new ArrayList<>();
            chartInfo.stream().forEachOrdered(chartInfo1 -> doubles.add(chartInfo1.getyAxis()));
            rr.add(doubles);
        }

        PriorityQueue<Edge> edges = new IndustryRelativity(list, rr).getRelativity();
        return new RelationGraph(edges, list, chartInfos);
    }

    /**
     * 获得一段时间的行业收益率
     *
     * @param industries 行业名
     * @param startDate  开始日期
     * @param endDate    结束日期
     * @return 行业收益率
     */
    @Override
    public Map<String, List<ChartInfo>> getAllIndustryReturnRate(List<String> industries, Date startDate, Date endDate) {
        Map<String, List<ChartInfo>> info = new HashMap<>();
        for (String name : industries) {
            List<ChartInfo> chartInfos = industryDao.getIndustryReturnRate(name, startDate, endDate);
            info.put(name, chartInfos);
        }
        return info;
    }
}
