package org.myproject.test.conndb.repositories;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.myproject.model.entities.LogRole;
import org.myproject.model.entities.LogUser;
import org.myproject.model.entities.LogUserRequest;
import org.myproject.model.entities.Teacher;
import org.myproject.model.repositories.RoleRepository;
import org.myproject.model.repositories.TeacherRepository;
import org.myproject.model.repositories.UserRepository;
import org.myproject.model.repositories.UserRequestRepository;
import org.myproject.model.utils.EncryptHash;
import org.myproject.model.utils.PasswordHash;
import org.myproject.model.utils.RandomPasswordGenerator;
import org.myproject.test.conndb.AbstractDatabaseTest;

public class LogUserRepositoryTest extends AbstractDatabaseTest {

    @Inject
    UserRepository              userRepository;

    @Inject
    UserRequestRepository       userRequestRepository;

    @Inject
    RoleRepository              roleRepository;

    @Inject
    private TeacherRepository teacherRepository;

    private static final Logger LOGGER = Logger.getLogger(LogUserRepositoryTest.class);
    
    
    public LogUserRepositoryTest() {

    }
    
    
    public void save() {
        String msg = "Registo Inserido";
        LogUser user = new LogUser();

        LOGGER.debug("Passou");
        
        String username = "admin";
                
        LOGGER.debug("Passou");
        
        Teacher teacher = teacherRepository.findOne(2L);
        user.setTeacher(teacher);
        
        user.setUserName("teste1");
        user.setPassword("test1");
        user.setRndPassword(RandomPasswordGenerator.generatePswd(30));
        
        LogRole role = roleRepository.findOne(2L);
        user.setLogRole(role);
        
        Date date = new Date();
        // http://docs.oracle.com/javase/6/docs/api/java/text/SimpleDateFormat.html
        String strDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        System.out.println("Str Date  :  " + strDate);
        
        user.setFirstName("");
        user.setLastName("");
        user.setCreationUser(username);
        user.setCreationDate(new Date());
        
        user.setModificationUser(username);
        user.setModificationDate(new Date());

        user.setLastLoginDate(new Date());

        System.out.println("Save");

        System.out.println("Role       : " + user.getLogRole().getRolename());
        System.out.println("User Name  : " + user.getUserName());
        System.out.println("Password   : " + user.getPassword());
        
        LogUser retUser = userRepository.save(user);
        
        System.out.printf("Log ID is %d and for returned account ID is %d\n", user.getId(), retUser.getId());

    }

    public void generate() throws NoSuchAlgorithmException, InvalidKeySpecException {
        System.out.println("Generate");
        
        LogUser user = new LogUser();
        List <Teacher> teachers = this.teacherRepository.findByExecutionYear("2014/2015");
        String username = "username";
        LogRole role = new LogRole();
        role = roleRepository.findOne(2L);
       
        for (Teacher teacher: teachers) {
            System.out.println("Generate teacher");
            
            
            String bigName = teacher.getFullName();
            String[] names = bigName.split(" ");
            System.out.println(String.format("FirstName: %s : LastName: %s", names[0], names[names.length-1])); 

           // LOGGER.debug(teacher);
            String userNumber = String.format("%04d", teacher.getId());
            System.out.println("Passou  1    " + teacher.getId());

            user.setTeacher(teacher);
            
            user.setFirstName(names[0]);
            user.setLastName(names[names.length-1]);
            
            
            user.setUserName("utilizador" + userNumber);
            user.setRndPassword(RandomPasswordGenerator.generatePswd(30));

            // Antigo
            // user.setPassword(Hash.md5(user.getRndPassword()));
            
            // Novo
            user.setPassword(PasswordHash.createHash(user.getRndPassword()));

            System.out.println("Passou 2");
            
            System.out.println("Str Role Id  :  " + role.getId());
            user.setLogRole(role);

            Date date = new Date();
            // http://docs.oracle.com/javase/6/docs/api/java/text/SimpleDateFormat.html
            String strDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
            System.out.println("Str Date  :  " + strDate);
            
            user.setCreationUser(username);
            user.setCreationDate(new Date());
            
            user.setModificationUser(username);
            user.setModificationDate(new Date());

            user.setLastLoginDate(new Date());
            
            LOGGER.debug(user);
            
            break;
            
        }
    }

    
    @Test
    public void testUserRequestbyTag() {
    	
    	LogUserRequest user = new LogUserRequest();
    	System.out.println("User :  " );
    	    	
    	user = this.userRequestRepository.findByUserRequestTag("j5Xv77H+X8");
   	
    	System.out.println("User :  " + user.getUserId().getUserName());
    	
    }
}
