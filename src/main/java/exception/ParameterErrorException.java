package exception;

/**
 * Created by keenan on 31/03/2017.
 */
public class ParameterErrorException extends Exception {
    public ParameterErrorException() {
        super();
    }

    public ParameterErrorException(String msg) {
        super(msg);
    }
}
