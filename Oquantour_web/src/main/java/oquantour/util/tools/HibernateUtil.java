package oquantour.util.tools;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Hibernate事务处理工具包
 * <p>
 * Created by qky on 2017/04/19.
 */
public class HibernateUtil {
    private static final SessionFactory sessionfactory;
    private static Session session;

    static {
        try {
            sessionfactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionfactory() {
        return sessionfactory;
    }

    public static Session getSession() throws HibernateException {
        session = sessionfactory.openSession();
        return session;
    }

    public static void closeSession(Session session) {
        if (null != session) {
            session.close();
        }
    }

}
