package br.org.funcate.terramobile.server.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.org.funcate.terramobile.server.model.DatabaseHelper;
import br.org.funcate.terramobile.server.model.Project;
import br.org.funcate.terramobile.server.model.dao.ProjectDAO;
import br.org.funcate.terramobile.server.model.exception.DAOException;
import br.org.funcate.terramobile.server.model.exception.DatabaseException;
import br.org.funcate.terramobile.server.model.exception.ProjectException;
import br.org.funcate.terramobile.server.model.exception.TerraMobileServerException;

public class ProjectService
{

	private static final Logger logger= Logger.getLogger( ProjectService.class.getName() );
	
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

	
	/**
	 * Iterate over project folders looking for project with the project status. File are stored on PROJECTS_FOLDER/PROJECT_ID/PROJECT_STATUS
	 * @param projectStatus
	 * @return
	 * @throws TerraMobileServerException
	 * @throws ProjectException 
	 * @throws DatabaseException 
	 * @throws DAOException 
	 */
	public static List<Project> getProjectList(int projectStatus) throws TerraMobileServerException, ProjectException, DatabaseException, DAOException 
	{
		List<Project> projects = new ArrayList<Project>();
		
		File appProjectFolder = new File(GlobalVariablesSingleton.getInstance().PROJECTS_FOLDER);
		
		File[] folders = appProjectFolder.listFiles();
		
		for (File projectFolder : folders)
		{
			File statusFolder = new File(projectFolder.getAbsolutePath()+"/"+projectStatus);
			
 
			if(!statusFolder.exists())
			{
				continue;
			}
			
			File[] gpkgFiles = statusFolder.listFiles();
			
			for (File projectGpkg : gpkgFiles)
			{
				Project project;
				try
				{
					project = new Project(projectGpkg.getName(), new FileInputStream(projectGpkg));
					project.setFile(new File(projectGpkg.getAbsolutePath()));
					
				} catch (FileNotFoundException e)
				{
					throw new ProjectException("Invalid project file: " + projectGpkg.getName(), e);
				}
				DatabaseHelper helper = new DatabaseHelper(projectGpkg.getAbsolutePath());
				
				ProjectDAO dao = new ProjectDAO(project, helper);
				
				dao.getUUID();
				
				dao.getStatus();
				
				dao.getDescription();

				if(project.getDescription()==null)
				{
					project.setDescription("");
				}
			
				helper.close();
				
				if(project.getStatus()!=projectStatus)
				{
					logger.log(Level.WARNING, "File in the wrong folder, file: " + projectGpkg.getAbsolutePath() + ". Project Status: " + projectStatus + ". Status Folder: " + project.getStatus());
					continue;
				}
				
				projects.add(project);
				
			}
			
			
		}
		
		return projects;
		
	}
	
	public static Project getProject(Project project) throws TerraMobileServerException, ProjectException
	{
		File projectFile = new File(GlobalVariablesSingleton.getInstance().PROJECTS_FOLDER+"/"+project.getUUID()+"/"+project.getStatus()+"/"+project.getFileName());
		
		if(!projectFile.exists())
		{
			throw new ProjectException("Unable to find requested project, UUID: " + project.getUUID() + ", Status: " + project.getStatus() + ", Name: " + project.getFileName() + ", Absolute Path: " + projectFile.getAbsolutePath());
		}
		
		project.setFile(projectFile);
	
		return project;
	}
}
