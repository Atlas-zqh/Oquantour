package oquantour.data.datahelper;

import oquantour.po.StockPO;
import oquantour.po.StockPOPK;
import oquantour.util.tools.HibernateUtil;
import org.hibernate.Session;

/**
 * Created by island on 5/5/17.
 */
public class StockDataHelper {
    Session session = HibernateUtil.getSession();

    public StockPO findStockByStockPOPK(StockPOPK stockPOPK){

        try {
            StockPO stockPO = (StockPO) session.get(StockPO.class, stockPOPK);
            return stockPO;
        } finally {
            if(session != null)
                session.close();
        }
    }

}
