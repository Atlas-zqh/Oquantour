package bl.tools;

import po.Stock;

import java.util.*;

/**
 * 从map中提出Date的列表（排过序的）
 * <p>
 * Created by keenan on 31/03/2017.
 */
public class SplitMapElements {

    /**
     * 从map中提出Date的列表（排过序的）
     *
     * @param listMap map
     * @return 排过序的日期列表
     */
    public static List<Date> splitKeys(Map<Date, List<Stock>> listMap) {
        List<Date> dateList = new ArrayList<>(listMap.keySet());
        Collections.sort(dateList);
        return dateList;
    }
}
