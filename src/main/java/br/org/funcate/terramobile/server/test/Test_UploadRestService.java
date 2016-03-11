package br.org.funcate.terramobile.server.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

public class Test_UploadRestService {
	
	@Test
	public void test_uploadNewProject() throws URISyntaxException {
		
		File fileGPKG = new File(Test_UploadRestService.class.getResource("/newProject.gpkg").toURI());
		String url = "http://localhost:8080/TerraMobileServer/tmserver/projectservices/uploadproject";
		
		HttpResponse response= null;
		
		try {
			response=sendPost(fileGPKG, url);
		} catch (ClientProtocolException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
		assertFalse(response==null);
		
		assertTrue(response.getStatusLine().getStatusCode()==204);
		
	}
	
	@Test
	public void test_uploadGatheredProject() throws URISyntaxException {

		File fileGPKG = new File(Test_UploadRestService.class.getResource("/gatheredProject.gpkg").toURI());
		String url = "http://localhost:8080/TerraMobileServer/tmserver/projectservices/uploadproject";
		
		HttpResponse response= null;
		
		try {
			response=sendPost(fileGPKG, url);
		} catch (ClientProtocolException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
		assertFalse(response==null);
		
		assertTrue(response.getStatusLine().getStatusCode()==204);
	}
	
	public HttpResponse sendPost(File file, String url) throws ClientProtocolException, IOException
	{
		
		
		HttpClient httpclient = new DefaultHttpClient();
		
		HttpPost httppost = new HttpPost(url);

		FileBody bin = new FileBody(file);
		
		StringBody fileName = new StringBody(file.getName());
		
		StringBody user = new StringBody("userName");
		
		StringBody password = new StringBody("password");

		MultipartEntity reqEntity = new MultipartEntity();
		
		reqEntity.addPart("file", bin);
		
		reqEntity.addPart("fileName", fileName);
		
		reqEntity.addPart("user", user);
		
		reqEntity.addPart("password", password);
		
		httppost.setEntity(reqEntity);

		HttpResponse response = httpclient.execute(httppost);
		
/*		HttpEntity resEntity = response.getEntity();
		
		System.out.println(":"+response.getAllHeaders());
		
		System.out.println(":"+response.getStatusLine());*/
		
		return response;
	}

}
