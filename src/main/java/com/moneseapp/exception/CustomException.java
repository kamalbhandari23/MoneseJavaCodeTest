package com.moneseapp.exception;

public class CustomException extends RuntimeException {

	/**
	 * The custom exception class.
	 */
	private static final long serialVersionUID = 442681069804339550L;

	public CustomException(String msg) {
		super(msg);
	}
}
