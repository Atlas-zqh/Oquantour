package bl.tools;

import vo.StockVO;

import java.util.Comparator;

/**
 * 排序
 * Created by Pxr on 2017/3/9.
 */
public class ComparatorChg implements Comparator<StockVO> {
    @Override
    public int compare(StockVO o1, StockVO o2) {
        if((o1.chg - o2.chg)>=0)
            return -1;
        else
            return 1;
    }
}
