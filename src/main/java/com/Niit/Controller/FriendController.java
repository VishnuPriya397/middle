package com.Niit.Controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.Niit.Dao.FriendDAO;
import com.Niit.model.ErrorClass;
import com.Niit.model.User;

@Controller
public class FriendController {
	
	@Autowired
	private FriendDAO friendDAO;
	
	public FriendController(){
		System.out.println("FriendController");
	}
	
	@RequestMapping(value="/suggestedUsers",method=RequestMethod.GET)
	public ResponseEntity<?> suggestedUsers(HttpSession session) {
		String email=(String)session.getAttribute("loginId");
		if(email==null) {
			ErrorClass error=new ErrorClass(5,"Unauthorized access");
			return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);
			
		}
		List<User> suggestedUsers=friendDAO.suggestedUsers(email);
		return new ResponseEntity<List<User>>(suggestedUsers,HttpStatus.OK);		
	}

}
