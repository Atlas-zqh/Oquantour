package exception;

/**
 * 不存在形成期的数据
 * <p>
 * Created by keenan on 11/04/2017.
 */
public class FormativePeriodNotExistException extends Exception {
    public FormativePeriodNotExistException() {
        super();
    }

    public FormativePeriodNotExistException(String msg) {
        super(msg);
    }
}
