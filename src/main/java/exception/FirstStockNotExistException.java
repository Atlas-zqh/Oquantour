package exception;

/**
 * Created by Pxr on 2017/3/15.
 */
public class FirstStockNotExistException extends Exception {
    public FirstStockNotExistException() {
        super();
    }

    public FirstStockNotExistException(String msg) {
        super(msg);
    }
}
