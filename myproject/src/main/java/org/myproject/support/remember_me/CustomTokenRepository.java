package org.myproject.support.remember_me;

import org.hibernate.SessionFactory;
import org.myproject.model.entities.PersistentLogin;
import org.myproject.model.repositories.PersistentLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import java.util.Date;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;

@Component
@Named(value = "customTokenRepository")
public class CustomTokenRepository implements PersistentTokenRepository {

	@Inject
	private PersistentLoginRepository persistentLoginRepository;
	
	private PersistentLogin persistentLogin;
	
	
    @Override
    public void createNewToken(PersistentRememberMeToken token) {

    	System.out.println("CTR - Series       : " + token.getSeries());
    	System.out.println("CTR - Token Value  : " + token.getTokenValue());
    	System.out.println("CTR - Last Used    : " + token.getDate());
    	System.out.println("CTR - User Name    : " + token.getUsername());
   	
    	this.persistentLogin = new PersistentLogin();
    	
    	this.persistentLogin.setToken(token.getTokenValue());
    	this.persistentLogin.setLast_used(token.getDate());
    	this.persistentLogin.setSeries(token.getSeries());
    	this.persistentLogin.setUsername(token.getUsername());
    	
    	this.persistentLoginRepository.saveAndFlush(this.persistentLogin);
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
    	
    	System.out.println("CTR - Series      : " + series);
    	System.out.println("CTR - Token Value : " + tokenValue);
    	System.out.println("CTR - Last Used   : " + lastUsed);

    	PersistentLogin token = this.persistentLoginRepository.findBySeries(series);

    	if (token != null) {
    		token.setToken(tokenValue);
    		token.setLast_used(lastUsed);

    		this.persistentLoginRepository.save(token);
    	}
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String series) {
    	
    	System.out.println("CTR - Series      : " + series);
 
    	PersistentLogin token = this.persistentLoginRepository.findBySeries(series);
    	
        if (token == null) {
            return null;
        }
        return new PersistentRememberMeToken(token.getUsername(),
                token.getSeries(), token.getToken(), token.getLast_used());
    }

	@Override
	public void removeUserTokens(String username) {

		System.out.println("CTR - User Name    : " + username);

    	PersistentLogin token = this.persistentLoginRepository.findByUsername(username);

    	this.persistentLoginRepository.delete(token);
	}
}
