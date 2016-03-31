package br.org.funcate.terramobile.server.admin;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
    







import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.org.funcate.terramobile.server.model.Project;
import br.org.funcate.terramobile.server.model.exception.DAOException;
import br.org.funcate.terramobile.server.model.exception.DatabaseException;
import br.org.funcate.terramobile.server.model.exception.ProjectException;
import br.org.funcate.terramobile.server.model.exception.TerraMobileServerException;
import br.org.funcate.terramobile.server.service.GlobalVariablesSingleton;
import br.org.funcate.terramobile.server.service.ProjectService;
    
@ManagedBean
@SessionScoped
public class AdminManagedBean implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @PostConstruct
    public void init() {
 
    }
    
    public List<Project> getList() throws TerraMobileServerException, ProjectException, DatabaseException, DAOException {
    	
    	List<Project> projects = ProjectService.getProjectList(0);
    	projects.addAll(ProjectService.getProjectList(1));
    	
    	return projects;
    	
    }
    
    public void delete(Project project) throws IOException, TerraMobileServerException {
    	File file = project.getFile();
    	if(file!=null)
    	{
    		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy_HH-mm");
    		String dateDirStr = GlobalVariablesSingleton.getInstance().BACKUP_FOLDER+"/"+format.format(new Date());    		
    		System.out.println(dateDirStr);
    		File dateDir = new File(dateDirStr);
    		if(!dateDir.exists())
    		{
    			dateDir.mkdirs();
    		}
    		file.renameTo(new File(dateDirStr+"/"+file.getName()));
    		//file.delete();
    		
    		System.out.println("DELETED: " + dateDirStr+"/"+file.getName());
    	}
    }
    

    
}