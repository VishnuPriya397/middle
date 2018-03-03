package com.Niit.Controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.Niit.Dao.BlogPostDAO;
import com.Niit.Dao.UserDAO;
import com.Niit.model.BlogPost;
import com.Niit.model.ErrorClass;
import com.Niit.model.User;

@Controller
public class BlogPostController {
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private BlogPostDAO blogPostDAO;
	
	public BlogPostController(){
		System.out.println("BlogPostController");
	}
	
	@RequestMapping(value="/addblogpost",method=RequestMethod.POST)
	public ResponseEntity<?> addBlogPost(@RequestBody BlogPost blogPost,HttpSession session)
	{
		String email=(String)session.getAttribute("loginId");
		if(email==null)
		{
			ErrorClass error=new ErrorClass(5,"Unauthorized access");
			return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);
		}
		blogPost.setPostedOn(new Date());
		User postedBy=userDAO.getUser(email);
		blogPost.setPostedBy(postedBy);
		try {
			blogPostDAO.addBlogPost(blogPost);
			return new ResponseEntity<BlogPost>(blogPost,HttpStatus.OK);
		} catch(Exception e) {
			ErrorClass error=new ErrorClass(6,"unable to post blog.."+e.getMessage());
			return new ResponseEntity<ErrorClass>(error,HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
	}
	
	@RequestMapping(value="/getblogs/{approved}",method=RequestMethod.GET)
	public ResponseEntity<?> getAllBlogs(@PathVariable int approved,HttpSession session)
	{
		String email=(String)session.getAttribute("loginId");
		if(email==null)
		{
			ErrorClass error=new ErrorClass(5,"Unauthorized access");
			return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);
		}
		if(approved==0) {
			User user=userDAO.getUser(email);
			if(!user.getRole().equals("ADMIN")) {
				ErrorClass error=new ErrorClass(7,"Access Denied");
				return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);
				
			}
}
	List<BlogPost> blogs=blogPostDAO.listOfBlogs(approved);
	return new ResponseEntity<List<BlogPost>>(blogs,HttpStatus.OK);
}
	@RequestMapping(value="/getblog/{id}",method=RequestMethod.GET)
	public ResponseEntity<?> getBlog(@PathVariable int id,HttpSession session) {
		String email=(String)session.getAttribute("loginId");
		if(email==null) {
			ErrorClass error=new ErrorClass(5,"Unauthorized access");
			return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);
		}
		BlogPost blogPost=blogPostDAO.getBlog(id);
		return new ResponseEntity<BlogPost>(blogPost,HttpStatus.OK);
	}

}
