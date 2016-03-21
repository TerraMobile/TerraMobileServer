package br.org.funcate.terramobile.server.rest;

/**
 * @author Funcate
 */

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.json.JSONObject;

import br.org.funcate.terramobile.server.model.Project;
import br.org.funcate.terramobile.server.model.exception.DAOException;
import br.org.funcate.terramobile.server.model.exception.DatabaseException;
import br.org.funcate.terramobile.server.model.exception.ProjectException;
import br.org.funcate.terramobile.server.model.exception.TerraMobileServerException;
import br.org.funcate.terramobile.server.service.ProjectService;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("/projectservices")
public class TerraMobileService {
	
	@Path("/listprojects")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	public Response listProjects(@FormDataParam("user") String user, @FormDataParam("password") String password, @FormDataParam("projectStatus") int projectStatus) {
	
		JSONObject jsonObject = new JSONObject();
		
		List<JSONObject> listGeopackage = new ArrayList<JSONObject>();

		String result="";
		
		try
		{
			List<Project> projects = ProjectService.getProjectList(projectStatus);
			
			for (Project project : projects)
			{
				
				if (project.getFile().exists())
				{
					JSONObject json = new JSONObject();
					json.put("project_id", project.getUUID());
					json.put("project_status", project.getStatus());
					json.put("project_name", project.getFileName());
					json.put("project_description", project.getDescription());
					listGeopackage.add(json);
				}
					
				jsonObject.put("projects", listGeopackage); //tratar caso o a collection listOfFiles estiver vazia?
				
			}
			
		} catch (TerraMobileServerException | ProjectException
				| DatabaseException | DAOException e)
		{
			e.printStackTrace();
			return Response.status(500).entity(e.getMessage()).build();
		}
			
		result = jsonObject.toString();
		
		return Response.status(200).entity(result).build();
	}

	@POST
	@Path("/downloadproject")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadProject(@FormDataParam("user") String user, 
			@FormDataParam("password") String password, 
			@FormDataParam("projectId") String projectId,
			@FormDataParam("projectStatus") int projectStatus,
			@FormDataParam("projectName") String projectName) {

		
		Response response=null;
		try
		{
		
			Project project = new Project(projectName, null);
			
			project.setStatus(projectStatus);
			
			project.setUUID(projectId);
			
			project = ProjectService.getProject(project);
			
			ResponseBuilder builder = Response.status(200).entity(project.getFile());
			
			builder.header("Content-Disposition", "attachment; filename="
					+ project.getFile().getName());
			
			response = builder.build();
		
		} catch (ProjectException | TerraMobileServerException e)
		{
			e.printStackTrace();
			return Response.status(500).entity(e.getMessage()).build();
		}
		
		return response;		
	}

	

	
	
	
	@POST
	@Path("/uploadproject")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadProject(@FormDataParam("user") String user, 
			@FormDataParam("password") String password, 
			@FormDataParam("fileName") String fileName,
			@FormDataParam("file") InputStream is,
            @FormDataParam("file") FormDataContentDisposition contentDispositionHeader) 
    {
		try {
			if(fileName==null || fileName=="" || fileName=="null")
			{
				return Response.status(500).entity("Missing or empty 'fileName' parameter.").build();
			}
						
			Project project = new Project(fileName, is);
		
			ProjectService.saveProject(project, user);
			
		} catch (ProjectException | DatabaseException | DAOException | TerraMobileServerException e) {
			e.printStackTrace();
			return Response.status(500).entity(e.getMessage()).build();
		}
		return Response.status(200).build();
	}
	
	
	


}