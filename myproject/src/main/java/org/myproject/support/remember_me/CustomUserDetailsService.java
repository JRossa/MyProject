package org.myproject.support.remember_me;

import org.myproject.model.entities.LogUser;
import org.myproject.model.repositories.UserRepository;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
	
    @Inject
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LogUser user = userRepository.findByUserName(username);
        
        return new User(user.getUserName(), user.getPassword(),
                AuthorityUtils.commaSeparatedStringToAuthorityList(user.getLogRole().getRolename()));
    }

}
