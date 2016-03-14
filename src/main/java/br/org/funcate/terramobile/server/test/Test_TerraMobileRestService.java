package br.org.funcate.terramobile.server.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

public class Test_TerraMobileRestService
{

	@Test
	public void test_uploadNewProject() throws URISyntaxException
	{

		File fileGPKG = new File(Test_TerraMobileRestService.class.getResource(
				"/newProject.gpkg").toURI());
		String url = "http://localhost:8080/TerraMobileServer/tmserver/projectservices/uploadproject";

		HttpResponse response = null;

		try
		{

			HashMap<String, String> parameters = new HashMap<String, String>();

			parameters.put("fileName", fileGPKG.getName());

			parameters.put("user", "userName");

			parameters.put("password", "password");

			response = sendPost(fileGPKG, url, parameters.entrySet());
			
		} catch (ClientProtocolException e)
		{
			fail(e.getMessage());
		} catch (IOException e)
		{
			fail(e.getMessage());
		}

		assertFalse(response == null);

		assertTrue(response.getStatusLine().getStatusCode() == 200);

	}

	@Test
	public void test_uploadGatheredProject() throws URISyntaxException
	{

		File fileGPKG = new File(Test_TerraMobileRestService.class.getResource(
				"/gatheredProject.gpkg").toURI());
		String url = "http://localhost:8080/TerraMobileServer/tmserver/projectservices/uploadproject";

		HttpResponse response = null;

		try
		{
			HashMap<String, String> parameters = new HashMap<String, String>();

			parameters.put("fileName", fileGPKG.getName());

			parameters.put("user", "userName");

			parameters.put("password", "password");

			response = sendPost(fileGPKG, url, parameters.entrySet());
		} catch (ClientProtocolException e)
		{
			fail(e.getMessage());
		} catch (IOException e)
		{
			fail(e.getMessage());
		}

		assertFalse(response == null);

		assertTrue(response.getStatusLine().getStatusCode() == 200);
	}

	@Test
	public void test_listNewProjects() throws URISyntaxException
	{

		String url = "http://localhost:8080/TerraMobileServer/tmserver/projectservices/listprojects";

		HttpResponse response = null;

		try
		{
			HashMap<String, String> parameters = new HashMap<String, String>();

			parameters.put("user", "userName");

			parameters.put("password", "password");

			parameters.put("project_status", "0");

			response = sendPost(null, url, parameters.entrySet());

		} catch (ClientProtocolException e)
		{
			fail(e.getMessage());
		} catch (IOException e)
		{
			fail(e.getMessage());
		}
		
		String expectedJSON = "{\"projects\":[{\"project_name\":\"newProject.gpkg\",\"project_status\":0,\"project_id\":\"10201203129123\",\"project_description\":\"\"}]}";

		assertFalse(response == null);

		assertTrue(response.getStatusLine().getStatusCode() == 200);
		
		String content=null;
		try
		{
			content = getStringFromInputStream(response.getEntity().getContent());
		} catch (UnsupportedOperationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		System.out.println(content);
		
		assertTrue(content==expectedJSON);

	}

	public HttpResponse sendPost(File file, String url,
			Collection<Entry<String, String>> parameters)
			throws ClientProtocolException, IOException
	{

		HttpClient httpclient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost(url);

		MultipartEntity reqEntity = new MultipartEntity();
		
		if(file!=null)
		{
			FileBody bin = new FileBody(file);
			
			reqEntity.addPart("file", bin);
		}
		
		
		if (parameters != null)
		{
			for (Entry<String, String> parameter : parameters)
			{
				StringBody parameterBody = new StringBody(parameter.getValue());

				reqEntity.addPart(parameter.getKey(), parameterBody);
			}
		}

		httppost.setEntity(reqEntity);

		HttpResponse response = httpclient.execute(httppost);

		/*
		 * HttpEntity resEntity = response.getEntity();
		 * 
		 * System.out.println(":"+response.getAllHeaders());
		 * 
		 * System.out.println(":"+response.getStatusLine());
		 */

		return response;
	}
	
	private String getStringFromInputStream(InputStream inputStream) throws IOException
	{
		String output="";
	
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream)); 

		while(br.ready())
		{ 
			output += br.readLine(); 
		    //line has the contents returned by the inputStream 
		}
		
	
		return output;
	}

}
