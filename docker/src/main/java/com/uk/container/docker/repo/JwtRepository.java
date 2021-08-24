/**
 * 
 */
package com.uk.container.docker.repo;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author pranika
 *
 */
public interface JwtRepository extends CrudRepository<JwtTokenDetails, String> {

	@Transactional
	@Modifying
	@Query(value = "update jwt_token set is_expired= :isExpired, updated_date= :updatedDate where jwt_token= :jwtToken", nativeQuery = true)
	void updateIsExpired(String jwtToken, String isExpired, LocalDateTime updatedDate);

}
