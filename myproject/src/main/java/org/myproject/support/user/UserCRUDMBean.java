package org.myproject.support.user;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.myproject.model.entities.LogRole;
import org.myproject.model.entities.LogUser;
import org.myproject.model.entities.Teacher;
import org.myproject.model.repositories.CategoryLookupTableRepository;
import org.myproject.model.repositories.RoleRepository;
import org.myproject.model.repositories.TeacherRepository;
import org.myproject.model.repositories.UserRepository;
import org.myproject.model.utils.BaseBean;
import org.myproject.model.utils.EncryptHash;
import org.myproject.model.utils.MailSender;
import org.myproject.model.utils.PasswordHash;
import org.myproject.model.utils.RandomPasswordGenerator;
import org.primefaces.context.RequestContext;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@Named(value = "userCRUDMBean")
public class UserCRUDMBean extends BaseBean {

    private static final long serialVersionUID = -3905598887376751619L;

    @Inject
    private UserRepository userRepository;

    @Inject
    private TeacherRepository teacherRepository;
    
    @Inject
    private RoleRepository roleRepository;
    
    @Inject
    private UserMBean mbUserBean;

    @Inject
    private CategoryLookupTableRepository  categoryLookupTableRepository;

    private LogUser user;

    private String title;


    
    public UserCRUDMBean() {
        this.user = new LogUser();
    }

    public LogUser getUser() {
        return user;
    }

    public void setUser(LogUser user) {
        this.user = user;
    }

    public void change() {
        System.out.println("Change");
        
    }
 
    public void onLoadSelectedUser () {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext(); 
        String username = (String) context.getSessionMap().get("username");
        
        // System.out.println("check Log User  :  " + username);
        
        this.user = this.userRepository.findByUserName(username);
    }
    
    
    public void add() throws NoSuchAlgorithmException, InvalidKeySpecException {
        System.out.println("Add");
        
        this.title = getResourceProperty("labels", "user_create");
       
        String username = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username");

        Teacher teacher = new Teacher();
        
        this.user.setTeacher(teacher);

        this.user.setUserName("");
        // Antigo
        // this.user.setPassword(Hash.md5("teste1"));
        
        // Novo
        this.user.setPassword(PasswordHash.createHash("teste1"));
        
        this.user.setRndPassword(RandomPasswordGenerator.generatePswd(30));
        
        // Antigo
        // this.user.setPassword(Hash.md5(this.user.getRndPassword()));
       
        // Novo
        // this.user.setPassword(PasswordHash.createHash(this.user.getRndPassword()));
        
        LogRole role = new LogRole();
        role = roleRepository.findOne(2L);
        System.out.println("Str Role Id  :  " + role.getId());
        this.user.setLogRole(role);

        this.user.setEnabled(true);
        
        Date date = new Date();
        // http://docs.oracle.com/javase/6/docs/api/java/text/SimpleDateFormat.html
        String strDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        System.out.println("Str Date  :  " + strDate);
        
        this.user.setCreationUser(username);
        this.user.setCreationDate(new Date());
        
        this.user.setModificationUser(username);
        this.user.setModificationDate(new Date());

        this.user.setLastLoginDate(new Date());
        
        System.out.println("Title  :  " + this.title);
        
    }

    
    public void sendEMail () {
        String msg = getResourceProperty("labels", "user_email_sent");
       
        System.out.println("Send EMail");
        String academicName = this.categoryLookupTableRepository.findAcademicNameByCategory(this.user.getTeacher().getCategory());
        
        
        System.out.println("Send EMail" + this.user.getTeacher().getCategory() + "  "
        		                        + academicName + "  "
        		                        + this.user.getTeacher().getFullName());

        
        MailSender mail = new MailSender();
        
        String emailSubject = "Envio de dados de utilizador";
        String emailMsg = "Exmo Senhor <br/>"
                        + academicName + "   " + this.user.getTeacher().getFullName() + "<br/>"
                        + "<br/><br/>" 
                        + "EMail         : " + this.getUser().getTeacher().getEMail() + "<br/>"
                        + "<br/><br/>" 
                        + "Utilizador    : " + this.getUser().getUserName() + "<br/>"
                        + "Palavra Passe : " + this.getUser().getRndPassword() + "<br/>"
                        + "<br/><br/>";
        
        mail.sendEmail("gepaq@academiamilitar.pt", 
                       "gepaq@academiamilitar.pt", emailSubject, emailMsg,
                       "gepaq@academiamilitar.pt", "chefegepaq");

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
        FacesContext.getCurrentInstance().addMessage(null, message);

    }
    

