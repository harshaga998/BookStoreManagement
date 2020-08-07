package controllers;

import models.BookDto;
import models.BooksDto;
import models.PushedTestReportDto;
import models.User;
import models.UserDto;
import models.Book;
import ninja.Context;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;
import ninja.SecureFilter;
import ninja.params.Param;
import ninja.params.PathParam;
import ninja.session.Session;
import ninja.uploads.DiskFileItemProvider;
import ninja.uploads.FileItem;
import ninja.uploads.FileProvider;
import ninja.uploads.MemoryFileItemProvider;
import ninja.validation.JSR303Validation;
import ninja.validation.Validation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.io.IOUtils;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

import dao.BookDao;
import dao.UserDao;
import etc.LoggedInUser;

public class BookController {

    @Inject
    BookDao bookDao;
    @Inject
    UserDao userDao;
    LoginLogoutController controller = new LoginLogoutController();

    public Result savePushedReports(@JSR303Validation PushedTestReportDto pushDto) {
//    	BookDto sampleBook = new BookDto();
//    	sampleBook.book_price = "20";
//    	sampleBook.book_title = "Harsh Sample Book";
//    	bookDao.postBook("Bob", sampleBook);
    	return Results.json().render("Successful");
    }
    
    
    public Result getbooksJson() {

      BooksDto booksDto = bookDao.getAllBooks();

        return Results.json().render(booksDto);

    }
    @FilterWith(SecureFilter.class)
    public Result bookNewPost(@LoggedInUser String username,
                                 Context context,
                                 @JSR303Validation BookDto bookDto,
                                 Validation validation) {

        if (validation.hasViolations()) {

            context.getFlashScope().error("Please correct field.");
            context.getFlashScope().put("title", bookDto.book_title);
            context.getFlashScope().put("price", bookDto.book_price);
            return Results.redirect("/article/new");

        }else {
            
            bookDao.postBook(username, bookDto);
            
            context.getFlashScope().success("New book added.");
            
            return Results.redirect("/");

        }

    }
    
    public Result deleted(@PathParam("id") Long id,Context context) {


        if (id != null && controller.temp!=0L) {

            bookDao.deleteBook(id);
            return Results.redirect("/");
        }else {
        	context.getFlashScope().error("Please login to delete or update the content!");
        	return Results.redirect("/article/"+id);
        }

       

    }
    
    
    public Result bookShow(@PathParam("id") Long id) {

        Book book = null;

        if (id != null) {

            book = bookDao.getBook(id);

        }

        return Results.html().render("book", book);

    }
     
    public Result ssignup(Context context) {

        return Results.html();

    }
    
    public Result signupNew(@Param("username")@LoggedInUser String username,
                                 Context context,
                                 @JSR303Validation UserDto userDto,
                                 Validation validation,@Param("fullname") String fullname,@Param("password") String password) {

        if (validation.hasViolations()) {
            context.getFlashScope().error("Please correct field.");
            context.getFlashScope().put("username", userDto.username);
            context.getFlashScope().put("password", userDto.password);
            context.getFlashScope().put("fullname", userDto.fullname);
            return Results.redirect("/signup");

        }
        boolean t = bookDao.isalreadyUser(username, fullname,password);
        boolean a=   bookDao.newsignup(username, userDto);
        if(t) {
        	context.getFlashScope().error("User Already There!");
        	return Results.redirect("/signup");
        }else {
   	 User user=  bookDao.currentuser(userDto.username);  
   	 controller.cuser=user.id;
   	  controller.temp=controller.cuser;
       	 if (a) {
                Session session = context.getSession();
                session.put("username", userDto.username);

                context.getFlashScope().success("SignUp Successful");
                controller.flag = 1;
                return Results.redirect("/");
            } else {

                // something is wrong with the input or password not found.
                context.getFlashScope().put("username", userDto.username);
              //  context.getFlashScope().put("rememberMe", String.valueOf(rememberMe));
                context.getFlashScope().error("login.errorLogin");

                return Results.redirect("/signup");

        }
        }

    }
    
    // UPDATE BOOK
	 @FilterWith(SecureFilter.class)
	    public Result updatedbook(@PathParam("id") Long id,@LoggedInUser String username,
	                                 Context context,
	                                 @JSR303Validation BookDto bookDto,
	                                 Validation validation) {

	     /*   if (validation.hasViolations()) {

	            context.getFlashScope().error("Please correct field.");
	            context.getFlashScope().put("name", eventDto.name);
	            context.getFlashScope().put("eventmanager", eventDto.eventmanager);
	            context.getFlashScope().put("eventdate", eventDto.eventdate);
	            context.getFlashScope().put("eventtype", eventDto.eventtype);
	            context.getFlashScope().put("location", eventDto.location);
	            return Results.redirect("/Update_event");

	        }else {*/
	            if(id != null && controller.temp!=0L) {
	            bookDao.updateBook(id,username, bookDto);
	            context.getFlashScope().success("Book Updated");
	            return Results.redirect("/");
	            }
	            else {
	            	context.getFlashScope().error("Login to update the book details");
	            	return Results.redirect("/");
	            }
	        }
	        
	 @FilterWith(SecureFilter.class)
	    public Result update(@PathParam("id") Long id) {
	        Book book = null;
	        if (id != null) {
	            book = bookDao.getBook(id);
	        }
	        return Results.html().render("book", book);
	    }
	 
	 public Result upload(Context context) {
	    	return Results.html();
	    }
	 
	 @FileProvider(DiskFileItemProvider.class)
	 public Result uploadFinish(Context context) throws Exception {

		    // Make sure the context really is a multipart context...
		    if (context.isMultipart()) {
		    		
		        // This is the iterator we can use to iterate over the
		        // contents of the request.
		        FileItemIterator fileItemIterator = context
		                .getFileItemIterator();
		       
		        
		        while (fileItemIterator.hasNext()) {
		        	FileItemStream item = fileItemIterator.next();
		        	int flag = 0;
		            String name = item.getName();
		            if(name.contains(".pdf") || name.contains(".txt")) {
		            	 flag = 1;
		            }
		            InputStream stream = item.openStream();
		            String contentType = item.getContentType();
//		            byte[] buffer = new byte[stream.available()];
		            byte[] buffer = IOUtils.toByteArray(stream);
		            stream.read(buffer);
		            File targetFile;
		            if(flag ==0) {
		            	targetFile = new File("C:\\Users\\Myanatomy\\event\\src\\main\\java\\assets\\images\\"+name);
		            }else {
		            	targetFile = new File("C:\\Users\\Myanatomy\\event\\src\\main\\java\\assets\\text\\"+name);
		            }
		            OutputStream outStream = new FileOutputStream(targetFile);
		            outStream.write(buffer);
		            outStream.close();
		           
		            if(flag ==0) {
		            	 context.getFlashScope().success("Image Uploaded");
		            	 return Results.ok().render("file",item);
		            }else {
		            	 context.getFlashScope().success("File Uploaded");
		            	return Results.ok().render("flag",flag).render("name1", name);
		            }
		        }
		       
		    }
		    
		    // We always return ok. You don't want to do that in production ;)
		   
		    return Results.ok();
		}
    }
