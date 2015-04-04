package org.myproject.support.settings;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.web.servlet.LocaleResolver;


public class LocaleAwareAuthProcFilter extends AbstractAuthenticationProcessingFilter {
    
    protected LocaleAwareAuthProcFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
        // TODO Auto-generated constructor stub
    }

    private LocaleResolver localeResolver;

    public LocaleResolver getLocaleResolver() {
        
        System.out.println("getLocaleResolver ");
        
        return localeResolver;
    }

    public void setLocaleResolver(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest arg0, HttpServletResponse arg1) throws AuthenticationException,
            IOException, ServletException {
        // TODO Auto-generated method stub
        return null;
    }


 

}