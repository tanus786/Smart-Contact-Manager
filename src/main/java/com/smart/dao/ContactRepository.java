package com.smart.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.entities.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

	// Pagination
	@Query("from Contact as c where c.user.id =:userId")
	// Current page and its record
	public Page<Contact> findContactByUser(@Param("userId") int userId, Pageable pageable);

}
