package ar.uba.fi.fiubappREST.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import ar.uba.fi.fiubappREST.domain.HighSchool;
import ar.uba.fi.fiubappREST.services.HighSchoolService;
import ar.uba.fi.fiubappREST.services.StudentSessionService;

@Controller
@RequestMapping("students/{userName}/highSchool")
public class HighSchoolController {	
	
	private HighSchoolService highSchoolService;
	private StudentSessionService studentSessionService;
	
	@Autowired
	public HighSchoolController(HighSchoolService highSchoolService, StudentSessionService studentSessionService) {
		super();
		this.highSchoolService = highSchoolService;
		this.studentSessionService = studentSessionService;
	}
		
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public @ResponseBody HighSchool addHigSchoolInformation(@RequestHeader(value="Authorization") String token, @PathVariable String userName, @RequestBody HighSchool highSchool) {
		this.studentSessionService.validateMine(token, userName);
		return highSchoolService.create(userName, highSchool);
	}
}


