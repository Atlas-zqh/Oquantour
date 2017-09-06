package oquantour.service;

import oquantour.po.PlateRealTimePO;
import oquantour.po.util.ChartInfo;
import oquantour.util.tools.PlateEnum;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by keenan on 10/06/2017.
 */
public interface PlateService {
    /**
     * 根据板块名获得板块内股票
     *
     * @param plateName 板块名数组
     * @return 股票名;股票号
     */
    List<String> getStockByPlate(String... plateName);

    /**
     * 获得所有板块名
     *
     * @return 所有板块名
     */
    List<String> getAllPlateName();

    /**
     * 获得板块收益率
     *
     * @param plateName 板块类型
     * @return
     */
    Map<String, List<ChartInfo>> getPlateReturnRates(Date startDate, Date endDate, String... plateName);

    /**
     * 获得实时板块数据
     *
     * @return Map<板块名称, PlateRealTimePO></>
     */
    Map<String, PlateRealTimePO> getRealTimePlateInfo();

}
