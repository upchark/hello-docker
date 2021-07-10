package com.uk.container.docker.repo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{
	
	public Employee findByFirstName(String firstName);

}
