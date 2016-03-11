package br.org.funcate.terramobile.server.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import br.org.funcate.terramobile.server.model.DatabaseHelper;
import br.org.funcate.terramobile.server.model.Project;
import br.org.funcate.terramobile.server.model.dao.ProjectDAO;
import br.org.funcate.terramobile.server.model.exception.DAOException;
import br.org.funcate.terramobile.server.model.exception.DatabaseException;
import br.org.funcate.terramobile.server.model.exception.ProjectException;
import br.org.funcate.terramobile.server.model.exception.TerraMobileServerException;

public class ProjectService
{
	private ProjectService()
	{}
	
	public static void saveProject(Project project, String user) throws ProjectException, DatabaseException, DAOException, TerraMobileServerException
	{
		
		
		File projectTempFile;
		try
		{
			projectTempFile = File.createTempFile("terramobile", "gpkg");
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ProjectException("Could not create a temporary file for project.", e);
		}
		
		saveInputStream(project.getIs(), projectTempFile);
		
		project.setFile(projectTempFile);
		
		File destinationFolder = getProjectFolder(project);
		
		File destinationFile = new File(destinationFolder+"/"+project.getFileName());
		
		moveFile(project, projectTempFile, destinationFile);
		
		project.setFile(destinationFile);
	}
	
	private static void saveInputStream(InputStream is, File file2Save2) throws ProjectException  {

		if (file2Save2.exists())
		{
			file2Save2.delete();
		}		
		
		OutputStream os=null;
		try
		{
			os = new FileOutputStream(file2Save2);
		
			byte[] buffer = new byte[256];
		    int bytes = 0;
		    while ((bytes = is.read(buffer)) != -1) {
		        os.write(buffer, 0, bytes);
		    }
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			throw new ProjectException("Cannot save inputstream to file, because file is invalid.", e);
		} catch (IOException e)
		{
			e.printStackTrace();
			throw new ProjectException("Cannot save inputstream to file.", e);
		}
		finally
		{
			try
			{
				os.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
	}
	
	private static void moveFile(Project project, File file, File destinationFile) throws ProjectException  {
		
		if(destinationFile.exists())
		{
			destinationFile.delete();
		}
		file.renameTo(destinationFile);
	}
	
	
	
	private static File getProjectFolder(Project project) throws DatabaseException, DAOException, ProjectException, TerraMobileServerException
	{
		DatabaseHelper helper = new DatabaseHelper(project.getFile().getAbsolutePath());
		ProjectDAO dao = new ProjectDAO(project, helper);
		String UUID = dao.getUUID();
		int status = dao.getStatus();
		helper.close();
		
		File projectFolder = new File(GlobalVariablesSingleton.getInstance().PROJECTS_FOLDER+"/"+UUID+"/"+status);
		if(!projectFolder.exists())
		{
			projectFolder.mkdirs();
		}
		return projectFolder;
	}
}
