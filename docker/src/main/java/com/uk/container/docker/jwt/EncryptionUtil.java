package com.uk.container.docker.jwt;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.xml.bind.DatatypeConverter;

import org.springframework.util.StringUtils;

//https://peoplesofttutorial.com/generating-key-store-and-trust-store-using-keytool/
//https://docs.oracle.com/javase/10/tools/keytool.htm#JSWOR-GUID-5990A2E4-78E3-47B7-AE75-6D1826259549
//https://stackoverflow.com/questions/47434877/how-to-generate-keystore-and-truststore
//File is copied at D:\KeyStore\keyStore-steps.txt
//https://www.baeldung.com/spring-security-oauth-jwt
public final class EncryptionUtil {
	
	private static final String SIGNING_ALGO = "RSA";
	private static final CharSequence PRIVATE_KEY_START = "-----BEGIN CERTIFICATE-----";
	private static final CharSequence PRIVATE_KEY_END = "-----END CERTIFICATE-----";
	private static KeyFactory KEY_FACTORY = null;
	private static final CharSequence PUBLIC_KEY_START = "-----BEGIN CERTIFICATE-----";
	private static final CharSequence PUBLIC_KEY_END = "-----END CERTIFICATE-----";
	
	static {
		try {
			System.out.println("----Static block----");
			KEY_FACTORY = KeyFactory.getInstance(SIGNING_ALGO);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	public static PrivateKey getPrivateKey(String privateKey) throws InvalidKeySpecException {
		if(StringUtils.isEmpty(privateKey)) {
			throw new IllegalArgumentException("Invalid key");	
		}
		
		final String senitizedKey = privateKey.replaceAll("\\n", "").replace(PRIVATE_KEY_START, "").replace(PRIVATE_KEY_END, "").trim();
		byte[] keyBytes = DatatypeConverter.parseBase64Binary(senitizedKey);
		final PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		return KEY_FACTORY.generatePrivate(spec);
	}
	
	public static PublicKey getPublicKey(String publicKey) {
		if(StringUtils.isEmpty(publicKey)) {
			throw new IllegalArgumentException("Invalid key");	
		}
		System.out.println(publicKey);
		try {
		final String senitizedKey = publicKey.replaceAll("\\n", "").replace(PUBLIC_KEY_START, "").replace(PUBLIC_KEY_END, "").trim();
		byte[] keyBytes = DatatypeConverter.parseBase64Binary(senitizedKey);
		final X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		
			return KEY_FACTORY.generatePublic(spec);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private EncryptionUtil() {}

}
