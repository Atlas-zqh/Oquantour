package exception;

/**
 * 选择回测股票范围时可能会出现的异常
 * 正常情况：选择所有股票、某个板块股票或者自选股票（不少于100只）
 * Created by st on 2017/3/27.
 */
public class SelectStocksException extends Exception {
    public SelectStocksException() {
        super();
    }

    public SelectStocksException(String msg) {
        super(msg);
    }
}
