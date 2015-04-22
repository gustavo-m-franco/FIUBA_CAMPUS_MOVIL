package ar.uba.fi.fiubappREST.domain;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import ar.uba.fi.fiubappREST.exceptions.CareerAlreadyExistsForStudentException;
import ar.uba.fi.fiubappREST.exceptions.CareerNotFoundForStudentException;
import ar.uba.fi.fiubappREST.exceptions.UnableToDeleteTheOnlyCareerForStudentException;
import ar.uba.fi.fiubappREST.exceptions.StudentAlreadyMateException;

public class StudentTest {
	
	private static final int CAREER_CODE = 10;
	private static final int ANOTHER_CAREER_CODE = 12;
	private static final String AN_USER_NAME = "anUserName";
	
	private StudentCareer studentCareer;
	
	private Student student;
	
	@Before
	public void setUp(){
		Career career = new Career();
		career.setCode(CAREER_CODE);
		
		studentCareer = new StudentCareer();
		studentCareer.setCareer(career);
		
		student = new Student();
		student.setCareers(new ArrayList<StudentCareer>());
		student.getCareers().add(studentCareer);
	}

	@Test
	public void testAddCareerOK() {
		Career career = new Career();
		career.setCode(ANOTHER_CAREER_CODE);
		studentCareer = new StudentCareer();
		studentCareer.setCareer(career);
		
		this.student.addCareer(studentCareer);
				
		assertEquals(2, this.student.getCareers().size());
	}
	
	@Test(expected=CareerAlreadyExistsForStudentException.class)
	public void testAddDuplicatedCareer() {
		Career career = new Career();
		career.setCode(CAREER_CODE);
		studentCareer = new StudentCareer();
		studentCareer.setCareer(career);
		
		this.student.addCareer(studentCareer);
	}

	@Test
	public void testAddMateOK() {
		student.setMates(new ArrayList<Student>());
		Student mate = new Student();
		mate.setMates(new ArrayList<Student>());
		
		this.student.addMate(mate);
				
		assertEquals(1, this.student.getMates().size());
	}
	
	@Test(expected=StudentAlreadyMateException.class)
	public void testAddMateAlreadyExists() {
		student.setMates(new ArrayList<Student>());
		Student mate = new Student();
		mate.setUserName(AN_USER_NAME);
		mate.setMates(new ArrayList<Student>());
		this.student.getMates().add(mate);
		
		this.student.addMate(mate);
	}

	@Test
	public void testRemoveCareerOK() {
		Career career = new Career();
		career.setCode(ANOTHER_CAREER_CODE);
		studentCareer = new StudentCareer();
		studentCareer.setCareer(career);		
		this.student.addCareer(studentCareer);
		
		this.student.removeCareer(studentCareer);
				
		assertEquals(1, this.student.getCareers().size());
	}
	
	@Test(expected=CareerNotFoundForStudentException.class)
	public void testRemoveCareerNotFound() {
		Career career = new Career();
		career.setCode(ANOTHER_CAREER_CODE);
		studentCareer = new StudentCareer();
		studentCareer.setCareer(career);		
		
		this.student.removeCareer(studentCareer);
	}
	
	@Test(expected=UnableToDeleteTheOnlyCareerForStudentException.class)
	public void testRemoveTheOnlyCareer() {		
		this.student.removeCareer(studentCareer);
	}

}

