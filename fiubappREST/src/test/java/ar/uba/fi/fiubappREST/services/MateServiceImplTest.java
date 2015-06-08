package ar.uba.fi.fiubappREST.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import ar.uba.fi.fiubappREST.converters.StudentProfileConverter;
import ar.uba.fi.fiubappREST.domain.ApplicationNotification;
import ar.uba.fi.fiubappREST.domain.Student;
import ar.uba.fi.fiubappREST.exceptions.StudentNotFoundException;
import ar.uba.fi.fiubappREST.persistance.NotificationRepository;
import ar.uba.fi.fiubappREST.persistance.StudentRepository;
import ar.uba.fi.fiubappREST.representations.StudentProfileRepresentation;

public class MateServiceImplTest {
	
	private static final String AN_USER_NAME = "anUserName";
	private static final String ANOTHER_USER_NAME = "anotherUserName";
	private static final String A_MATE_USER_NAME = "aMateUserName";
	
	@Mock
	private StudentRepository studentRepository;
	@Mock
	private NotificationRepository notificationRepository;
	@Mock
	private StudentProfileConverter converter;
	@Mock
	private StudentProfileRepresentation profile;
	
	private Student student;
		
	private MateService service;
	
	@Before
	public void setUp() throws ParseException{
		this.studentRepository = mock(StudentRepository.class);
		this.notificationRepository = mock(NotificationRepository.class);
		this.converter = mock(StudentProfileConverter.class);
		this.service= new MateServiceImpl(studentRepository, notificationRepository, converter);
				
		this.student = new Student();
		this.student.setUserName(AN_USER_NAME);
		this.student.setMates(new ArrayList<Student>());
		
		this.profile = mock(StudentProfileRepresentation.class);
	}
		
	@Test
	public void testBecomeMates(){
		student.setMates(new ArrayList<Student>());
		Student mate = new Student();
		mate.setUserName(A_MATE_USER_NAME);
		mate.setMates(new ArrayList<Student>());
		when(this.studentRepository.findByUserNameAndFetchMatesEagerly(AN_USER_NAME)).thenReturn(student);
		when(this.studentRepository.findByUserNameAndFetchMatesEagerly(A_MATE_USER_NAME)).thenReturn(mate);
		when(this.studentRepository.save(student)).thenReturn(student);
		when(this.studentRepository.save(mate)).thenReturn(mate);
		when(this.converter.convert(student, mate)).thenReturn(profile);
		ApplicationNotification notification = new ApplicationNotification();
		List<ApplicationNotification> notifications = new ArrayList<ApplicationNotification>();
		notifications.add(notification);
		when(this.notificationRepository.findByUserNameAndApplicantUserName(AN_USER_NAME, A_MATE_USER_NAME)).thenReturn(notifications);
		doNothing().when(notificationRepository).delete(notification);
		
		StudentProfileRepresentation mateProfile = this.service.becomeMates(AN_USER_NAME, A_MATE_USER_NAME);
		
		assertEquals(profile, mateProfile);
	}
	
	@Test(expected=StudentNotFoundException.class)
	public void testBecomeMatesStudentNotFound(){
		when(this.studentRepository.findByUserNameAndFetchMatesEagerly(AN_USER_NAME)).thenReturn(null);
		
		this.service.becomeMates(AN_USER_NAME, A_MATE_USER_NAME);
	}
	
	@Test
	public void testGetMates(){
		Student mate = new Student();
		mate.setUserName(A_MATE_USER_NAME);
		mate.setMates(new ArrayList<Student>());
		student.addMate(mate);
		when(this.studentRepository.findByUserNameAndFetchMatesEagerly(AN_USER_NAME)).thenReturn(student);
		when(this.converter.convert(student, mate)).thenReturn(profile);
		
		List<StudentProfileRepresentation> mates = this.service.getMates(AN_USER_NAME);
		
		assertEquals(1, mates.size());
	}
	
	@Test
	public void testDeleteMate(){
		student.setMates(new ArrayList<Student>());
		Student mate = new Student();
		mate.setUserName(A_MATE_USER_NAME);
		mate.setMates(new ArrayList<Student>());
		student.addMate(mate);
		when(this.studentRepository.findByUserNameAndFetchMatesEagerly(AN_USER_NAME)).thenReturn(student);
		when(this.studentRepository.findByUserNameAndFetchMatesEagerly(A_MATE_USER_NAME)).thenReturn(mate);
		when(this.studentRepository.save(student)).thenReturn(student);
		when(this.studentRepository.save(mate)).thenReturn(mate);
		
		this.service.deleteMate(AN_USER_NAME, A_MATE_USER_NAME);
		
		assertTrue(true);
	}
	
	@Test
	public void testGetCommonsMates(){		
		Student firstStudent = mock(Student.class);
		when(this.studentRepository.findByUserNameAndFetchMatesEagerly(AN_USER_NAME)).thenReturn(firstStudent);
		Student secondStudent = mock(Student.class);
		when(this.studentRepository.findByUserNameAndFetchMatesEagerly(ANOTHER_USER_NAME)).thenReturn(secondStudent);
		
		Student aCommonMate = mock(Student.class);
		Student anotherCommonMate = mock(Student.class);
		Student yetAnotherCommonMate = mock(Student.class);
		
		Student aFirstStudentMate = mock(Student.class);
		Student anotherFirstStudentMate = mock(Student.class);
		
		Student aSecondStudentMate = mock(Student.class);
		
		List<Student> firstStudentMates = new ArrayList<Student>();
		firstStudentMates.add(secondStudent);
		firstStudentMates.add(aFirstStudentMate);
		firstStudentMates.add(anotherFirstStudentMate);
		firstStudentMates.add(aCommonMate);
		firstStudentMates.add(anotherCommonMate);
		firstStudentMates.add(yetAnotherCommonMate);
		when(firstStudent.getMates()).thenReturn(firstStudentMates);
		
		List<Student> secondStudentMates = new ArrayList<Student>();
		secondStudentMates.add(firstStudent);
		secondStudentMates.add(aSecondStudentMate);
		secondStudentMates.add(aCommonMate);
		secondStudentMates.add(anotherCommonMate);
		secondStudentMates.add(yetAnotherCommonMate);
		when(secondStudent.getMates()).thenReturn(secondStudentMates);
		
		StudentProfileRepresentation aCommonMateRepresentation = mock(StudentProfileRepresentation.class);
		when(this.converter.convert(secondStudent, aCommonMate)).thenReturn(aCommonMateRepresentation);
		StudentProfileRepresentation anotherCommonMateRepresentation = mock(StudentProfileRepresentation.class);
		when(this.converter.convert(secondStudent, anotherCommonMate)).thenReturn(anotherCommonMateRepresentation);
		StudentProfileRepresentation yetAnotherCommonMateRepresentation = mock(StudentProfileRepresentation.class);
		when(this.converter.convert(secondStudent, yetAnotherCommonMate)).thenReturn(yetAnotherCommonMateRepresentation);
		
		List<StudentProfileRepresentation> commonsMates = this.service.getCommonstMates(AN_USER_NAME, ANOTHER_USER_NAME);
		
		assertEquals(3, commonsMates.size());
		assertTrue(commonsMates.contains(aCommonMateRepresentation));
		assertTrue(commonsMates.contains(anotherCommonMateRepresentation));
		assertTrue(commonsMates.contains(yetAnotherCommonMateRepresentation));
	}
}
