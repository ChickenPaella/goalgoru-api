package com.gorugoru.api.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gorugoru.api.component.auth.AuthProvider;
import com.gorugoru.api.domain.model.User;
import com.gorugoru.api.domain.repository.UserRepository;
import com.gorugoru.api.jackson.Views;
import com.gorugoru.api.service.UserService;

/**
 * JSON REST API Test
 * @author Administrator
 *
 */
@RestController
@RequestMapping(path = "/user",  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(TestController.class);
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ObjectMapper mapper;
	
	@RequestMapping(path = "/view/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> view(HttpServletRequest request, ModelMap model,
			@PathVariable("id") String id) throws JsonProcessingException {
		logger.info("view() id: "+id);
		
		User user = userService.getUserById(id);
		if(user == null){
			return new ResponseEntity<String>("{result:\"NOT EXIST\"}", HttpStatus.BAD_REQUEST);
		}
		
        String json = mapper.writerWithView(Views.DEF.class).writeValueAsString(user);
		
		return new ResponseEntity<String>(json, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/save/{name}/{age}", method = RequestMethod.GET)
	public ResponseEntity<?> save(HttpServletRequest request, ModelMap model,
			@PathVariable("name") String name,
			@PathVariable("age") int age) {
		logger.info("save() {} {}", name, age);
		
		Calendar cal = new GregorianCalendar();
		int thisYear = cal.get(Calendar.YEAR);
		cal.set(thisYear - age + 1, 0, 1);
		String date = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		
		User user = userService.registUser(AuthProvider.NONE, "", "id", "pass", "name", date, "email", "phone", "profile", "cardNumber");
		
		return new ResponseEntity<String>("OK "+user.getSeq(), HttpStatus.OK);
	}
	
	@RequestMapping(path = "/cardnumber", method = RequestMethod.POST)
	public ResponseEntity<?> registCardNumber(HttpServletRequest request, @RequestParam("user") User user) {
		logger.info("registCardNumber()");
		logger.info("user id: " + user.getId());
		logger.info("user name: " + user.getName());
		
		userService.registCardNumber(user);
		
		return new ResponseEntity<String>("OK"+user.getCardNumber(),HttpStatus.OK);
	}
	
	@RequestMapping(path = "/list", method = RequestMethod.GET)
	public ResponseEntity<?> list(HttpServletRequest request, ModelMap model) throws JsonProcessingException {
		logger.info("list()");
		
        String json = userService.getUserListJSON(Views.DEF.class);
		
		return new ResponseEntity<String>(json, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/find/{name}/{age}", method = RequestMethod.GET)
	public ResponseEntity<?> list2(HttpServletRequest request, ModelMap model,
			@PathVariable("name") String name,
			@PathVariable("age") int age) {
		logger.info("find()");
		
		//DateUtil.ageToBirthYear(age)
		
		Date date = new Date();
		
		List<User> userList = (List<User>) userRepository.findByNameAndBirthDateLessThan(name, date);
		
		return new ResponseEntity<List<User>>(userList, HttpStatus.OK);
	}
	
	
}