    public void generate() throws NoSuchAlgorithmException, InvalidKeySpecException {
        System.out.println("Generate");
        
        List <Teacher> teachers = this.teacherRepository.findByExecutionYear("2014/2015");
        String username = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username");
        LogRole role = new LogRole();
        role = roleRepository.findOne(2L);
       
        for (Teacher teacher: teachers) {
            String bigName = teacher.getFullName();
            String[] names = bigName.split(" ");
           
            // System.out.println(String.format("FirstName: %s : LastName: %s", names[0], names[names.length-1])); 
            
            String userNumber = String.format("%04d", teacher.getId());
            
            this.user.setTeacher(teacher);
            
            user.setFirstName(names[0]);
            user.setLastName(names[names.length-1]);
            
            this.user.setUserName("utilizador" + userNumber);
            this.user.setRndPassword(RandomPasswordGenerator.generatePswd(30));

            // Antigo
            // this.user.setPassword(Hash.md5(this.user.getRndPassword()));
            
            // Novo
            this.user.setPassword(PasswordHash.createHash(this.user.getRndPassword()));
            
            System.out.println("Str Role Id  :  " + role.getId());
            this.user.setLogRole(role);

            this.user.setEnabled(true);
            
            Date date = new Date();
            // http://docs.oracle.com/javase/6/docs/api/java/text/SimpleDateFormat.html
            String strDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
            System.out.println("Str Date  :  " + strDate);
            
            this.user.setCreationUser(username);
            this.user.setCreationDate(new Date());
            
            this.user.setModificationUser(username);
            this.user.setModificationDate(new Date());

            this.user.setLastLoginDate(new Date());
            
            this.userRepository.save(this.user);

            System.out.println("Passou break");
            
            // TODO - Retirar no final
            break;
            
        }
    }
    

    public void update() {
        System.out.println("UpDate");
 
        this.title = getResourceProperty("labels", "user_update");
        System.out.println("Title  :  " + this.title);
 
        String username = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username");
        
        this.user = mbUserBean.getSelectedUser();
        
        if (this.user != null)
            if (this.user.getId() != null) {
                System.out.println("UpDate   " + this.user.getId());
                
                Date date = new Date();
                // http://docs.oracle.com/javase/6/docs/api/java/text/SimpleDateFormat.html
                String strDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                System.out.println("Str Date  :  " + strDate);
 
                this.user.setModificationUser(username);
                this.user.setModificationDate(new Date());
                
            }
        
    }

    
    public void cancel() {
        this.mbUserBean.unselectUser();
    }

    
    public void close() {
        this.mbUserBean.unselectUser();
    }
    
    
    public void changePassword() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String msg =  getResourceProperty("labels", "user_password_changed");
       
 //       this.mbUserBean.changePassword();
        
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    
    public void disableButtons()  {
        
        if (this.mbUserBean.getDisableButtons()) {
            RequestContext.getCurrentInstance().execute("updateButton.disable();");
            RequestContext.getCurrentInstance().execute("deleteButton.disable();");
            RequestContext.getCurrentInstance().execute("dataTable.unselectAllRows();");
        }
        
    }
    
    public void delete(boolean delete) {

        disableButtons();
        if (delete == true) {
            this.mbUserBean.delete();
        } else {
            this.user = mbUserBean.getSelectedUser();
        }
    }
    
