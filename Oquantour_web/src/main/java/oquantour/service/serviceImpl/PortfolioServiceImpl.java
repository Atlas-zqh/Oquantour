package oquantour.service.serviceImpl;

import oquantour.data.dao.CombinationDao;
import oquantour.data.dao.IndustryDao;
import oquantour.data.dao.StockDao;
import oquantour.exception.WrongCombinationException;
import oquantour.po.StockCombination;
import oquantour.po.StockPO;
import oquantour.po.util.ChartInfo;
import oquantour.po.util.PortfolioInfo;
import oquantour.service.PortfolioService;
import oquantour.service.servicehelper.analysis.portfolio.PortfolioAnalysis;
import oquantour.util.tools.SortUtil;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 处理投资组合相关功能
 * <p>
 * Created by keenan on 22/05/2017.
 */
@Service
@Transactional
public class PortfolioServiceImpl implements PortfolioService {
    private final String rr = "收益率", sc = "个股收益率贡献";
    @Autowired
    CombinationDao combinationDao;
    @Autowired
    StockDao stockDao;
    @Autowired
    IndustryDao industryDao;

    PortfolioAnalysis pa = new PortfolioAnalysis();

    /**
     * 增加股票组合
     *
     * @param username      用户名
     * @param portfolioName 组合名称
     * @param stocks        股票号
     * @param positions     持股数
     * @throws WrongCombinationException 当stocks, positions, prices长度不匹配时会抛出
     */
    @Override
    public void addPortfolio(String username, String portfolioName, List<String> stocks, List<Double> positions) throws WrongCombinationException {
        combinationDao.addStockCombination(username, portfolioName, stocks, positions);
    }

    /**
     * 修改股票组合
     *
     * @param username      用户名
     * @param portfolioName 组合名称
     * @param stocks        股票号
     * @param positions     持股数
     * @throws WrongCombinationException 当stocks, positions, prices长度不匹配时会抛出
     */
    @Override
    public void modifyPortfolio(String username, String portfolioName, List<String> stocks, List<Double> positions) throws WrongCombinationException {
        combinationDao.modifyStockCombination(username, portfolioName, stocks, positions);
    }

    /**
     * 根据用户名和组合名获得股票组合信息
     *
     * @param username      用户名
     * @param portfolioName 组合名称
     * @return 用户的组合信息及分析
     */
    @Override
    public PortfolioInfo getPortfolio(String username, String portfolioName) {
        PortfolioInfo pi = new PortfolioInfo();
        List<StockCombination> stockCombinations = combinationDao.getStockCombination(username, portfolioName);
        Map<String, List<ChartInfo>> returnRatesInfo = getReturnRate(stockCombinations);
        // 收益率
        List<ChartInfo> returnRate = returnRatesInfo.get(rr);

        pi.setReturnRates(returnRate);
        // 股票贡献率: 最赚钱/输钱股票
        List<ChartInfo> contri = returnRatesInfo.get(sc);
        List<ChartInfo> contri2 = new ArrayList<>(contri);
        // 最赚钱股票
        List<ChartInfo> big = contri.stream().filter(chartInfo -> chartInfo.getyAxis() > 0).collect(Collectors.toList());
        SortUtil.sortDescending(big, chartInfo -> chartInfo.getyAxis());
        pi.setMaxProfitStocks(big);
        // 最输钱股票
        List<ChartInfo> small = contri2.stream().filter(chartInfo -> chartInfo.getyAxis() < 0).collect(Collectors.toList());
        SortUtil.sort(small, chartInfo -> chartInfo.getyAxis());
        pi.setMinProfitStocks(small);
        // 行业分布
        List<ChartInfo> indusDitrib = getIndustryDistribution(stockCombinations.get(stockCombinations.size() - 1));
        pi.setIndustryDistribution(indusDitrib);
        // 波动率
        Double vola = pa.getVolatility(returnRate);
        pi.setAnnualizedVolatility(vola);

        Map<String, Map<String, Double>> mapMap = pa.getAvgPosition(stockCombinations);
        // 平均仓位
        Map<String, Double> avg = mapMap.get(PortfolioAnalysis.avg);
        pi.setAvgPosition(avg);
        // 交易统计
        Map<String, Double> cnt = mapMap.get(PortfolioAnalysis.cnt);
        pi.setTradeCnt(cnt);
        // 交易组合名称
        String pName = stockCombinations.get(0).getCombinationName();
        pi.setPortfolioName(pName);
        // 创建时间
        Timestamp cTime = stockCombinations.get(0).getTime();
        pi.setCreateTime(cTime);
        // 总收益率
        Double trr = returnRate.get(returnRate.size() - 1).getyAxis();
        pi.setTotalReturnRate(trr);
        // 净值
        Double net = 1 + trr;
        pi.setNewNet(net);
        // 调仓详情
        pi.setPortfolio(stockCombinations);
        pi.setPositionInfo(getPositionInfo(stockCombinations));
        return pi;
    }

