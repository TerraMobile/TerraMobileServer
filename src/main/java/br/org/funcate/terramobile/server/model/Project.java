package br.org.funcate.terramobile.server.model;

import java.io.File;
import java.io.InputStream;

public class Project
{
	private String fileName;
	private InputStream is;
	private File file;
	private String UUID;
	private int status;
	
	public Project(String fileName, InputStream is) {

		super();
		this.fileName = fileName;
		this.is = is;
	}
	public String getFileName()
	{
	
		return fileName;
	}
	public void setFileName(String fileName)
	{
	
		this.fileName = fileName;
	}
	public InputStream getIs()
	{
	
		return is;
	}
	public void setIs(InputStream is)
	{
	
		this.is = is;
	}
	public File getFile()
	{
	
		return file;
	}
	public void setFile(File file)
	{
	
		this.file = file;
	}
	public String getUUID()
	{
	
		return UUID;
	}
	public void setUUID(String uUID)
	{
	
		UUID = uUID;
	}
	public int getStatus()
	{
	
		return status;
	}
	public void setStatus(int status)
	{
	
		this.status = status;
	}
	
	
}
