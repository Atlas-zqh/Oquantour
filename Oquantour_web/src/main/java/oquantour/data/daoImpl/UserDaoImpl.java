package oquantour.data.daoImpl;

import oquantour.data.dao.UserDao;
import oquantour.po.PersonalStrategyPO;
import oquantour.po.StockCombination;
import oquantour.po.StockSelectedByUser;
import oquantour.po.UserPO;
import oquantour.util.tools.EncryptionUtil;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by island on 5/5/17.
 */
@Transactional(readOnly = false)
@Repository
public class UserDaoImpl implements UserDao {
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
     * 在数据库中增加用户
     * @param userPO
     */
    @Override
    public void addUser(UserPO userPO) {
            this.hibernateTemplate.save(encryptUserPO(userPO));
    }


    @Override
    public void addPersonalStrategy(PersonalStrategyPO personalStrategyPO) {

    }

    @Override
    public PersonalStrategyPO getPersonalStrategy(String userName) {
        return null;
    }

    /**
     * 修改用户信息
     * @param userPO
     */
    @Override
    public void modifyUser(UserPO userPO) {
        this.hibernateTemplate.update(encryptUserPO(userPO));
    }

    /**
     * 用户添加自选股票
     * @param userName
     * @param stockID
     */
    @Override
    public void addSelectedStock(String userName, String stockID) {
        StockSelectedByUser stockSelectedByUser = new StockSelectedByUser();
        stockSelectedByUser.setUserName(userName);
        stockSelectedByUser.setStockId(stockID);
        this.hibernateTemplate.save(stockSelectedByUser);
    }

    /**
     * 删除用户自选股
     * @param userName
     * @param stocks
     */
    @Override
    public void deleteSelectedStock(String userName, String... stocks) {
        for(int i = 0; i < stocks.length; i++){
            List<StockSelectedByUser> stockSelectedByUser = (List<StockSelectedByUser>) this.hibernateTemplate.find("from oquantour.po.StockSelectedByUser where userName = ? and stockId = ?",
                    new Object[]{userName, stocks[i]});
            if(stockSelectedByUser.size() > 0)
                this.getHibernateTemplate().delete(stockSelectedByUser.get(0));
        }
    }

    /**
     * 根据用户名获得该用户所有自选股票
     * @param userName
     * @return
     */
    @Override
    public List<String> getSelectedStock(String userName) {
        List<String> stocks = (List<String>) this.hibernateTemplate.find("select distinct stockId from oquantour.po.StockSelectedByUser where userName = ?", userName);
        return stocks;
    }

    /**
     * 根据用户名查找用户
     * @param username
     * @return
     */
    @Override
    public UserPO findUserByName(String username) {
        UserPO user = new UserPO();
        String queryString = "select count(*) from bean.User u where u.name=:myName";

        String paramName= "myName";

        String value= "xiyue";

        //this.getHibernateTemplate().findByNamedParam(queryString, paramName, value);
        UserPO userPO = this.hibernateTemplate.get(UserPO.class, EncryptionUtil.encrypt(username));
        if(userPO != null) {
            this.getHibernateTemplate().evict(userPO);
            user = decryptUserPO(userPO, user);
            return user;
        }

        return null;

    }

    private UserPO encryptUserPO(UserPO userPO){
        String userName = EncryptionUtil.encrypt(userPO.getUserName());
        String userPassword = EncryptionUtil.encrypt(userPO.getUserPassword());
        String phone = EncryptionUtil.encrypt(userPO.getPhone());
        userPO.setUserName(userName);
        userPO.setUserPassword(userPassword);
        userPO.setPhone(phone);
        return userPO;
    }

    private UserPO decryptUserPO(UserPO userPO, UserPO newUser){
        String userName = EncryptionUtil.decrypt(userPO.getUserName());
        String userPassword = EncryptionUtil.decrypt(userPO.getUserPassword());
        String phone = EncryptionUtil.decrypt(userPO.getPhone());
        newUser.setUserName(userName);
        newUser.setUserPassword(userPassword);
        newUser.setPhone(phone);
        return newUser;
    }
}
