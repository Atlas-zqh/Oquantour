package oquantour.data.datahelper;

import oquantour.po.UserPO;
import oquantour.util.tools.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 * Created by island on 5/5/17.
 */
public class UserDataHelper {
    //Session session = HibernateUtil.getSession();

    /**
     * 数据库中新增用户
     * @param userPO
     * @return
     */
    public boolean addUser(UserPO userPO){
        Session session = null;
        try {
            session = HibernateUtil.getSession();
            session.beginTransaction();

            session.save(userPO);
            return true;
        } catch (HibernateException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (null != session) {
                session.getTransaction().commit();
                HibernateUtil.closeSession(session);
            }
        }
        //setHibernateTemplate();
        //this.hibernateTemplate.save(userPO);
    }

    /**
     * 数据库中更新用户信息
     * @param userPO
     * @return
     */
    public boolean modifyUser(UserPO userPO) {
        Session session = null;
        try {
            session = HibernateUtil.getSession();
            session.beginTransaction();

            session.update(userPO);
            return true;
        } catch (HibernateException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (null != session) {
                session.getTransaction().commit();
                HibernateUtil.closeSession(session);
            }
        }
    }

    /**
     * 根据用户名在数据库中查找用户
     * @param username
     * @return
     */
    public UserPO findUserByName(String username) {
        Session session = null;
        try {
            session = HibernateUtil.getSession();
            session.beginTransaction();

            UserPO userPO = (UserPO) session.get(UserPO.class, username);

            return userPO;
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.getTransaction().commit();
                HibernateUtil.closeSession(session);
            }
        }
    }
}
