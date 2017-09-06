package exception;

/**
 * Created by Pxr on 2017/3/15.
 */
public class BothStockNotExistException extends Exception {
    public BothStockNotExistException() {
        super();
    }

    public BothStockNotExistException(String msg) {
        super(msg);
    }
}
