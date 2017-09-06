package oquantour.data.daoImpl;

import oquantour.data.dao.CombinationDao;
import oquantour.exception.WrongCombinationException;
import oquantour.po.StockCombination;
import oquantour.po.StockCombinationPO;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by island on 2017/6/5.
 */
@Transactional(readOnly = false)
@Repository
public class CombinationDaoImpl implements CombinationDao{
    //使用Spring注入HibernateTemplate的一个实例
    private HibernateTemplate hibernateTemplate;

    public HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }

    @Resource(name="hibernateTemplate")
    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    /**
     * 增加股票组合
     * @param username
     * @param combinationName
     * @param stocks
     * @param positions
     */
    @Override
    public void addStockCombination(String username, String combinationName, List<String> stocks, List<Double> positions) throws WrongCombinationException {
        List<StockCombinationPO> stockCombinationPOS = (List<StockCombinationPO>)this.getHibernateTemplate().find("from oquantour.po.StockCombinationPO where userName = ? and combinationName = ?", new Object[]{username, combinationName});

        if(stockCombinationPOS.size() > 0){
            throw new WrongCombinationException("该用户已有同名组合");
        }
        if(stocks.size() != positions.size()) {
            throw new WrongCombinationException("股票和仓位数组长度不一致");
        }

        StockCombinationPO stockCombinationPO = processCombination(username, combinationName, stocks, positions);
        this.getHibernateTemplate().save(stockCombinationPO);
    }

    /**
     * 修改股票组合
     * @param username
     * @param combinationName
     * @param stocks
     * @param positions
     */
    @Override
    public void modifyStockCombination(String username, String combinationName, List<String> stocks, List<Double> positions) throws WrongCombinationException {
        List<StockCombinationPO> stockCombinationPOS = (List<StockCombinationPO>)this.getHibernateTemplate().find("from oquantour.po.StockCombinationPO where userName = ? and combinationName = ?", new Object[]{username, combinationName});
        if(stockCombinationPOS.size() == 0){
            throw new WrongCombinationException("没有该组合");
        }
        if(stocks.size() != positions.size()) {
            throw new WrongCombinationException("股票和仓位数组长度不一致");
        }
        StockCombinationPO stockCombinationPO = processCombination(username, combinationName, stocks, positions);
        this.getHibernateTemplate().save(stockCombinationPO);
    }

    private StockCombinationPO processCombination(String username, String combinationName, List<String> stocks, List<Double> positions){
        StockCombinationPO stockCombinationPO = new StockCombinationPO();
        stockCombinationPO.setCombinationName(combinationName);
        stockCombinationPO.setUserName(username);
        String stock = "";
        String position = "";
//        String price = "";
        for(int i = 0; i < stocks.size(); i++){
            stock += stocks.get(i) + ";";
            position += ("" + positions.get(i)) + ";";
//            price += prices.get(i) + ";";
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        java.util.Date date = new java.util.Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        stockCombinationPO.setSaveTime(timestamp);
        stockCombinationPO.setStocks(stock);
        stockCombinationPO.setPosition(position);
//        stockCombinationPO.setPrices(price);

        return stockCombinationPO;
    }

    /**
     * 根据用户名和组合名获得股票组合
     * @param username
     * @param combinationName
     * @return
     */
    @Override
    public List<StockCombination> getStockCombination(String username, String combinationName) {
        List<StockCombinationPO> stockCombinationPOS = (List<StockCombinationPO>)this.getHibernateTemplate().find("from oquantour.po.StockCombinationPO where userName = ? and combinationName = ? ", new Object[]{username, combinationName});
        for(int i = 0; i < stockCombinationPOS.size(); i++){
            this.getHibernateTemplate().evict(stockCombinationPOS.get(i));
        }
        if(stockCombinationPOS.size() == 0)
            return new ArrayList<>();
        List<StockCombination> stockCombinations = new ArrayList<>();
        for(int i = 0; i < stockCombinationPOS.size(); i++){
            StockCombinationPO stockCombinationPO = stockCombinationPOS.get(i);
            List<String> stocks = new ArrayList<>();
            List<Double> positions = new ArrayList<>();
            List<Double> prices = new ArrayList<>();
            String[] ss = stockCombinationPO.getStocks().split(";");
            String[] ps = stockCombinationPO.getPosition().split(";");
            //String[] prs = stockCombinationPO.getPrices().split(";");
            for(int j = 0; j < ss.length; j++){
                stocks.add(ss[j]);
                positions.add(Double.parseDouble(ps[j]));
//                System.out.println(stockCombinationPO.getSaveTime() + "," + stocks.get(j) + "," + positions.get(j));
                //prices.add(Double.parseDouble(prs[i]));
            }
            StockCombination stockCombination = new StockCombination();
            stockCombination.setUserName(username);
            stockCombination.setCombinationName(combinationName);
            stockCombination.setPositions(positions);
            stockCombination.setStocks(stocks);
            stockCombination.setTime(stockCombinationPO.getSaveTime());
            stockCombination.setPrices(prices);
            stockCombinations.add(stockCombination);
        }
        return stockCombinations;
    }

    @Override
    public void deleteStockCombination(String username, String combinationName) {
        List<StockCombinationPO> stockCombinationPOS = (List<StockCombinationPO>)this.getHibernateTemplate().find("from oquantour.po.StockCombinationPO where userName = ? and combinationName = ? ", new Object[]{username, combinationName});
        for(int i = 0; i < stockCombinationPOS.size(); i++){
            this.getHibernateTemplate().delete(stockCombinationPOS.get(i));
        }
    }

    @Override
    public Map<String, List<StockCombination>> getAllStockCombinationOfUser(String username) {
        List<String> combinationNames = (List<String>) this.getHibernateTemplate().find("select distinct combinationName from oquantour.po.StockCombinationPO where userName = ?", username);
        Map<String, List<StockCombination>> maps = new HashMap<>();
        for(int i = 0; i < combinationNames.size(); i++){
            maps.put(combinationNames.get(i), getStockCombination(username, combinationNames.get(i)));
        }
        return maps;
    }
}
