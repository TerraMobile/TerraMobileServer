package br.org.funcate.terramobile.server.rest;

/**
 * @author Funcate
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import br.org.funcate.terramobile.server.model.Project;
import br.org.funcate.terramobile.server.model.exception.DAOException;
import br.org.funcate.terramobile.server.model.exception.DatabaseException;
import br.org.funcate.terramobile.server.model.exception.ProjectException;
import br.org.funcate.terramobile.server.model.exception.TerraMobileServerException;
import br.org.funcate.terramobile.server.service.GlobalVariablesSingleton;
import br.org.funcate.terramobile.server.service.ProjectService;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("/projectservices")
public class TerraWebService {
	
	@Path("/getlistfiles/{user}")
	@GET
	@Produces("application/json")
	public Response getListOfFiles(@PathParam("user") String user, String password) throws JSONException {
		
		JSONObject jsonObject = new JSONObject();
		String result;
		
		try {
			List<JSONObject> listGeopackage = new ArrayList<JSONObject>();
			File folder = new File(GlobalVariablesSingleton.getInstance().PROJECTS_FOLDER + user);
			File[] listOfFiles = folder.listFiles();

			if(listOfFiles!=null)
			{
				for (int i = 0; i < listOfFiles.length; i++) {
					if (listOfFiles[i].isFile()) {
						JSONObject json = new JSONObject();
						json.put("pkg", listOfFiles[i].getName());
						listGeopackage.add(json);
					}
				}	
				jsonObject.put("packages", listGeopackage); //tratar caso o a collection listOfFiles estiver vazia?
			}
			else
			{
				jsonObject.put("Exception", "Invalid server projects folder.");
			}
			
			
		} catch (Exception e) {
			jsonObject.put("Exception", "Ocorreu um erro: " + e.getMessage());
		} finally {
			result = jsonObject.toString();
		}
		return Response.status(200).entity(result).build();
	}

	@GET
	@Path("/getprojects/{user}/{filename}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getProjects(@PathParam("user") String user, String password, @PathParam("filename") String fileName) throws FileNotFoundException, TerraMobileServerException {
		return download(user, fileName);
	}

	private Response download(String user, String fileName) throws FileNotFoundException, TerraMobileServerException {
		Response response = null;
		NumberFormat myFormat = NumberFormat.getInstance();
		myFormat.setGroupingUsed(true);

		//Pega arquivo
		File file = new File(GlobalVariablesSingleton.getInstance().PROJECTS_FOLDER + "/" + user + "/" + fileName);
		if (file.exists()) {
			ResponseBuilder builder = Response.ok(file);
			builder.header("Content-Disposition", "attachment; filename="
					+ file.getName());
			response = builder.build();
		} else {
			throw new FileNotFoundException("File does not exist");
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
            @FormDataParam("file") FormDataContentDisposition contentDispositionHeader) throws TerraMobileServerException
    {
		try {
			Project project = new Project(fileName, is);
		
			ProjectService.saveProject(project, user);
			
		} catch (ProjectException | DatabaseException | DAOException e) {
			e.printStackTrace();
			return Response.status(500).entity(e.getMessage()).build();
		}
		return Response.status(200).build();
	}
	
	
	


}