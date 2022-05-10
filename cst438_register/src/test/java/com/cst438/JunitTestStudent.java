package com.cst438;

import static org.mockito.ArgumentMatchers.any;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.cst438.controller.ScheduleController;
import com.cst438.controller.StudentController;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.ScheduleDTO;
import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;
import com.cst438.domain.StudentRepository;
import com.cst438.service.GradebookService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = { StudentController.class })
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest
public class JunitTestStudent {
	
	static final String URL = "http://localhost:8080";
	public static final int TEST_STUDENT_ID = 18;
	public static final String TEST_STUDENT_NAME = "testing";
	public static final String TEST_STUDENT_EMAIL = "testing@csumb.edu";
	public static final int TEST_STUDENT_STATUS_CODE = 1;
	
	@MockBean
	StudentRepository studentRepository;
	
	@Autowired
	private MockMvc mvc;
	
	@Test
	public void addStudent() throws Exception {
		
		MockHttpServletResponse response;
		
		Student student = new Student();
		student.setStudent_id(TEST_STUDENT_ID);
		student.setName(TEST_STUDENT_NAME);
		student.setEmail(TEST_STUDENT_EMAIL);
		
		given(studentRepository.findById(TEST_STUDENT_ID)).willReturn(null);
		given(studentRepository.findByEmail(TEST_STUDENT_EMAIL)).willReturn(null);
		given(studentRepository.save(any(Student.class))).willReturn(student);
		
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.student_id = TEST_STUDENT_ID;
		studentDTO.email = TEST_STUDENT_EMAIL;
		studentDTO.name = TEST_STUDENT_NAME;
		
		response = mvc.perform(
				MockMvcRequestBuilders
				 .post("/student")
				 .content(asJsonString(studentDTO))
				 .contentType(MediaType.APPLICATION_JSON)
				 .accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertEquals(200, response.getStatus());
		
		StudentDTO result = fromJsonString(response.getContentAsString(), StudentDTO.class);
		assertNotEquals(0, result.student_id);
		
		verify(studentRepository).save(any(Student.class));
	}

	@Test
	public void getStudent() throws Exception {

		MockHttpServletResponse response;
		
		Student student = new Student();
		student.setStudent_id(TEST_STUDENT_ID);
		
		given(studentRepository.findById(TEST_STUDENT_ID)).willReturn(student);
		
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.student_id = TEST_STUDENT_ID;
		response = mvc.perform(
				MockMvcRequestBuilders
				 .get("/student?id=" + TEST_STUDENT_ID)
				 .accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertEquals(200, response.getStatus());
		
		
		studentDTO = fromJsonString(response.getContentAsString(), StudentDTO.class);
		
		boolean found = false;	
		if(studentDTO.student_id == TEST_STUDENT_ID) {
			found = true;
		}
		assertTrue("Added student not found.", found);
		
		// verify that repository find method was called.
		verify(studentRepository, times(1)).findById(TEST_STUDENT_ID);
	}
	
	@Test
	public void updateStatus() throws Exception {
		
		MockHttpServletResponse response;

		Student student = new Student();
		student.setStudent_id(TEST_STUDENT_ID);
		student.setStatusCode(TEST_STUDENT_STATUS_CODE);
		
		given(studentRepository.findById(TEST_STUDENT_ID)).willReturn(student);
		given(studentRepository.save(any(Student.class))).willReturn(student);
		
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.student_id = TEST_STUDENT_ID;
		studentDTO.status_code = TEST_STUDENT_STATUS_CODE;
		response = mvc.perform(
				MockMvcRequestBuilders
				 .put("/student/" + studentDTO.student_id)
				 .content(asJsonString(studentDTO))
				 .contentType(MediaType.APPLICATION_JSON)
				 .accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertEquals(200, response.getStatus());
	}
	
	private static String asJsonString(final Object obj) {
		try {

			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static <T> T  fromJsonString(String str, Class<T> valueType ) {
		try {
			return new ObjectMapper().readValue(str, valueType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
