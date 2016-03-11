package br.org.funcate.terramobile.server.model;

import java.sql.Connection;
import java.sql.DriverManager;

import br.org.funcate.terramobile.server.model.exception.DatabaseException;

public class DatabaseHelper
{

	private Connection connection;

	private String sqliteFilePath;

	public DatabaseHelper(String sqliteFile) throws DatabaseException {

		this.sqliteFilePath = sqliteFile;

		try
		{
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:"
					+ sqliteFile);
		} catch (Exception e)
		{
			throw new DatabaseException(
					"Failed to connect to SQLite database: " + sqliteFilePath,
					e);
		} finally
		{

		}
	}

	public void close() throws DatabaseException
	{

		try
		{
			connection.close();
		} catch (Exception e)
		{
			throw new DatabaseException("Failed to close SQLite connection: "
					+ sqliteFilePath, e);
		}
	}

	public Connection getConnection()
	{
	
		return connection;
	}

	public void setConnection(Connection connection)
	{
	
		this.connection = connection;
	}
}
