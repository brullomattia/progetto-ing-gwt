package com.progetto.server;

public class DatabaseException extends Exception{
	public DatabaseException(String errorMessage)
	{
		super(errorMessage);
	}
}
