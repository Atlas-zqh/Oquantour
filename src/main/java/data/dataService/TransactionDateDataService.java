package data.dataService;

import po.Stock;

import java.util.Date;
import java.util.List;

/**
 * Created by keenan on 07/03/2017.
 */
public interface TransactionDateDataService {
    /**
     * 得到某一日的前n个交易日的股票数据
     *
     * @param daysBefore n的值
     * @param date       某一日
     * @return 前n个交易日的数据（若没有，则为空）
     */
    List<Stock> getDaysBeforeByCode(int daysBefore, Date date, String code);

    /**
     * 根据日期得到这个交易日和前一个交易日的数据
     * 如果这个交易日之前没有交易日，则返回的Stock[]长度为1
     * 如果这个交易日之前有交易日，则返回的Stock[]长度为2
     * 如果这一日不是交易日，或某个股票这一日没有交易数据，则返回的Stock[]长度为0
     *
     * @param date 要查询的日期
     * @return 791个股票的当前交易日和前一交易日的信息
     */
    List<Stock[]> getStockOfTodayAndYesterday(Date date);

    /**
     * 根据股票代码获得某只股票的全部交易日
     * @param stock 股票代码/
     * @return 所有交易日
     */
    List<Date> getAllTransactionDays(String stock);
}
