package com.emad.spring.Service.Implementation;

import com.emad.spring.Dao.InstructorRepository;
import com.emad.spring.Entity.Course;
import com.emad.spring.Entity.Instructor;
import com.emad.spring.Entity.InstructorDetails;
import com.emad.spring.Exceptions.InvalidIdException;
import com.emad.spring.Exceptions.ObjectNotFoundException;
import com.emad.spring.Service.Interfaces.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class InstructorServiceImpl implements InstructorService {
	private  InstructorRepository instructorRepository;
	private InstructorDetailsServiceImpl instructorDetailsServiceImpl;

	private CourseServiceImpl courseServiceImpl;
	
	
	@Autowired
	public InstructorServiceImpl(InstructorRepository instructorRepository,
								@Lazy InstructorDetailsServiceImpl instructorDetailsServiceImpl,
								 CourseServiceImpl courseServiceImpl)
	{
		this.instructorRepository = instructorRepository;
		this.instructorDetailsServiceImpl = instructorDetailsServiceImpl;
		this.courseServiceImpl = courseServiceImpl;
	}

	public InstructorServiceImpl (InstructorRepository instructorRepository) {
		this.instructorRepository = instructorRepository;
	}

	public InstructorServiceImpl() {
	}

	@Override
	public List<Instructor> getAll() {
		return instructorRepository.findAll();
	}

	@Override
	public Instructor getById(Integer id) {
		validateId(id);
		return instructorRepository.findById(id)
				.orElseThrow(()-> new ObjectNotFoundException("Object is null"));
	}


	@Override
	// Add verification 
	public Instructor create(Instructor instructor) {
		return  instructorRepository.save(instructor);
	}

	@Override
	public void delete(Integer instructorId) {
		validateId(instructorId);
		instructorRepository.findById(instructorId)
				.ifPresentOrElse(
						value-> instructorRepository.deleteById(instructorId),
						()-> {throw new ObjectNotFoundException("not found"); }
				);
	}


	@Override
	public Instructor update (Instructor instructor , Integer instructorId){
		validateId(instructorId);
		return instructorRepository.findById(instructorId)
				.map(
						value -> {
							value.setFirstName(instructor.getFirstName());
							value.setLastName(instructor.getLastName());
							value.setEmail(instructor.getEmail());
							return instructorRepository.save(value);
						}
				)
				.orElseThrow(() ->new  ObjectNotFoundException("Instructor not found"));
	}



	public void setInstructorDetails(int instructorId, int instructorDetailsId) {

		validateId(instructorId,instructorDetailsId);

		Instructor tempInstructor = instructorRepository.findById(instructorId)
				.orElseThrow(
						()-> new ObjectNotFoundException("instructor does not exist !!")
				);

		InstructorDetails tempInstructorDetails =
				instructorDetailsServiceImpl.getById(instructorDetailsId);

		tempInstructor.setInstructorDetails(tempInstructorDetails);
		instructorRepository.save(tempInstructor);
		
	}

	
	public Instructor addCourse(int instructorId , int courseId){
		validateId(instructorId, courseId);
		Instructor tempInstructor = instructorRepository.findById(instructorId)
				.orElseThrow(
						()-> new ObjectNotFoundException("instructor does not exist !!!")
				);
		Course tempCourse = courseServiceImpl.getById(courseId);
		tempInstructor.add(tempCourse);
		return instructorRepository.save(tempInstructor);
	}




	public void validateId(int... ids){
		for (int id : ids){
			if (id<0){
				throw new InvalidIdException("Invalid Id: " + id);
			}
		}
	}





}
