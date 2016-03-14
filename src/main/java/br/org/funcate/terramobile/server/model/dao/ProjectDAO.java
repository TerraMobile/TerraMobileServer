package br.org.funcate.terramobile.server.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import br.org.funcate.terramobile.server.model.DatabaseHelper;
import br.org.funcate.terramobile.server.model.Project;
import br.org.funcate.terramobile.server.model.exception.DAOException;
import br.org.funcate.terramobile.server.model.exception.DatabaseException;
import br.org.funcate.terramobile.server.model.exception.ProjectException;

public class ProjectDAO
{

	private DatabaseHelper helper;

	private Project project;

	public ProjectDAO(Project project, DatabaseHelper helper) {

		this.helper = helper;
		this.project = project;
	}

	
	private String getSetting(String key) throws DAOException, DatabaseException
	{
		ResultSet resultSet = null;
		Statement statement = null;
		String settingValue = null;
		try
		{
			statement = helper.getConnection().createStatement();

			resultSet = statement
					.executeQuery("select value from tm_settings where key=='"+key+"'");
			if (resultSet.next())
			{
				settingValue = resultSet.getString("value");
			}
			/*else
			}
			{
				throw new DAOException("Missing '"+key+"' setting");
			}*/
			

		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DAOException("Failed getting project '"+key+"' setting.", e);
		} finally
		{
			try
			{
				if (statement != null)
				{
					statement.close();
				}

				if (resultSet != null)
				{
					resultSet.close();	
				}

			} catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new DatabaseException("Unable to close statement and resultset.", e);
			}
		}
		return settingValue;
	}
	public String getUUID() throws DAOException, DatabaseException
	{

		String projectId = getSetting("project_id");
		project.setUUID(projectId);
		return projectId;
	}
	
	public int getStatus() throws DAOException, DatabaseException, ProjectException
	{

		String projectStatus = getSetting("project_status");
		int projectStatusInt = 0;
		try {
			projectStatusInt = Integer.parseInt(projectStatus);	
		}
		catch(NumberFormatException e)
		{
			throw new ProjectException("Invalid project status format.", e);
		}
		
		project.setStatus(projectStatusInt);
		return projectStatusInt;
	}
	
	public String getDescription() throws DAOException, DatabaseException, ProjectException
	{

		String projectStatus = getSetting("project_description");
		
		project.setDescription(projectStatus);
		return projectStatus;
	}
}
