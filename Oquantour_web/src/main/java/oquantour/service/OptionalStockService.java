package oquantour.service;

import oquantour.po.StockPO;

import java.util.List;

/**
 * 自选股相关
 * Created by keenan on 16/05/2017.
 */
public interface OptionalStockService {
    /**
     * 增加自选股
     *
     * @param account 用户账户
     * @param stockID 股票代码
     */
    void addOptionalStock(String account, String stockID);

    /**
     * 删除自选股
     *
     * @param account 用户账户
     * @param stockID 股票代码
     */
    void deleteOptionalStock(String account, String stockID);

    /**
     * 判断股票是否已在自选股中
     *
     * @param account 用户账户
     * @param stockID 股票代码
     * @return 是否已在自选股中
     */
    boolean isInOptionalStock(String account, String stockID);

    /**
     * 获得某用户所有自选股
     *
     * @param account 用户账户
     * @return 用户所有自选股
     */
    List<String> getAllOptionalStock(String account);
}
