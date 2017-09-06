package oquantour.data.dao;

import oquantour.exception.WrongCombinationException;
import oquantour.po.StockCombination;
import oquantour.po.StockCombinationPO;

import java.util.List;
import java.util.Map;

/**
 * Created by island on 2017/6/5.
 */
public interface CombinationDao {
    /**
     * 增加股票组合
     * @param username
     * @param combinationName
     * @param stocks
     * @param positions
     */
    void addStockCombination(String username, String combinationName, List<String> stocks, List<Double> positions) throws WrongCombinationException;

    /**
     * 修改股票组合
     * @param username
     * @param combinationName
     * @param stocks
     * @param positions
     */
    void modifyStockCombination(String username, String combinationName, List<String> stocks, List<Double> positions) throws WrongCombinationException;

    /**
     * 删除股票组合
     * @param username
     * @param combinationName
     */
    void deleteStockCombination(String username, String combinationName);

    /**
     * 根据用户名和组合名获得股票组合
     * @param username
     * @param combinationName
     * @return
     */
    List<StockCombination> getStockCombination(String username, String combinationName);

    /**
     * 获得某用户所有股票组合
     * @param username
     * @return
     */
    Map<String, List<StockCombination>> getAllStockCombinationOfUser(String username);
}
