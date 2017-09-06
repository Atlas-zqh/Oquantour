package oquantour.service.serviceImpl;

import oquantour.data.dao.PlateDao;
import oquantour.po.PlateRealTimePO;
import oquantour.po.PlateinfoPO;
import oquantour.po.util.ChartInfo;
import oquantour.service.PlateService;
import oquantour.util.tools.PlateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by keenan on 10/06/2017.
 */
@Service
@Transactional
public class PlateServiceImpl implements PlateService {
    @Autowired
    PlateDao plateDao;

    /**
     * 根据板块名获得板块内股票
     *
     * @param plateName 板块名数组
     * @return 股票名;股票号
     */
    @Override
    public List<String> getStockByPlate(String... plateName) {
        return plateDao.getAllStockInfoInPlate(plateName);
    }

    /**
     * 获得所有板块名
     *
     * @return 所有板块名
     */
    @Override
    public List<String> getAllPlateName() {
        return plateDao.getAllPlateName();
    }

    /**
     * 获得板块收益率
     *
     * @param startDate
     * @param endDate
     * @param plateName 板块类型  @return
     */
    @Override
    public Map<String, List<ChartInfo>> getPlateReturnRates(Date startDate, Date endDate, String... plateName) {
        Map<String, List<ChartInfo>> map = new HashMap<>();

        for (String plate : plateName) {
            List<PlateinfoPO> plateinfoPOS = plateDao.getPlateInfo(plate, startDate, endDate);
            List<ChartInfo> chartInfos = new ArrayList<>();
            plateinfoPOS.stream().forEachOrdered(plateinfoPO -> chartInfos.add(new ChartInfo(plateinfoPO.getDateValue(), plateinfoPO.getReturnRate())));
            map.put(plate, chartInfos);
        }
        return map;
    }

    /**
     * 获得实时板块数据
     *
     * @return Map<板块名称, PlateRealTimePO></>
     */
    @Override
    public Map<String, PlateRealTimePO> getRealTimePlateInfo() {
        return plateDao.getRealTimePlateInfo();
    }
}
