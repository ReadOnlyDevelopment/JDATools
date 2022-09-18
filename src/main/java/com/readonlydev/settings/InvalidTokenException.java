package com.readonlydev.settings;


public class InvalidTokenException extends Exception
{

    private static final long serialVersionUID = 1L;


    InvalidTokenException(String invalidToken)
    {
        super("The token [%s] provided does not match a valid Discord Bot Token".formatted(invalidToken));
    }

}
