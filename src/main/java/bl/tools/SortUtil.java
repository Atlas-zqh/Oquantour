package bl.tools;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * 排序工具类
 * <p>
 * List<SortObject> list;
 * SortUtil.sortDescending(list, sortObject -> sortObject.getExtraReturnRate());
 * Created by keenan on 25/03/2017.
 */
public class SortUtil {

    /**
     * 根据指定属性对集合顺序排序（从小到大）
     *
     * @param data 集合对象
     * @param func 委托
     * @param <T>  数据类型
     * @param <R>  要排序的属性的数据类型
     */
    public static <T, R extends Comparable<? super R>> void sort(List<T> data, Function<T, R> func) {
        Comparator<T> comparator = Comparator.comparing(func);
        data.sort(comparator);
    }

    /**
     * 根据指定属性对集合倒序排序（从大到小）
     *
     * @param data 集合对象
     * @param func 委托
     * @param <T>  数据类型
     * @param <R>  要排序的属性的数据类型
     */
    public static <T, R extends Comparable<? super R>> void sortDescending(List<T> data, Function<T, R> func) {
        Comparator<T> comparator = Comparator.comparing(func).reversed();
        data.sort(comparator);
    }


}
