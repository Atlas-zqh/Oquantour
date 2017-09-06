package oquantour.service.serviceImpl;

import oquantour.data.dao.UserDao;
import oquantour.po.StockPO;
import oquantour.service.OptionalStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by keenan on 23/05/2017.
 */
@Service
@Transactional
public class OptionalStockServiceImpl implements OptionalStockService {
    @Autowired
    private UserDao userDao;

    private Map<String, List<String>> map = new HashMap<>();

    /**
     * 增加自选股
     *
     * @param account 用户账户
     * @param stockID 股票代码
     */
    @Override
    public void addOptionalStock(String account, String stockID) {
        userDao.addSelectedStock(account, stockID);
        refreshMap(account);
    }

    /**
     * 删除自选股
     *
     * @param account 用户账户
     * @param stockID 股票代码
     */
    @Override
    public void deleteOptionalStock(String account, String stockID) {
        userDao.deleteSelectedStock(account, stockID);
        refreshMap(account);
    }

    /**
     * 判断股票是否已在自选股中
     *
     * @param account 用户账户
     * @param stockID 股票代码
     * @return 是否已在自选股中
     */
    @Override
    public boolean isInOptionalStock(String account, String stockID) {
        List<String> optionalStocks = map.get(account);

        if (optionalStocks == null) {
            refreshMap(account);

            List<String> os = map.get(account);
            if (os == null) {
                return false;
            } else {
                return os.contains(stockID);
            }
        } else {
            return optionalStocks.contains(stockID);
        }
    }

    /**
     * 获得某用户所有自选股
     *
     * @param account 用户账户
     * @return 用户所有自选股
     */
    @Override
    public List<String> getAllOptionalStock(String account) {
        List<String> optionalStocks = map.get(account);
        if (optionalStocks == null) {
            refreshMap(account);

            List<String> os = map.get(account);
            if (os == null) {
                return Collections.emptyList();
            } else {
                return os;
            }
        } else {
            return optionalStocks;
        }
    }

    private void refreshMap(String account) {
        map.put(account, userDao.getSelectedStock(account));

        System.out.println(account + " " + map.get(account).size());
    }
}
