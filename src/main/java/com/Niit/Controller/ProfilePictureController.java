package com.Niit.Controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.Niit.Dao.NotificationDAO;
import com.Niit.Dao.ProfilePictureDAO;
import com.Niit.model.ErrorClass;
import com.Niit.model.ProfilePicture;

@Controller
public class ProfilePictureController {
	
	@Autowired
	private ProfilePictureDAO profilePictureDAO;
	
	public ProfilePictureController(){
		System.out.println("ProfilePictureController");
	}
	
	@RequestMapping(value="/uploadprofile",method=RequestMethod.POST)
	public ResponseEntity<?> uploadProfilePicture(@RequestParam CommonsMultipartFile image,HttpSession session){
		String email=(String)session.getAttribute("loginId");
		if(email==null) {
			ErrorClass error=new ErrorClass(6,"Unauthorized access");
			return new ResponseEntity<ErrorClass>(error,HttpStatus.OK);
			
		}
		ProfilePicture profilePicture=new ProfilePicture();
		profilePicture.setEmail(email);
		profilePicture.setImage(image.getBytes());
		profilePictureDAO.uploadProfilePicture(profilePicture);
		
		return new ResponseEntity<ProfilePicture>(profilePicture,HttpStatus.OK);
			
	}
	@RequestMapping(value="/getimage/{email:.+}",method=RequestMethod.GET)
	public @ResponseBody byte[] getImage(@PathVariable String email,HttpSession session){
		System.out.println(email);
		String auth=(String)session.getAttribute("loginId");
		if(auth==null) {
			return null;
		}
		ProfilePicture profilePicture=profilePictureDAO.getImage(email);
		if(profilePicture==null) {
			return null;
		}
		else
			return profilePicture.getImage();
		}
}
