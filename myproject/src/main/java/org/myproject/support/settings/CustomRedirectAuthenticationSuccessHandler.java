package org.myproject.support.settings;

import java.io.IOException;
import java.util.Date;
import java.util.Set;
import java.util.ResourceBundle;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.myproject.model.entities.LogSession;
import org.myproject.model.entities.LogUser;
import org.myproject.model.repositories.LogSessionRepository;
import org.myproject.model.repositories.UserRepository;
import org.myproject.support.logsession.LogSessionCRUDMBean;
import org.myproject.support.user.UserMBean;
import org.primefaces.context.RequestContext;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.context.support.WebApplicationContextUtils;

@Named
public class CustomRedirectAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    protected Logger       logger = Logger.getLogger(this.getClass());

    @Inject
    private UserRepository userRepository;

    @Inject
    private LogSessionRepository sessionRepository;
    
    private UserMBean userMBean;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws ServletException, IOException {

        String username = authentication.getName();
        String rolename = getRole(username);
        String userId = getUserId(username);;
        String logNameMessage = getUserFullName(username, rolename);
        String lastLogMessage = getLastLogin(username);

        request.getSession().setAttribute("username", username);
        request.getSession().setAttribute("rolename", rolename);
        request.getSession().setAttribute("userId", userId);
        request.getSession().setAttribute("changepass", 0);
        
//      TODO -   String adminTargetUrl = "/pages/admin/admin.am";
        String adminTargetUrl = "/schedule/admin";
        String userTargetUrl = "/pages/user/userT.am";
        String teacherTargetUrl = "/pages/teacher.am";
        String studentTargetUrl = "/public/student.am";

        LogUser user = this.userRepository.findByUserName(username);

//
// 		  To redirect to the LogginController handler
//        
//        if (user.getRndPassword() != null) {
//        	System.out.println("Change Passowrd ");
//            request.getSession().setAttribute("changepass", 1);
//
//            adminTargetUrl = "/public/login";
//         }
        
        user.setAttempts(0);
        
        this.userRepository.saveAndFlush(user);
        
        LogSession session = new LogSession();
        
        session.setUser(user);
        session.setActive(true);
        session.setStartDate(new Date());
        session.setEndDate(new Date());
        
        this.sessionRepository.saveAndFlush(session);

        request.getSession().setAttribute("logNameMessage", logNameMessage);
        
        if (lastLogMessage != null)
          request.getSession().setAttribute("lastLogMessage", lastLogMessage);

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        logger.info("Redirect : " + roles);

        if (roles.contains("ROLE_ADMIN")) {
            getRedirectStrategy().sendRedirect(request, response, adminTargetUrl);
        } else if (roles.contains("ROLE_USER_T")) {
            userTargetUrl = "/pages/user/userT.am";
            getRedirectStrategy().sendRedirect(request, response, userTargetUrl);
        } else if (roles.contains("ROLE_TEACHER")) {
            getRedirectStrategy().sendRedirect(request, response, userTargetUrl);
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
            return;
        }
    }

    private String getRole (String username) {
        String rolename = "NO_ROLE";

        LogUser user = this.userRepository.findByUserName(username);

        if (user != null) {
            logger.debug("Roles " + user.getLogRole().getRolename());
            rolename = user.getLogRole().getRolename();
        } else {
            logger.error("User not found !!!");
        }

        return rolename;
    }

    private String getUserId (String username) {
        Long userId = -1L;

        LogUser user = this.userRepository.findByUserName(username);

        if (user != null) {
            userId = user.getId();
        } else {
            logger.error("User not found !!!");
        }

        return userId.toString();
    }
    
    private String getLastLogin(String username) {
        String lastlogin = "";

        LogUser user = this.userRepository.findByUserName(username);

        if (user != null) {

            if (user.getLastLoginDate() == null) {
                // Para resolver o primeiro acesso colocar CreationDate +
                // Modification Date
                if (user.getCreationDate() != null) {
                    if (user.getModificationDate() != null) {
                        logger.info("Last Login " + user.getModificationDate());
                        lastlogin = user.getModificationDate().toString();
                    }
                } else {
                    lastlogin = new Date().toString();
                    user.setModificationDate(new Date());
                    user.setCreationDate(new Date());
                }
    
                user.setLastLoginDate(user.getModificationDate());
                user.setModificationDate(new Date());
                user.setModificationUser(username);
            } else {
                // Para quando o utilizador está corretamente inserido
                logger.info("Last Login " + user.getLastLoginDate());
                lastlogin = user.getLastLoginDate().toString();
                user.setLastLoginDate(new Date());
            }
                
            this.userRepository.saveAndFlush(user);

        } else {
            logger.error("User not found !!!");
        }

        return lastlogin;
    }

    private String getUserFullName(String username, String rolename) {
        String userFullName = "";

        LogUser user = this.userRepository.findByUserName(username);

        if (user != null) {
            if (rolename.equals("ROLE_USER_T") || rolename.equals("ROLE_ADMIN")) {
                logger.debug("Name " + user.getTeacher().getFullName());
                userFullName = user.getTeacher().getFullName();
            }
        } else {
            logger.error("User not found !!!");
        }

        return userFullName;
    }

}
