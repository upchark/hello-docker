package com.uk.container.docker.repo;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JWT_TOKEN")
public class JwtTokenDetails {
	@Id
	@Column(name = "jwt_token", length = 800)
	private String jwtToken;
	@Column(name = "generated_date")
	private LocalDateTime generatedDate;
	@Column(name = "expiry_date")
	private LocalDateTime expiredDate;
	@Column(name = "is_expired")
	private String isExpired;
	@Column(name = "updated_date")
	private LocalDateTime updatedDate;
	
	public JwtTokenDetails() {}

	public JwtTokenDetails(String jwtToken, LocalDateTime generatedAt, LocalDateTime expiryDate, String isExpired, LocalDateTime updatedDate) {
		this.jwtToken = jwtToken;
		this.generatedDate = generatedAt;
		this.expiredDate = expiryDate;
		this.isExpired = isExpired;
		this.updatedDate = updatedDate;
	}

	public String getJwtToken() {
		return jwtToken;
	}

	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}

	public LocalDateTime getGeneratedDate() {
		return generatedDate;
	}

	public void setGeneratedDate(LocalDateTime generatedDate) {
		this.generatedDate = generatedDate;
	}

	public LocalDateTime getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(LocalDateTime expiredDate) {
		this.expiredDate = expiredDate;
	}

	public String getIsExpired() {
		return isExpired;
	}

	public void setIsExpired(String isExpired) {
		this.isExpired = isExpired;
	}

	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(LocalDateTime updatedDate) {
		this.updatedDate = updatedDate;
	}

}
