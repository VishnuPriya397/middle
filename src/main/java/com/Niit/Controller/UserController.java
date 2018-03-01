package com.Niit.Controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.Niit.Dao.UserDAO;
import com.Niit.model.ErrorClass;
import com.Niit.model.User;

@Controller
public class UserController {
	
	@Autowired
	private UserDAO userDAO;
	
	public UserController(){
		System.out.println("UserController");
	}
	
	@RequestMapping(value="/registeruser",method=RequestMethod.POST)
	public ResponseEntity<?> registerUser(@RequestBody User user){
		System.out.println(user.toString());
		if(!userDAO.isEmailUnique(user.getEmail())){
			ErrorClass error=new ErrorClass(1,"Email already exists please type a new one");
		return new ResponseEntity<ErrorClass>(error,HttpStatus.CONFLICT);
		}
		try{
		userDAO.registerUser(user);
		}catch(Exception e){
			ErrorClass error=new ErrorClass(2,"some required fields may be empty"+e.getMessage());
		 return new ResponseEntity<ErrorClass>(error,HttpStatus.INTERNAL_SERVER_ERROR);
		}
			return new ResponseEntity<User>(user,HttpStatus.OK);
	}
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public ResponseEntity<?> login(@RequestBody User user,HttpSession session){
		System.out.println(user);
		User validUser=userDAO.login(user);
		System.out.println(validUser);
		if(validUser==null){
			ErrorClass error=new ErrorClass(5,"Login failed... Invalid email/password...");
		return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);
		}
		else{
			validUser.setOnline(true);
			userDAO.update(validUser);
			session.setAttribute("loginId", user.getEmail());
			return new ResponseEntity<User>(validUser,HttpStatus.OK);
	}
	}

	@RequestMapping(value="/logout",method=RequestMethod.PUT)
	public ResponseEntity<?> logout(HttpSession session) {
		String email=(String)session.getAttribute("loginId");
		if(email==null) {
			ErrorClass error=new ErrorClass(4,"Please login ...");
			return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);
		
	}
		User user=userDAO.getUser(email);
		user.setOnline(false);
		userDAO.update(user);
		session.removeAttribute("loginId");
		session.invalidate();
		return new ResponseEntity<User>(user,HttpStatus.OK);
	}
	@RequestMapping(value="/getuser",method=RequestMethod.GET)
	public ResponseEntity<?> getUser(HttpSession session) {
		String email=(String)session.getAttribute("loginId");
		if(email==null) {
			ErrorClass error=new ErrorClass(5,"Unauthorized access..");
			return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);
	}
		User user=userDAO.getUser(email);
		return new ResponseEntity<User>(user,HttpStatus.OK);
	}
	@RequestMapping(value="/updateuser",method=RequestMethod.PUT)
	public ResponseEntity<?> updateUser(@RequestBody User user,HttpSession session) {
		String email=(String)session.getAttribute("loginId");
		if(email==null) {
			ErrorClass error=new ErrorClass(5,"Unauthorized access..");
					return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);
		}
		try {
			userDAO.update(user);
			return new ResponseEntity<User>(user,HttpStatus.OK);
		} catch(Exception e) {
			ErrorClass error=new ErrorClass(5,"Unable to update user details.."+e.getMessage());
			return new ResponseEntity<ErrorClass>(error,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	}	

	