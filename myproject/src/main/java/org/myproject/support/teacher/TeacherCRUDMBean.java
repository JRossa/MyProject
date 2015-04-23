package org.myproject.support.teacher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.myproject.model.entities.CategoryGroup;
import org.myproject.model.entities.LogRole;
import org.myproject.model.entities.LogUser;
import org.myproject.model.entities.Teacher;
import org.myproject.model.repositories.CategoryLookupTableRepository;
import org.myproject.model.repositories.RoleRepository;
import org.myproject.model.repositories.TeacherRepository;
import org.myproject.model.repositories.UserRepository;
import org.myproject.model.utils.BaseBean;
import org.myproject.model.utils.EncodeToUTF;
import org.myproject.model.utils.EncryptHash;
import org.myproject.model.utils.MailSender;
import org.myproject.model.utils.RandomPasswordGenerator;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@Named(value = "teacherCRUDMBean")
public class TeacherCRUDMBean extends BaseBean {

    private static final long serialVersionUID = -3905598887376751619L;

    @Inject
    private TeacherRepository teacherRepository;
    
    @Inject
    private CategoryLookupTableRepository  categoryLookupTableRepository;
    
    @Inject
    private TeacherMBean mbTeacherMBean;

    private Teacher teacher;

    // To avoid - String index out of range: 0
    private String teacherPhoto = new String("images/photos/generic_profile.jpg");
    
    private String title;

    
    public TeacherCRUDMBean() {
        this.teacher = new Teacher();
        this.teacher.setCategoryGroup(new CategoryGroup());
    }

    
    public Teacher getTeacher() {
    	
//    	if(teacher == null){
//    		teacher = (Teacher)super.getInstance(Teacher.class);
//    	}

    	return teacher;
    }

    
    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    
	public String getTeacherPhoto() {
		return teacherPhoto;
	}


	public void setTeacherPhoto(String teacherPhoto) {
		this.teacherPhoto = teacherPhoto;
	}


    public void sendEMail () {
        String msg = getResourceProperty("labels", "user_email_sent");
        String academicName = this.categoryLookupTableRepository.findAcademicNameByCategory(this.teacher.getCategory());
        
        
        System.out.println("Send EMail" + this.teacher.getCategory() + "  "
        		                        + academicName + "  "
        		                        + this.teacher.getFullName());

        MailSender mail = new MailSender();
        
        String emailSubject = "Envio de dados para o corpo docente";
        String emailMsg = "Exmo Senhor <br/>"
                        + academicName + "   " + this.teacher.getFullName() + "<br/>"
                        + "<br/><br/>" 
                        + "EMail         : " + this.teacher.getEMail() + "<br/>"
                        + "<br/><br/>";
        
        mail.sendEmail("gepaq@academiamilitar.pt", 
                       "gepaq@academiamilitar.pt", emailSubject, emailMsg,
                       "gepaq@academiamilitar.pt", "chefegepaq");

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
        FacesContext.getCurrentInstance().addMessage(null, message);

    }


