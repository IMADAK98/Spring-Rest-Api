package com.emad.spring.RestControllers;

import java.util.List;

import com.emad.spring.Entity.Course;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.emad.spring.Entity.Instructor;
import com.emad.spring.Exceptions.ExceptionResponse;
import com.emad.spring.Exceptions.ObjectNotFoundException;
import com.emad.spring.Service.InstructorServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

@RestController
@RequestMapping("/api/v1")
public class InstructorController {
	
	private final InstructorServiceImpl instructorServiceImpl;
	
	
	public InstructorController(InstructorServiceImpl instructorServiceImpl) {
		super();
		this.instructorServiceImpl = instructorServiceImpl;
	}

	@GetMapping("/Instructors")
	public List<Instructor> getAllInstructors(){
		return instructorServiceImpl.getAllInstructors();
	}
	
	@GetMapping("/Instructors/{id}")
	public ResponseEntity<Instructor> getInstructorById(@PathVariable int id) {
		return new ResponseEntity<>(instructorServiceImpl.getInstructorById(id)
		,HttpStatus.OK);
	}
	
	@PostMapping("/Instructors")
	public ResponseEntity<Instructor> createInstructor(@Valid @RequestBody  Instructor instructor  ) {	
		Instructor createdInstructor = instructorServiceImpl.createInstructor(instructor);
		return new ResponseEntity<Instructor>(createdInstructor ,HttpStatus.OK);
	}
	
	@PutMapping("/Instructors/{instructorId}")
	public ResponseEntity<Instructor> updateInstructor(@Valid @RequestBody Instructor instructor , @PathVariable int instructorId) {
		return new  ResponseEntity<Instructor>
				(instructorServiceImpl.updateInstructor(instructor , instructorId)
				,HttpStatus.OK);
	}
	
	@DeleteMapping("Instructors/instructor/{instructorId}")
	public ResponseEntity<Void> deleteInstructor(@PathVariable int instructorId) {
		instructorServiceImpl.deleteInstructor(instructorId);
		return ResponseEntity.ok().build();
	}
	
	
	@PostMapping("/Instructors/{instructorId}/{instructorDetailsId}")
	public ResponseEntity<Void> setInstructorDetails(@PathVariable int instructorId , @PathVariable int instructorDetailsId) {
		instructorServiceImpl.setInstructorDetails(instructorId, instructorDetailsId);
		return ResponseEntity.ok().build();
	}
	
	
	@PostMapping("/Instructors_Courses/{instructorId}/{courseId}")
	public ResponseEntity<Instructor> addCourse(@PathVariable int instructorId , @PathVariable int courseId)
	{
		return new ResponseEntity<Instructor>(instructorServiceImpl.addCourse(instructorId, courseId), HttpStatus.OK);
	}
	
	
	
	
	//TO DO  
	//Write a update method .. ie @PutMapping ,, add exception handling and validation to service implementation methods
	//check if controllers need validation also ???
	
	
	// code for exception handling method 
	@ExceptionHandler
	public ResponseEntity<ExceptionResponse> handleException(ObjectNotFoundException exception){
		ExceptionResponse error = new ExceptionResponse();
		error.setStatus(HttpStatus.NOT_FOUND.value());
		error.setMessage(exception.getMessage());
		error.setTimeStamp(System.currentTimeMillis());
		
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<ExceptionResponse> handlResponseEntity (MethodArgumentNotValidException ex){
		ExceptionResponse error = new ExceptionResponse();
		error.setMessage(ex.getFieldError().getDefaultMessage());
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setTimeStamp(System.currentTimeMillis());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
}