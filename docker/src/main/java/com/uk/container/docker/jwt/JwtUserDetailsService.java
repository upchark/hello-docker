package com.uk.container.docker.jwt;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("---JwtUserDetailsService=======loadUserByUsername---" + username);
		if ("upchar".equalsIgnoreCase(username)) {
			return new User(username, "$2a$10$33ZlyPgcyhxSyxvbeq9E5eZac3aR.OYAJv.c5mzKDo0w/oBZ3Xqlm",
					new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("User not found:" + username);
		}
	}

}
