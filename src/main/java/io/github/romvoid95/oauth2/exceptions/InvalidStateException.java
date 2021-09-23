package io.github.romvoid95.oauth2.exceptions;

/**
 * Exception raised when the provided OAuth2 state is not valid.
 *
 * <p>
 * <b>Not to be confused with {@link java.lang.IllegalStateException
 * IllegalStateException}</b>
 *
 * 
 */
public class InvalidStateException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidStateException(String message) {
	super(message);
    }
}
