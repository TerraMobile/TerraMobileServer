package br.org.funcate;

/**
 * @author Funcate
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.ResponseBuilder;

@Path("/projectservices")
public class TerraWebService {

	//Diretório dos arquivos gpkg
	private static final String FILE_FOLDER = "/home/terrabrasilis/terramobile/";

	@Path("/getlistfiles/{user}")
	@GET
	@Produces("application/json")
	public Response getListOfFiles(@PathParam("user") String user, String password) throws JSONException {
		
		JSONObject jsonObject = new JSONObject();
		String result;
		
		try {
			List<JSONObject> listGeopackage = new ArrayList<JSONObject>();
			File folder = new File(FILE_FOLDER + user);
			File[] listOfFiles = folder.listFiles();

			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					JSONObject json = new JSONObject();
					json.put("pkg", listOfFiles[i].getName());
					listGeopackage.add(json);
				}
			}	
			jsonObject.put("packages", listGeopackage); //tratar caso o a collection listOfFiles estiver vazia?
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
	public Response getProjects(@PathParam("user") String user, String password, @PathParam("filename") String fileName) throws FileNotFoundException {
		return download(user, fileName);
	}

	private Response download(String user, String fileName) throws FileNotFoundException {
		Response response = null;
		NumberFormat myFormat = NumberFormat.getInstance();
		myFormat.setGroupingUsed(true);

		//Pega arquivo
		File file = new File(FILE_FOLDER + "/" + user + "/" + fileName);
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
	
	
	//Método ainda não finalizado
	@POST
	@Path("/setprojects/{user}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response setProjects(@PathParam("user") String user, String password, @PathParam("filename") String fileName) {	
		try {
			saveFile(user);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private void saveFile(/*InputStream is,*/ String fileLocation) throws IOException {
		File file = new File(FILE_FOLDER + fileLocation);
		
		if (!file.exists()) file.mkdir();
		
		OutputStream os = new FileOutputStream(file);
		byte[] buffer = new byte[256];
	    int bytes = 0;
	    /*while ((bytes = is.read(buffer)) != -1) {
	        os.write(buffer, 0, bytes);
	    }*/
	}

}