    /**
     * 删除投资组合
     *
     * @param userAccount   用户账户
     * @param portfolioName 用户投资组合名称
     */
    @Override
    public void deletePortfolio(String userAccount, String portfolioName) {
        combinationDao.deleteStockCombination(userAccount, portfolioName);
    }

    /**
     * 得到用户所有投资组合
     *
     * @param username 用户名
     * @return 所有投资组合
     */
    @Override
    public List<Map.Entry<String, Timestamp>> getAllPortfolios(String username) {
        Map<String, List<StockCombination>> map = combinationDao.getAllStockCombinationOfUser(username);
        Map<String, Timestamp> map1 = new HashMap<>();
        for (Map.Entry<String, List<StockCombination>> entry : map.entrySet()) {
            Timestamp t = entry.getValue().get(entry.getValue().size() - 1).getTime();
            map1.put(entry.getKey(), t);
        }

        Comparator<Map.Entry<String, Timestamp>> comparator = new Comparator<Map.Entry<String, Timestamp>>() {
            @Override
            public int compare(Map.Entry<String, Timestamp> o1, Map.Entry<String, Timestamp> o2) {
                if (o1.getValue().equals(o2.getValue())) {
                    return 0;
                } else if (o1.getValue().before(o2.getValue())) {
                    return -1;
                } else {
                    return 1;
                }
            }
        };

        List<Map.Entry<String, Timestamp>> entryList = new ArrayList<>(map1.entrySet());
        Collections.sort(entryList, comparator);

        return entryList;
    }

    /**
     * 获得某一组合最新调仓情况
     *
     * @param username      用户名
     * @param portfolioName 组合名
     * @return 某一组合最新调仓情况
     */
    @Override
    public StockCombination getLatestPortfolio(String username, String portfolioName) {
        List<StockCombination> stockCombinations = combinationDao.getStockCombination(username, portfolioName);
        return stockCombinations.get(stockCombinations.size() - 1);
    }

