package bridge.exceptions;

/**
 * Thrown when Object wasn't found
 */
public class ObjectNotFoundException extends Exception {

    /**
     * Constructs a new exception related to a plugin
     * {@link Exception#Exception(String)}
     *
     * @param message The Message.
     */
    public ObjectNotFoundException (String message){
        super(message);
    }

    /**
     * Constructs a new exception related to a plugin
     * {@link Exception#Exception(String)}
     *
     * @param throwable  The Throwable
     */
    public ObjectNotFoundException (Throwable throwable){
        super(throwable);
    }

    /**
     * Constructs a new exception related to a plugin
     * {@link Exception#Exception(String)}
     *
     * @param message The Message.
     * @param throwable  The Throwable
     */
    public ObjectNotFoundException (String message, Throwable throwable){
        super(message, throwable);
    }

}
