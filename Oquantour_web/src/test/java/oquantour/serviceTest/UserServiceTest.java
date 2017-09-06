package oquantour.serviceTest;

import oquantour.BaseTest;
import oquantour.data.dao.UserDao;
import oquantour.po.UserPO;
import oquantour.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by keenan on 24/05/2017.
 */
public class UserServiceTest extends BaseTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Test
    public void testFind() {
        String username = "ljr";
        try {
            UserPO userPO = userService.findUserByID(username);

            System.out.println(userPO.getPhone());
            System.out.println(userPO.getUserPassword());
            System.out.println(userPO.getUserName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdate() {
        String username = "ljr";

        try {
            UserPO userPO = userService.findUserByID(username);

            System.out.println(userPO.toString());

            userPO.setPhone("13013822266");
            System.out.println(userPO.toString());
            userService.modifyUser(userPO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateDao() {
        String username = "ljr";

        UserPO userPO = userDao.findUserByName(username);
        userPO.setPhone("13013822266");
        userDao.modifyUser(userPO);
    }
}
