package com.training.WalletPlus.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.WalletPlus.model.User;
import com.training.WalletPlus.repo.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	
        try {
            User user = userRepository.findById(username).get();
            return UserDetailsImpl.build(user);
        }
        catch(Exception e) {
        	e.printStackTrace();
            throw new UsernameNotFoundException("User not found with username : " + username);
        }
    }
}