    public void userCreate () {
        String msg = "User Create";

//     <ui:composition>
//        
//    <ui:include name="change" src="changePasswordT.xhtml"  />
//    <ui:include name="create" src="createUserT.xhtml"  />
//        
//    <h:form  >  
//        <p:menubar>
//        
//        <p:submenu label="#{labels.menu_user}" icon="ui-icon-document" >
//        <p:menuitem value="#{labels.menu_user_create}" 
//                    actionListener="#{userCRUDMBean.add}"
//                    update=":userCreateDlg" 
//                    action="#{userCRUDMBean.userCreate}" />
        
        System.out.println(msg);
        
        RequestContext.getCurrentInstance().execute("PF('userCreateDialog').show()");
    }
    
    
    public void generateRndPassword() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String msg = getResourceProperty("labels", "user_rndpassword_generated");

        this.user.setRndPassword(RandomPasswordGenerator.generatePswd(30));
//        this.user.setPassword(EncryptHash.md5(this.user.getRndPassword()));
        this.user.setPassword(PasswordHash.createHash(this.user.getRndPassword()));
        
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void encryptName() {
        String msg = getResourceProperty("labels", "user_encrypt_name");

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void save() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String msg = getResourceProperty("labels", "user_register_inserted") + "  ";

        System.out.println("Save");

        if (this.user.getId() != null)
            System.out.println("Id       : " + this.user.getId());
        
        System.out.println("Role       : " + this.user.getLogRole().getRolename());
        System.out.println("User Name  : " + this.user.getUserName());
        System.out.println("Password   : " + this.user.getPassword());

        if (this.user != null) {
            
            if (this.user.getRndPassword() != null &&
                    this.user.getRndPassword().length() > 0) {
                this.user.setPassword(PasswordHash.createHash(this.user.getRndPassword()));
                this.user.setRndPassword(""); 
            }

            if (this.user.getId() == null) {
                // Add
                LogUser retUser = this.userRepository.save(this.user);
                System.out.printf("Log ID is %d and for returned account ID is %d\n", user.getId(), retUser.getId());

                msg = msg + "   " + getResourceProperty("labels", "user_register_added");
                System.out.printf(msg);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
                FacesContext.getCurrentInstance().addMessage(null, message);

            } else {
                // Update
                // ATENÇÃO: updateUser uses userCRUDMBean.user.   because userMBean.user.logRole.id doesn't work
                // this.user = this.mbUserBean.getSelectedUser();
                
                if (this.user != null) {
                    this.userRepository.save(this.user);
                    
                    msg = msg + "   " + getResourceProperty("labels", "user_register_updated");
                    System.out.printf(msg);
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
                    FacesContext.getCurrentInstance().addMessage(null, message);

                } else {    
                    msg = "Erro na inserção do registo";
                    System.out.printf(msg);
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null);
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }    
            }
        }

        RequestContext.getCurrentInstance().update("userListForm:userDataTable");
        
        
       // System.out.printf("Log ID is %d and for returned account ID is %d\n", this.user.getId(), insertUser.getId());
        
    }

    
    public void valueChanged (ValueChangeEvent e) {
        String msg = e.getNewValue().toString();
        Long  userId =  Long.parseLong(msg);
  
        Teacher teacher = teacherRepository.findOne(userId);
        this.user.setTeacher(teacher);

        String bigName = teacher.getFullName();
        String[] names = bigName.split(" ");

//        System.out.println("Passou Value Changed" + userId);
        this.user.setFirstName(names[0]);
        this.user.setLastName(names[names.length-1]);

        /* To test
        System.out.println("Novo valor :  " + e.getNewValue().toString());
        
        msg = teacher.getEMail().toString() + "  (" + userId.toString() + ")";
        
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
        */
    }
    
    
    public String getResourceProperty(String resource, String label) {
        Application application = FacesContext.getCurrentInstance().getApplication();
        ResourceBundle bundle = application.getResourceBundle(FacesContext.getCurrentInstance(), resource);

        return bundle.getString(label);
    }

}
