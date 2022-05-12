
package com.readonlydev.oauth2.exceptions;

import com.readonlydev.oauth2.Scope;

/**
 * Exception raised whenever attempting to perform an action or function with a
 * missing {@link com.readonlydev.oauth2.Scope Scope}.
 */
public class MissingScopeException extends RuntimeException {

    private static final long   serialVersionUID = 1L;

    private static final String FORMAT           = "Cannot %s without '%s' scope!";

    public MissingScopeException(String action, Scope missing) {
        super(String.format(FORMAT, action, missing.getText()));
    }
}
