package br.org.funcate.terramobile.server.service;

import java.io.File;

import br.org.funcate.terramobile.server.model.exception.TerraMobileServerException;

public class GlobalVariablesSingleton
{

	//Diretório dos arquivos gpkg
	public final String TERRAMOBILE_HOME= "/opt/TerraMobileServer";
	public final String PROJECTS_FOLDER=TERRAMOBILE_HOME+"/projects";
	public final String BACKUP_FOLDER=TERRAMOBILE_HOME+"/backup";

	
	private GlobalVariablesSingleton() throws TerraMobileServerException
	{
		initializeRepository();
		validadeServerHome();
	}
	
	public static GlobalVariablesSingleton getInstance() throws TerraMobileServerException
	{
		return new GlobalVariablesSingleton();
	}

	
	private void validadeServerHome() throws TerraMobileServerException
	{
		File home = new File(this.TERRAMOBILE_HOME);
		if(!home.exists())
		{
			throw new TerraMobileServerException("GlobalVariables.TERRAMOBILE_HOME doesn't exist.");
		}
		if(!home.isDirectory())
		{
			throw new TerraMobileServerException("Invalid GlobalVariables.TERRAMOBILE_HOME variable.");
		}

		File projectFolders = new File(this.PROJECTS_FOLDER);
		if(!projectFolders.exists())
		{
			throw new TerraMobileServerException("Project folder doesn't exist.");
		}
		if(!projectFolders.isDirectory())
		{
			throw new TerraMobileServerException("Invalid project folder.");
		}
	}
	
	private void initializeRepository()
	{
		File home = new File(this.TERRAMOBILE_HOME);
		home.mkdirs();

		File projectFolders = new File(this.PROJECTS_FOLDER);
		projectFolders.mkdirs();
		
		File backupFolders = new File(this.BACKUP_FOLDER);
		backupFolders.mkdirs();
		
		
	}
}
