package com.cst438.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface StudentRepository extends CrudRepository <Student, Integer> {
	
	// declare the following method to return a single Student object
	// default JPA behavior that findBy methods return List<Student> except for findById.
//	@Query("select e from Student e where e.student.email=:email")
//	public Student findByEmail(@Param("email") String email);
//	
//	@Query("select e from Student e where e.student.name=:name and e.student.email=:email")
//	public Student findByNameAndEmail(@Param("name") String name, @Param("email") String email);
//	
//	@SuppressWarnings("unchecked")
//	Student save(Student e);
	
	public Student findByEmail(String email);
}
