package com.gdt.chess.service;

public class IllegalMoveException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IllegalMoveException(String message) {
        super(message);
    }
}
