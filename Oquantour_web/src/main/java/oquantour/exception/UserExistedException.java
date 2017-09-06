package oquantour.exception;

/**
 * 用户已存在
 * <p>
 * Created by keenan on 06/05/2017.
 */
public class UserExistedException extends Exception {
    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public UserExistedException() {
        super();
    }
}
