package com.uk.container.docker.repo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "TBL_EMPLOYEES")
public class Employee {
	@Id
	@GeneratedValue
	private Long id;
	@Column(name = "first_name", insertable = true, updatable = true, length = 100)
	private String firstName;
	@Column(name = "last_name", insertable = true, updatable = true, length = 100)
	private String lastName;
	@Column(name = "email", insertable = true, updatable = true, nullable = false, unique = false)
	private String email;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + "]";
	}

}
