package com.cst438.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.ScheduleDTO;
import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;
import com.cst438.domain.StudentRepository;
import com.cst438.service.GradebookService;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class StudentController {
	
	@Autowired
	StudentRepository studentRepository;
	
	@GetMapping("/student")
	public StudentDTO getStudents() {
		System.out.println("/schedule called.");
		String student_email = "test@csumb.edu";   // student's email 
		
		Student student = studentRepository.findByEmail(student_email);
		if (student != null) {
			System.out.println("/schedule student "+student.getName()+" "+student.getStudent_id());
			return createStudentDTO(student);
		} else {
			System.out.println("/schedule student not found. "+student_email);
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Student not found. " );
		}
	}
	
	@PostMapping("/student")
	@Transactional
	public StudentDTO addStudent( @RequestBody StudentDTO studentDTO) {

		String student_name = "jorge";
		String student_email = "papichulo@csumb.edu";   // student's email
		
		Student studentCreds = studentRepository.findByEmail(studentDTO.email);
		
		if (studentCreds == null) {
			Student student = new Student();
			student.setName(student_name);
			student.setEmail(student_email);
			Student savedStudent = studentRepository.save(student);
			
			StudentDTO result = createStudentDTO(savedStudent);
			return result;
		} else {
			throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "Student already exists.  ");
		}
		
	}
	
	private StudentDTO createStudentDTO(Student s) {
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.student_id = s.getStudent_id();
		studentDTO.name = s.getName();
		studentDTO.email = s.getEmail();
		return studentDTO;
	}
	
}