	public void add() {
        System.out.println("Add");
        
        System.out.println("Title  :  " + this.title);
    }

    
    public void update() {
        System.out.println("UpDate");
 

        this.title = getResourceProperty("labels", "user_update");
        System.out.println("Title  :  " + this.title);
 
        String username = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username");
       
        if (username == null) {
        	return;
        }
        
        this.teacher = mbTeacherMBean.getSelectedTeacher();
        
        if (this.teacher != null)
            if (this.teacher.getId() != null) {
                System.out.println("UpDate   " + this.teacher.getId());
                
                String photoName = this.teacher.getFullName() + ".bmp";
                
                String path = (String) FacesContext.getCurrentInstance().getExternalContext().getRealPath("/")
                		     + "/resources/images/photos/";
                
                String ctxPath = "images/photos/";
                System.out.println("Path : " + path + "   " + photoName);
                
                File photoFile = new File(path +  photoName);  
                
                if (photoFile.exists()) {
//                    EncodeToUTF encode = new EncodeToUTF();
//                    photoName = encode.toUTF(photoName);

                	this.setTeacherPhoto(ctxPath + photoName);
                } else {
                	this.setTeacherPhoto(ctxPath + "generic_profile.jpg");
                }
 
                Date date = new Date();
                // http://docs.oracle.com/javase/6/docs/api/java/text/SimpleDateFormat.html
                String strDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                System.out.println("Str Date  :  " + strDate);
 
             
            }
        
    }

    
    public void cancel() {
        this.mbTeacherMBean.unselectTeacher();
    }

    
    public void close() {
        this.mbTeacherMBean.unselectTeacher();
    }

    
    public void disableButtons()  {
        
        if (this.mbTeacherMBean.getDisableButtons()) {
            RequestContext.getCurrentInstance().execute("updateButton.disable();");
//            RequestContext.getCurrentInstance().execute("deleteButton.disable();");
            RequestContext.getCurrentInstance().execute("dataTable.unselectAllRows();");
        }
        
    }
 
    
    public void delete(boolean delete) {

        disableButtons();
        if (delete == true) {
            this.mbTeacherMBean.delete();
        } else {
            this.teacher = mbTeacherMBean.getSelectedTeacher();
        }
    }

    
    public void survey () {
        System.out.println("teacherMBean  - survey");
        
    }

    
    public void encryptName() {
        String msg = "Encrypt Name";

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    
    public String getTitle() {
        return title;
    }

    
    public void setTitle(String title) {
        this.title = title;
    }

    
    public void save() {
        String msg = "Registo Inserido";

        System.out.println("Save");

       
         
        if (this.teacher != null) {
            if (this.teacher.getId() == null) {
                // Add - TODO
                msg = msg + "   Adicionado";
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
                FacesContext.getCurrentInstance().addMessage(null, message);

            } else {
                // Update
                this.teacher = mbTeacherMBean.getSelectedTeacher();
                
                if (this.teacher != null) {
                    this.teacherRepository.save(this.teacher);
                    
                    msg = msg + "   Atualizado";
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
                    FacesContext.getCurrentInstance().addMessage(null, message);

                } else {    
                    msg = "Erro na inserção do registo";
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null);
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }    
            }
        }
        
       // System.out.printf("Log ID is %d and for returned account ID is %d\n", this.user.getId(), insertUser.getId());
        
    }

    
    public void valueChanged (ValueChangeEvent e) {
        String msg = e.getNewValue().toString();
        Long  userId =  Long.parseLong(msg);
        
       
        /* To test
        System.out.println("Novo valor :  " + e.getNewValue().toString());
        
        msg = teacher.getEMail().toString() + "  (" + userId.toString() + ")";
        
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
        */
    }
 
    
	public void handleFileUpload(FileUploadEvent event) {
		
        try {
            String path = (String) FacesContext.getCurrentInstance().getExternalContext().getRealPath("/")
       		     + "/resources/images/photos/";

            File targetFolder = new File(path);
            
            System.out.println("Name : " + event.getFile().getFileName());

            // To correct latin chars
            String targetName = new String(event.getFile().getFileName().getBytes(Charset.defaultCharset()), "UTF-8");
            System.out.println("Name : " + targetName);

            InputStream inputStream = event.getFile().getInputstream();
            OutputStream out = new FileOutputStream(new File(targetFolder, targetName));
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            
            inputStream.close();
            out.flush();
            out.close();
            
            this.setTeacherPhoto("images/photos/" + targetName);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	
    public String getResourceProperty(String resource, String label) {
        Application application = FacesContext.getCurrentInstance().getApplication();
        ResourceBundle bundle = application.getResourceBundle(FacesContext.getCurrentInstance(), resource);

        return bundle.getString(label);
    }

}
