package oquantour.exception;

import java.sql.Date;

/**
 * Created by keenan on 09/05/2017.
 */
public class NotTransactionDayException extends Exception {
    private Date date;

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public NotTransactionDayException() {
        super();
    }

    public NotTransactionDayException(Date date) {
        super();
        this.date = date;
    }

    public Date getDate() {
        return date;
    }
}
