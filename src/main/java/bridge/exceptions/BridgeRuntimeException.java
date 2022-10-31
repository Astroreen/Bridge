package bridge.exceptions;

public class BridgeRuntimeException extends Exception {


    /**
     * {@link Exception#Exception(String)}
     *
     * @param msg the displayed message.
     */
    public BridgeRuntimeException(final String msg) {
        super(msg);
    }

    /**
     * {@link Exception#Exception(String, Throwable)}
     *
     * @param msg   the exception message.
     * @param cause the Throwable that caused this exception.
     */
    public BridgeRuntimeException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

    /**
     * {@link Exception#Exception(Throwable)}
     *
     * @param cause the Throwable that caused this exception.
     */
    public BridgeRuntimeException(final Throwable cause) {
        super(cause);
    }
}
