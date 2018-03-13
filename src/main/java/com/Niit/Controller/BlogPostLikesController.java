package com.Niit.Controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.Niit.Dao.BlogPostLikesDAO;
import com.Niit.model.BlogPost;
import com.Niit.model.BlogPostLikes;
import com.Niit.model.ErrorClass;

@Controller
public class BlogPostLikesController {

	@Autowired
	private BlogPostLikesDAO blogPostLikesDAO;
	
	public BlogPostLikesController(){
		System.out.println("BlogPostLikesController");
	}
	
	@RequestMapping(value="/hasuserlikedblog/{blogId}",method=RequestMethod.GET)
	public ResponseEntity<?> hasUserLikedBlog(@PathVariable int blogId,HttpSession session)
	{
		String email=(String)session.getAttribute("loginId");
		if(email==null)
		{
			ErrorClass error=new ErrorClass(5,"Unauthorized access");
			return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);
		}
		BlogPostLikes blogPostLikes=blogPostLikesDAO.hasUserLikedBlog(blogId, email);
		return new ResponseEntity<BlogPostLikes>(blogPostLikes,HttpStatus.OK);
	}
	
	@RequestMapping(value="/updateLikes/{id}",method=RequestMethod.PUT)
	public ResponseEntity<?> updateLikes(@PathVariable int id,HttpSession session)
	{
		String email=(String)session.getAttribute("loginId");
		if(email==null)
		{
			ErrorClass error=new ErrorClass(5,"Unauthorized access");
			return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);
		}
		BlogPost blogPost=blogPostLikesDAO.updateLikes(id, email);
		return new ResponseEntity<BlogPost>(blogPost,HttpStatus.OK);
	}
}
