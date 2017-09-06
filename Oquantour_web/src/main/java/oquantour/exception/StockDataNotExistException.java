package oquantour.exception;

import java.sql.Date;

/**
 * Created by keenan on 09/05/2017.
 */
public class StockDataNotExistException extends Exception {
    private String stockCode;

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public StockDataNotExistException() {
    }

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public StockDataNotExistException(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getStockCode() {
        return stockCode;
    }

}
