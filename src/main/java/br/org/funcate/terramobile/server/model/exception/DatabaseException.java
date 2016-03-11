package br.org.funcate.terramobile.server.model.exception;

public class DatabaseException extends Exception {

	private static final long serialVersionUID = 5398324537531733587L;

	public DatabaseException(String message, Throwable originalException)
	{
		super(message, originalException);
	}
	
}
