package com.uk.container.docker.jwt;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.uk.container.docker.repo.JwtRepository;

import io.jsonwebtoken.ExpiredJwtException;

//https://www.javainuse.com/spring/boot-jwt

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtRepository jwtRepository;

	private static final String AUTH_HEADER_TYPE = "Bearer";

	List<String> jwtExcludedUrls = Arrays.asList("http://localhost:8081/h2");

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		System.out.println("----Enter JwtRequestFilter=========doFilterInternal----");
		String userName = null;
		String jwtToken = null;
		final String authHeader = request.getHeader("Authorization");
		System.out.println("Header:" + authHeader);
		try {
			if (!StringUtils.isEmpty(authHeader) && null != authHeader && authHeader.startsWith(AUTH_HEADER_TYPE)) {
				jwtToken = authHeader.substring(7);
				System.out.println("JWT:" + jwtToken);
				userName = jwtUtil.getUserNameFromToken(jwtToken);
			}
			if (null != userName && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(userName);

				boolean isExcluded = jwtExcludedUrls.stream()
						.anyMatch(urlPath -> request.getRequestURI().contains(urlPath));
				System.out.println("isExcluded:" + isExcluded);
				// if token is valid configure Spring Security to manually set authentication
				if (!isExcluded) {
					if (jwtUtil.validateToken(jwtToken, userDetails)) {
						UsernamePasswordAuthenticationToken upa = new UsernamePasswordAuthenticationToken(
								userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
						upa.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						// After setting the Authentication in the context, we specify
						// that the current user is authenticated. So it passes the
						// Spring Security Configurations successfully.
						SecurityContextHolder.getContext().setAuthentication(upa);
					} else {
						throw new ExpiredJwtException(null, null, "Token is expired");
					}
				}
			}
		} catch (Exception ex) {
			jwtRepository.updateIsExpired(jwtToken, "Y", LocalDateTime.now());
			throw ex;
		}
		filterChain.doFilter(request, response);
		System.out.println("----EXIT JwtRequestFilter=========doFilterInternal----");
	}

}
