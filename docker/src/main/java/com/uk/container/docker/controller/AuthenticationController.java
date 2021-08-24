package com.uk.container.docker.controller;

import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.uk.container.docker.jwt.JWTUtil;
import com.uk.container.docker.jwt.JwtRequest;
import com.uk.container.docker.jwt.JwtResponse;
import com.uk.container.docker.jwt.JwtUserDetailsService;
import com.uk.container.docker.repo.JwtRepository;
import com.uk.container.docker.repo.JwtTokenDetails;

@RestController
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	@Qualifier("jwtUserDetailsService")
	private JwtUserDetailsService jwtUserDetailsService;

	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private JwtRepository jwtRepository;

	@PostMapping(value = "/authenticate")
	public ResponseEntity<JwtResponse> authenticateUser(@RequestBody JwtRequest jwtRequest) {
		System.out.println("AuthenticationController==========Start authenticateUser---");
		System.out.println(jwtRequest.getUserName()+":"+jwtRequest.getPassword());
		authenticate(jwtRequest.getUserName(), jwtRequest.getPassword());
		final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(jwtRequest.getUserName());
		String jwtToken = null;
		try {
			jwtToken = jwtUtil.generateToken(userDetails);
		} catch (InvalidKeySpecException | JsonProcessingException e) {
			e.printStackTrace();
		}
		JwtResponse jwtResponse = new JwtResponse(jwtToken);
		jwtResponse.setGeneratedAt(LocalDateTime.now());
		Date date = jwtUtil.getExpirationDateFromToken(jwtToken);
		LocalDateTime expiryDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		jwtResponse.setExpiryDate(expiryDate);
		
		JwtTokenDetails jwtTokenDetails = new JwtTokenDetails(jwtResponse.getJwtToken(), jwtResponse.getGeneratedAt(), jwtResponse.getExpiryDate(), "N", LocalDateTime.now());
		jwtRepository.save(jwtTokenDetails );
		return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
	}

	private void authenticate(String userName, String password) {
		System.out.println("---AuthenticationController=======Enter authenticate---");
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
		System.out.println("---AuthenticationController=====Exit authenticate---");
	}

}
