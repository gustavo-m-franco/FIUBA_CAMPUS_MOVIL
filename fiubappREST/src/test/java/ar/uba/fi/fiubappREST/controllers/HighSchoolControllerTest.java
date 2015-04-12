package ar.uba.fi.fiubappREST.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import ar.uba.fi.fiubappREST.domain.HighSchool;
import ar.uba.fi.fiubappREST.services.HighSchoolService;
import ar.uba.fi.fiubappREST.services.StudentSessionService;

public class HighSchoolControllerTest {
	
	private static final String AN_USER_NAME = "anUserName";
	private static final String A_TOKEN = "aToken";

	private HighSchoolController controller;
	
	@Mock
	private HighSchoolService service;
	@Mock
	private StudentSessionService studentSessionService;
	@Mock
	private HighSchool highSchool;
	
	@Before
	public void setUp(){
		this.service = mock(HighSchoolService.class);
		this.studentSessionService = mock(StudentSessionService.class);
		this.controller = new HighSchoolController(service, studentSessionService);
	}

	@Test
	public void testAddHigSchoolInformation() {
		when(service.create(AN_USER_NAME, highSchool)).thenReturn(highSchool);
				
		HighSchool createdHighSchool = this.controller.addHigSchoolInformation(A_TOKEN, AN_USER_NAME, highSchool);
		
		assertEquals(createdHighSchool, highSchool);		
	}
	
	@Test
	public void testGetHigSchoolInformation() {
		when(service.create(AN_USER_NAME, highSchool)).thenReturn(highSchool);
				
		HighSchool createdHighSchool = this.controller.getHigSchoolInformation(A_TOKEN, AN_USER_NAME);
		
		assertEquals(createdHighSchool, highSchool);		
	}
	
	@Test
	public void testDeleteHigSchoolInformation() {
		doNothing().when(service).delete(AN_USER_NAME);
				
		this.controller.deleteHigSchoolInformation(A_TOKEN, AN_USER_NAME);
		
		assertTrue(true);		
	}
	
}