    /**
     * 计算股票组合收益率
     *
     * @param portfolios 股票组合
     * @return
     */
    private Map<String, List<ChartInfo>> getReturnRate(List<StockCombination> portfolios) {

        StockCombination stockCombination = portfolios.get(0);
        List<String> strings = stockCombination.getStocks();

        strings.forEach(s -> System.out.print("=====" + s));

        Map<java.sql.Date, Double> returnRates = new TreeMap<>();
        Map<String, Double> stockRank = new HashMap<>();

        for (int i = 0; i < portfolios.size(); i++) {
            StockCombination portfolio = portfolios.get(i);
            Date before = portfolio.getTime();

            Date after;
            if (i == portfolios.size() - 1) {
                after = new Date(System.currentTimeMillis());
            } else {
                after = portfolios.get(i + 1).getTime();
            }

            // 如果同一天有多次调仓，则以最后一次调仓为准
            if (DateUtils.isSameDay(before, after)) {
                continue;
            }

            List<Double> positions = portfolio.getPositions();
            java.sql.Date before_sql = new java.sql.Date(before.getTime());
            java.sql.Date after_sql = new java.sql.Date(after.getTime());
            for (int j = 0; j < portfolio.getStocks().size(); j++) {
                String stockID = portfolio.getStocks().get(j);
                List<StockPO> stockInfo = stockDao.searchStock(stockID, before_sql, after_sql);

                for (StockPO stockPO : stockInfo) {
                    if (returnRates.get(stockPO.getDateValue()) == null) {
                        returnRates.put(stockPO.getDateValue(), stockPO.getChg() * positions.get(j));
                    } else {
                        double tmp = returnRates.get(stockPO.getDateValue());
                        tmp += stockPO.getChg() * positions.get(j);
                        returnRates.put(stockPO.getDateValue(), tmp);
                    }

                    // 计算每个股票的累计收益贡献
                    if (stockRank.get(stockID) == null) {
                        stockRank.put(stockID, stockPO.getChg() * positions.get(j));
                    } else {
                        double tmp = stockRank.get(stockID);
                        tmp += stockPO.getChg() * positions.get(j);
                        stockRank.put(stockID, tmp);
                    }
                }
            }
        }

        // 将map转为list
        List<ChartInfo> chartInfos = new ArrayList<>();
        for (Map.Entry<java.sql.Date, Double> entry : returnRates.entrySet()) {
            chartInfos.add(new ChartInfo(entry.getKey(), entry.getValue()));
        }

        List<ChartInfo> chartInfos1 = new ArrayList<>();
        for (Map.Entry<String, Double> entry : stockRank.entrySet()) {
            chartInfos1.add(new ChartInfo(entry.getKey(), entry.getValue()));
        }

        Map<String, List<ChartInfo>> result = new HashMap<>();
        result.put(rr, chartInfos);
        result.put(sc, chartInfos1);
        return result;
    }

    /**
     * 获得行业分布
     *
     * @param stockCombination
     * @return
     */
    private List<ChartInfo> getIndustryDistribution(StockCombination stockCombination) {
        Map<String, Double> count = new HashMap<>();

        List<String> stockIDs = stockCombination.getStocks();
        Map<String, String> industries = industryDao.getIndustryOfStock(stockIDs.toArray(new String[0]));
        for (Map.Entry<String, String> entry : industries.entrySet()) {
            if (count.get(entry.getValue()) == null) {
                count.put(entry.getValue(), 1.0);
            } else {
                double tmp = count.get(entry.getValue());
                tmp += 1;
                count.put(entry.getValue(), tmp);
            }
        }

        List<ChartInfo> chartInfos = new ArrayList<>();
        for (Map.Entry<String, Double> entry : count.entrySet()) {
            chartInfos.add(new ChartInfo(entry.getKey(), entry.getValue() / stockIDs.size()));
        }
        return chartInfos;
    }

    /**
     * 获得调仓日调仓详情
     *
     * @param stockCombinations 所有调仓组合
     * @return 调仓日详情
     */
    private Map<Timestamp, List<Double>> getPositionInfo(List<StockCombination> stockCombinations) {
        Map<Timestamp, List<Double>> positioninfo = new HashMap<>();

        for (int i = 1; i < stockCombinations.size(); i++) {
            StockCombination before = stockCombinations.get(i - 1);
            StockCombination after = stockCombinations.get(i);
            Timestamp time = after.getTime();
            List<Double> res = new ArrayList<>();

            List<String> beforeStocks = before.getStocks();
            List<Double> beforePositions = before.getPositions();
            List<String> afterStocks = after.getStocks();

            for (int j = 0; j < afterStocks.size(); j++) {
                String code = afterStocks.get(j);
                int index = beforeStocks.indexOf(code);
                double from = (index == -1) ? 0.0 : beforePositions.get(index);
                res.add(from);
            }
            positioninfo.put(time, res);
        }
        return positioninfo;
    }
}
