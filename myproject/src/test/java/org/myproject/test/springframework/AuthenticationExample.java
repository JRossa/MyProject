package org.myproject.test.springframework;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

//  https://docs.spring.io/spring-security/site/docs/3.1.6.RELEASE/reference/technical-overview.html

public class AuthenticationExample {
	  private static AuthenticationManager am = new SampleAuthenticationManager();

	  public static void main(String[] args) throws Exception {
	      String name = "teste002";
	      String password = "JXe*h7O401";

	      try {
	        Authentication request = new UsernamePasswordAuthenticationToken(name, password);
	        Authentication result = am.authenticate(request);
	        SecurityContextHolder.getContext().setAuthentication(result);

	      } catch(AuthenticationException e) {
	        System.out.println("Authentication failed: " + e.getMessage());
	      }

	    System.out.println("Successfully authenticated. Security context contains: " +
	              SecurityContextHolder.getContext().getAuthentication());
	  }
	}


class SampleAuthenticationManager implements AuthenticationManager {
	  static final List<GrantedAuthority> AUTHORITIES = new ArrayList<GrantedAuthority>();

	  static {
	    AUTHORITIES.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
	  }

	  public Authentication authenticate(Authentication auth) throws AuthenticationException {
	    if (auth.getName().equals(auth.getCredentials())) {
	      return new UsernamePasswordAuthenticationToken(auth.getName(),
	        auth.getCredentials(), AUTHORITIES);
	      }
	      throw new BadCredentialsException("Bad Credentials");
	  }
}
