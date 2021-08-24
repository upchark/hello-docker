package com.uk.container.docker.jwt;

import java.io.Serializable;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final Long JWT_TOKEN_VALIDITY = 1L * 60;//2 Mins

	@Value("${jwt.secret:#{null}}")
	private String secretKey;

	private final ObjectMapper mapper;

	public JWTUtil(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	/* 1. GET USERNAME FROM TOKEN */
	public String getUserNameFromToken(String jwt) {
		return getClaimFromToken(jwt, Claims::getSubject);
	}

	private <T> T getClaimFromToken(String jwt, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(jwt);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String jwt) {
		System.out.println("getAllClaimsFromToken:"+secretKey);
		System.out.println("getAllClaimsFromToken:"+jwt);
		JwtParser parser = Jwts.parser();
		parser.setSigningKey(secretKey);
		//parser.setSigningKey(EncryptionUtil.getPublicKey(secretKey));
		Jws<Claims> claims = parser.parseClaimsJws(jwt);
		return claims.getBody();
	}

	/* 2. GENERATE TOKEN */
	public String generateToken(UserDetails userDetails) throws InvalidKeySpecException, JsonProcessingException {
		Map<String, Object> claims = new HashMap<>();
		claims.put("userDetails", mapper.writeValueAsString(userDetails));
		return generateToken(claims, userDetails.getUsername());
	}

	// while creating the token -
	// 1. Define claims of the token, like Issuer, Expiration, Subject, and the ID
	// 2. Sign the JWT using the HS512 algorithm and secret key.
	// 3. According to JWS Compact
	// Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
	// compaction of the JWT to a URL-safe string
	private String generateToken(Map<String, Object> claims, String username) throws InvalidKeySpecException {
		JwtBuilder builder = Jwts.builder().setClaims(claims).setSubject(username)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
				.signWith(SignatureAlgorithm.HS512, secretKey);
				//.signWith(SignatureAlgorithm.RS256, EncryptionUtil.getPublicKey(secretKey));
		return builder.compact();
	}

	/* 3. VALIDATE TOKEN */
	public boolean validateToken(String jwt, UserDetails userDetails) {
		final String userName = getUserNameFromToken(jwt);
		return userName.equals(userDetails.getUsername()) && !isTokenExpired(jwt);
	}

	private boolean isTokenExpired(String jwt) {
		final Date expireDate = getExpirationDateFromToken(jwt);
		return expireDate.before(new Date());
	}

	/* 4. GET EXPIRY DATE FROM TOKEN */
	public Date getExpirationDateFromToken(String jwt) {
		return getClaimFromToken(jwt, Claims::getExpiration);
	}

	/* 5. GET USER FROM TOKEN */
	public UserDetails getUserDetailsFromClaim(String jwt) {
		return getClaimFromToken(jwt, (Claims claims) -> {
			final String userdetails = claims.get("userDetails", String.class);
			try {
				return mapper.readValue(userdetails, UserDetails.class);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			return null;
		});
	}

}
