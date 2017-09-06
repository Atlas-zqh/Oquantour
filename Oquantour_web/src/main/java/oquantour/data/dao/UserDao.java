package oquantour.data.dao;

import oquantour.po.PersonalStrategyPO;
import oquantour.po.UserPO;

import java.util.List;

/**
 * Created by island on 5/5/17.
 */
public interface UserDao {
    /**
     * 在数据库中新增用户
     * @param userPO
     * @return
     */
    void addUser(UserPO userPO);

    /**
     * 在数据库中修改用户信息
     * @param userPO
     * @return
     */
    void modifyUser(UserPO userPO);

    /**
     * 根据用户名获得数据库中用户
     * @param username
     * @return
     */
    UserPO findUserByName(String username);

    /**
     * 用户添加自选股票
     * @param userName
     * @param stockID
     */
    void addSelectedStock(String userName, String stockID);

    /**
     * 删除用户自选股
     * @param userName
     * @param stocks
     */
    void deleteSelectedStock(String userName, String... stocks);

    /**
     * 根据用户名获得该用户所有自选股票
     * @param userName
     * @return
     */
    List<String> getSelectedStock(String userName);


    void addPersonalStrategy(PersonalStrategyPO personalStrategyPO);

    PersonalStrategyPO getPersonalStrategy(String userName);
}
