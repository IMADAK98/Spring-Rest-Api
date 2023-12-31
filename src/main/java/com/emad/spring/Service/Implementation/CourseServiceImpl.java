package com.emad.spring.Service.Implementation;

import com.emad.spring.Dao.CourseRepository;
import com.emad.spring.Entity.Course;
import com.emad.spring.Entity.Review;
import com.emad.spring.Entity.Student;
import com.emad.spring.Exceptions.InvalidIdException;
import com.emad.spring.Exceptions.ObjectNotFoundException;
import com.emad.spring.Service.Interfaces.CourseService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

	private CourseRepository courseRepository;
	private StudentServiceImpl studentServiceImpl;

	private ReviewServiceImpl reviewServiceImpl;

@Autowired
	public CourseServiceImpl(CourseRepository courseRepository, @Lazy StudentServiceImpl studentServiceImpl,
							 ReviewServiceImpl reviewServiceImpl) {
		super();
		this.courseRepository = courseRepository;
		this.studentServiceImpl = studentServiceImpl;
		this.reviewServiceImpl=reviewServiceImpl;
	}

	public CourseServiceImpl(StudentServiceImpl studentServiceImpl) {
		this.studentServiceImpl = studentServiceImpl;
	}

	public CourseServiceImpl(CourseRepository courseRepository) {
		this.courseRepository = courseRepository;
	}

	public CourseServiceImpl(ReviewServiceImpl reviewServiceImpl) {
		this.reviewServiceImpl = reviewServiceImpl;
	}

	public CourseServiceImpl() {
	}

	@Override
	public List<Course> getAll() {
		// TODO Auto-generated method stub
		return courseRepository.findAll();
	}

	@Override
	public Course create(Course course) {
		return courseRepository.save(course);
	}

	public Course getById(Integer courseId) {
		validateId(courseId);
		return courseRepository.findById(courseId)
				.orElseThrow( ()-> new ObjectNotFoundException("object is null")
		);
	}
	
	public Course update(Course course , Integer courseID) {
		validateId(courseID);

		Course existingCourse = courseRepository.findById(courseID)
				.orElseThrow(()-> new ObjectNotFoundException("object is null"));
		existingCourse.setTitle(course.getTitle());
		courseRepository.save(existingCourse);
		return existingCourse;
	}


	public void delete(Integer courseId) {
		validateId(courseId);
		courseRepository.findById(courseId)
				.ifPresentOrElse(
						value-> {
							value.setInstructor(null);
							courseRepository.deleteById(value.getId());
						},
						()-> {throw new ObjectNotFoundException("Object not found");}
				);
	}

	@Override
	public Course setStudent(int courseId, int studentId) {
			validateId(courseId, studentId);
			Course tempCourse = courseRepository.findById(courseId)
				.orElseThrow(()-> new ObjectNotFoundException("null course object")
				);
			Student tempStudent = studentServiceImpl.getById(studentId);
			tempCourse.addStudent(tempStudent);

		return courseRepository.save(tempCourse);
	}

	@Override
	public List<Course> findCourseByStudentsId(int id) {
		return courseRepository.findCourseByStudentsId(id);
	}

	@Override
	public Course setCourseForReview(int courseId, int reviewId) {
		validateId(courseId,reviewId);
		Course tempCourse = courseRepository.findById(courseId)
				.orElseThrow(()->new ObjectNotFoundException("Course object is null"));
		Review tempReview =reviewServiceImpl.getById(reviewId);
		tempReview.setCourse(tempCourse);
		return courseRepository.save(tempCourse);
	}


	public void validateId(int... ids){
		for (int id : ids){
			if (id<=0){
				throw new InvalidIdException("Invalid Id: " + id);
			}
		}
	}
	


}
