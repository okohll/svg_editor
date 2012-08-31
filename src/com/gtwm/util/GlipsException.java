package com.gtwm.util;

/**
 * Catch-all application exception for use when generating internally
 */
public class GlipsException extends Exception {

	/**
	 * We have to provide at least a message when creating this exception
	 */
	private GlipsException() {
	}
	
	public GlipsException(String message) {
		super(message);
	}
	
	public GlipsException(String message, Throwable cause) {
        super(message, cause);
    }

}
