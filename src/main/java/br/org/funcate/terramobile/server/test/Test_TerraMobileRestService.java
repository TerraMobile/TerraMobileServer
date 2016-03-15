package br.org.funcate.terramobile.server.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

public class Test_TerraMobileRestService
{
	
	private static final String serviceURL = "http://localhost:8080/TerraMobileServer/projectservices/";

	@Test
	public void test_uploadNewProject() throws URISyntaxException
	{

		File fileGPKG = new File(Test_TerraMobileRestService.class.getResource(
				"/newProject.gpkg").toURI());
		String url = serviceURL+"uploadproject";

		HttpEntity entity = null;

		try
		{

			HashMap<String, String> parameters = new HashMap<String, String>();

			parameters.put("fileName", fileGPKG.getName());

			parameters.put("user", "userName");

			parameters.put("password", "password");

			entity = sendPost(fileGPKG, url, parameters.entrySet());
			
		} catch (ClientProtocolException e)
		{
			fail(e.getMessage());
		} catch (IOException e)
		{
			fail(e.getMessage());
		}

		assertFalse(entity == null);

	}

	@Test
	public void test_uploadGatheredProject() throws URISyntaxException
	{

		File fileGPKG = new File(Test_TerraMobileRestService.class.getResource(
				"/gatheredProject.gpkg").toURI());
		String url = serviceURL+"uploadproject";

		HttpEntity entity = null;

		try
		{
			HashMap<String, String> parameters = new HashMap<String, String>();

			parameters.put("fileName", fileGPKG.getName());

			parameters.put("user", "userName");

			parameters.put("password", "password");

			entity = sendPost(fileGPKG, url, parameters.entrySet());
		} catch (ClientProtocolException e)
		{
			fail(e.getMessage());
		} catch (IOException e)
		{
			fail(e.getMessage());
		}

		assertFalse(entity == null);

	}

	@Test
	public void test_listNewProjects() throws URISyntaxException
	{

		String url = serviceURL+"listprojects";
		HttpEntity entity = null;

		try
		{
			HashMap<String, String> parameters = new HashMap<String, String>();

			parameters.put("user", "userName");

			parameters.put("password", "password");

			parameters.put("projectStatus", "0");

			entity = sendPost(null, url, parameters.entrySet());

		} catch (ClientProtocolException e)
		{
			fail(e.getMessage());
		} catch (IOException e)
		{
			fail(e.getMessage());
		}
		
		

		assertFalse(entity == null);

	}
	
	@Test
	public void test_listGatheredProjects() throws URISyntaxException
	{

		String url = serviceURL+"listprojects";

		HttpEntity entity = null;

		try
		{
			HashMap<String, String> parameters = new HashMap<String, String>();

			parameters.put("user", "userName");

			parameters.put("password", "password");

			parameters.put("projectStatus", "1");

			entity = sendPost(null, url, parameters.entrySet());

		} catch (ClientProtocolException e)
		{
			fail(e.getMessage());
		} catch (IOException e)
		{
			fail(e.getMessage());
		}
		
		assertFalse(entity == null);
		
	}
	
	public HttpEntity sendPost(File file, String url,
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
		
		assertTrue(response.getStatusLine().getStatusCode() == 200);
		
		HttpEntity entity = response.getEntity();
		
		ContentType contentType = ContentType.getOrDefault(entity);

		return entity;
	}
	
	private String getStringFromInputStream(InputStream inputStream, String encoding) throws IOException
	{
		
		String body = IOUtils.toString(inputStream, encoding);
		
		return body.trim();
	}
	
	@Test
	public void test_dowloadNewProject() throws URISyntaxException
	{

		String url = serviceURL+"downloadproject";

		HttpEntity entity = null;

		try
		{
			HashMap<String, String> parameters = new HashMap<String, String>();

			parameters.put("user", "userName");

			parameters.put("password", "password");

			parameters.put("projectStatus", "0");
			
			parameters.put("projectName", "newProject.gpkg");
			
			parameters.put("projectId", "10201203129123");

			entity = sendPost(null, url, parameters.entrySet());
			
			assertFalse(entity == null);
			
			assertFalse(entity.getContent()==null);

		} catch (ClientProtocolException e)
		{
			fail(e.getMessage());
		} catch (IOException e)
		{
			fail(e.getMessage());
		}
	
	}
	
	@Test
	public void test_dowloadGatheredProject() throws URISyntaxException
	{

		String url = serviceURL+"downloadproject";

		HttpEntity entity = null;

		try
		{
			HashMap<String, String> parameters = new HashMap<String, String>();

			parameters.put("user", "userName");

			parameters.put("password", "password");

			parameters.put("projectStatus", "1");
			
			parameters.put("projectName", "gatheredProject.gpkg");
			
			parameters.put("projectId", "10201203129123");

			entity = sendPost(null, url, parameters.entrySet());
			
			assertFalse(entity == null);
			
			assertFalse(entity.getContent()==null);

		} catch (ClientProtocolException e)
		{
			fail(e.getMessage());
		} catch (IOException e)
		{
			fail(e.getMessage());
		}
	
	}

}
