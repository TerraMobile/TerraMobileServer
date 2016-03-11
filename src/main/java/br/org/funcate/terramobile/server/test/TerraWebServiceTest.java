package br.org.funcate.terramobile.server.test;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

public class TerraWebServiceTest {

	public static void main(String[] args) throws ClientProtocolException, IOException {
		// TODO Auto-generated method stub
		new TerraWebServiceTest().sendFile();
	}
	
	public void sendFile() throws ClientProtocolException, IOException
	{
		File fileGPKG = new File("/dados/temp/GPKG-TerraMobile-test/test.gpkg");
		
		HttpClient httpclient = new DefaultHttpClient();
		
		HttpPost httppost = new HttpPost("http://localhost:8080/TerraMobileServer/tmserver/projectservices/uploadproject");

		FileBody bin = new FileBody(fileGPKG);
		
		StringBody fileName = new StringBody(fileGPKG.getName());
		
		StringBody user = new StringBody("userName");
		
		StringBody password = new StringBody("password");

		MultipartEntity reqEntity = new MultipartEntity();
		
		reqEntity.addPart("file", bin);
		
		reqEntity.addPart("fileName", fileName);
		
		reqEntity.addPart("user", user);
		
		reqEntity.addPart("password", password);
		
		//reqEntity.addPart("comment", comment);
		
		httppost.setEntity(reqEntity);

		HttpResponse response = httpclient.execute(httppost);
		
		HttpEntity resEntity = response.getEntity();
		
		System.out.println(":"+response.getAllHeaders());
		
		System.out.println(":"+response.getStatusLine());
	}

}




