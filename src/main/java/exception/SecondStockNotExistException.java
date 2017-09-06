package exception;

/**
 * Created by Pxr on 2017/3/15.
 */
public class SecondStockNotExistException extends Exception {
    public SecondStockNotExistException() {
        super();
    }

    public SecondStockNotExistException(String msg) {
        super(msg);
    }
}
