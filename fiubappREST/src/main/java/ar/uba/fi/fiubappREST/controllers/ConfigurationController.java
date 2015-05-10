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

import ar.uba.fi.fiubappREST.domain.LocationConfiguration;
import ar.uba.fi.fiubappREST.services.ConfigurationService;
import ar.uba.fi.fiubappREST.services.StudentSessionService;

@Controller
@RequestMapping("students/{userName}/configurations")
public class ConfigurationController {	
	
	private ConfigurationService configurationService;
	private StudentSessionService studentSessionService;
	
	@Autowired
	public ConfigurationController(ConfigurationService configurationService, StudentSessionService studentSessionService) {
		super();
		this.configurationService = configurationService;
		this.studentSessionService = studentSessionService;
	}
		
	@RequestMapping(value="location", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody LocationConfiguration getLocationConfiguration(@RequestHeader(value="Authorization") String token, @PathVariable String userName) {
		this.studentSessionService.validateMine(token, userName);
		return this.configurationService.getLocationConfiguration(userName);
	}
	
	@RequestMapping(value="location", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody LocationConfiguration updateLocationNotification(@RequestHeader(value="Authorization") String token, @PathVariable String userName, @RequestBody LocationConfiguration locationConfiguration) {
		this.studentSessionService.validateMine(token, userName);
		return this.configurationService.updateLocationConfiguration(userName, locationConfiguration);
	}
	
}

