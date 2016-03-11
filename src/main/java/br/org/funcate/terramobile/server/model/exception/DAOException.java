package br.org.funcate.terramobile.server.model.exception;

public class DAOException extends Exception {

	private static final long serialVersionUID = 5398324537531733587L;

	public DAOException(String message)
	{
		super(message);
	}

		
	
	public DAOException(String message, Throwable e)
	{
		super(message, e);
	}
	
}
