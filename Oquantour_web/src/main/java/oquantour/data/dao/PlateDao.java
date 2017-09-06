package oquantour.data.dao;

import oquantour.po.PlateRealTimePO;
import oquantour.po.PlateinfoPO;
import oquantour.po.StockPO;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by island on 5/10/17.
 */
public interface PlateDao {
    /**
     * 根据板块名称，获得该段时间内存在数据的股票
     * @param plateName
     * @param startDate
     * @param endDate
     * @return
     */
    Map<String, List<StockPO>> getStockInStockPlate(String plateName, Date startDate, Date endDate);

    /**
     * 根据股票信息获得它所在的板块
     * @param stockInfo 股票名或代码
     * @return 板块名
     */
    String getStockPlate(String stockInfo);

    /**
     * 得到板块中的所有股票 代码;名称
     * @param plateName 板块名
     * @return 板块中的所有股票的stockID;stockName
     */
    List<String> getAllStockInfoInPlate(String... plateName);

    /**
     * 获得板块一段时间内每个交易日的收益率
     * @param plateName
     * @param startDate
     * @param endDate
     * @return
     */
    List<PlateinfoPO> getPlateInfo(String plateName, Date startDate, Date endDate);

    /**
     * 获得所有板块名称
     * @return
     */
    List<String> getAllPlateName();

    /**
     * 获得实时板块数据
     * @return Map<板块名称, PlateRealTimePO></>
     */
    Map<String, PlateRealTimePO> getRealTimePlateInfo();

    /**
     *  更新实时板块数据
     */
    void updateRealTimePlateInfo();

    /**
     * 添加板块信息
     */
    void addPlateInfo();

    /**
     * 更新板块信息
     * @param plateinfoPO
     */
    void updatePlateInfo(PlateinfoPO plateinfoPO);
}
