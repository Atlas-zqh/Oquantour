package oquantour.DAOTest;

import oquantour.BaseTest;
import oquantour.data.dao.PlateDao;
import oquantour.po.PlateinfoPO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

/**
 * Created by keenan on 09/06/2017.
 */
public class PlateDaoTest extends BaseTest {
    @Autowired
    PlateDao plateDao;

    @Test
    public void testGetPlate() {
        Date today = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        calendar.set(year - 1, month, day);
        long one = calendar.getTimeInMillis();
        java.sql.Date oneYear = new java.sql.Date(one);

        List<PlateinfoPO> SH_Composite = plateDao.getPlateInfo("上证指数", oneYear, today);
        List<PlateinfoPO> SmallPlate_Composite = plateDao.getPlateInfo("中小板指", oneYear, today);
        List<PlateinfoPO> Gem_Composite = plateDao.getPlateInfo("创业板指", oneYear, today);
        List<PlateinfoPO> SS_300 = plateDao.getPlateInfo("沪深300", oneYear, today);
        List<PlateinfoPO> SZ_Composite = plateDao.getPlateInfo("深证成指", oneYear, today);
        List<PlateinfoPO> SZA_Composite = plateDao.getPlateInfo("深证Ａ指", oneYear, today);

        System.out.println(SH_Composite.size());
        System.out.println(SmallPlate_Composite.size());
        System.out.println(Gem_Composite.size());
        System.out.println(SS_300.size());
        System.out.println(SZ_Composite.size());
        System.out.println(SZA_Composite.size());

    }
}
