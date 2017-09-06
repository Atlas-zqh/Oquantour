package oquantour.service;

import oquantour.exception.WrongCombinationException;
import oquantour.po.StockCombination;
import oquantour.po.util.PortfolioInfo;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * 处理投资组合相关功能
 * <p>
 * Created by keenan on 22/05/2017.
 */
public interface PortfolioService {

    /**
     * 增加股票组合
     *
     * @param username      用户名
     * @param portfolioName 组合名称
     * @param stocks        股票号
     * @param positions     持股数
     * @throws WrongCombinationException 当stocks, positions, prices长度不匹配时会抛出
     */
    void addPortfolio(String username, String portfolioName, List<String> stocks, List<Double> positions) throws WrongCombinationException;

    /**
     * 修改股票组合
     *
     * @param username      用户名
     * @param portfolioName 组合名称
     * @param stocks        股票号
     * @param positions     持股数
     * @throws WrongCombinationException 当stocks, positions, prices长度不匹配时会抛出
     */
    void modifyPortfolio(String username, String portfolioName, List<String> stocks, List<Double> positions) throws WrongCombinationException;

    /**
     * 根据用户名和组合名获得股票组合
     *
     * @param username      用户名
     * @param portfolioName 组合名称
     * @return 用户组合信息及分析
     */
    PortfolioInfo getPortfolio(String username, String portfolioName);

    /**
     * 删除投资组合
     *
     * @param userAccount   用户账户
     * @param portfolioName 用户投资组合名称
     */
    void deletePortfolio(String userAccount, String portfolioName);

    /**
     * 得到用户所有投资组合
     *
     * @param username 用户名
     * @return 所有投资组合<组合名, 创建时间>
     */
    List<Map.Entry<String, Timestamp>> getAllPortfolios(String username);

    /**
     * 获得某一组合最新调仓情况
     *
     * @param username      用户名
     * @param portfolioName 组合名
     * @return 某一组合最新调仓情况
     */
    StockCombination getLatestPortfolio(String username, String portfolioName);
}
