package dao;

import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import models.Article;
import models.ArticleDto;
import models.ArticlesDto;
import models.BookDto;
import models.Book;
import models.BookDto;
import models.BooksDto;
import models.User;
import models.UserDto;
import ninja.jpa.UnitOfWork;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

import controllers.LoginLogoutController;

public class BookDao {
	@Inject UserDao userDao;
    @Inject
    Provider<EntityManager> entitiyManagerProvider;
    
    LoginLogoutController newu=new LoginLogoutController();
    

    static int x = 0;
    @UnitOfWork
    public BooksDto getAllBooks() {
        
        EntityManager entityManager = entitiyManagerProvider.get();
        
        TypedQuery<Book> q = entityManager.createQuery("SELECT x FROM Book x", Book.class);
        List<Book> books = q.getResultList();        

        BooksDto booksDto = new BooksDto();
        booksDto.books = books;
        
        return booksDto;
        
    }
    
    @Transactional
    public boolean postBook(String username, BookDto bookDto) {
        
        EntityManager entityManager = entitiyManagerProvider.get();
        
        TypedQuery<User> q = entityManager.createQuery("SELECT x FROM User x WHERE username = :usernameParam", User.class);
        User user = q.setParameter("usernameParam", username).getSingleResult();
        
        if (user == null) {
            return false;
        }
        
        Book book = new Book(user, bookDto.book_title, bookDto.book_price);
        entityManager.persist(book);
        
        return true;
        
    }
    
    @Inject
    Provider<EntityManager> entityManagerProvider;
    
    @Transactional
    public boolean newsignup(String username, UserDto userDto) {
        
    	
            EntityManager entityManager = entityManagerProvider.get();
        	System.out.println("###########################");
        	System.out.println("Value of X: "+x);
        	System.out.println("###########################");
            if(x==0){
            	User u = new User(userDto.username,userDto.password, userDto.fullname);
            	entityManager.persist(u);
            	return true;
            }else {
            	return false;
            }
           
    }

    
    @UnitOfWork
    public List<Book> getFirstBookForFrontPage() {
        
        EntityManager entityManager = entitiyManagerProvider.get();
        
        TypedQuery<Book> q = entityManager.createQuery("SELECT x FROM Book x WHERE x.id IN (SELECT c.Book_id FROM Book_authorids c WHERE c.authorIds =:idd) ", Book.class);
        List<Book> book = q.setParameter("idd",newu.getcuser()).setMaxResults(2).getResultList();      
        
        return book;
        
        
    }
    
    @UnitOfWork
    public List<Book> getOlderBooksForFrontPage() {
        
        EntityManager entityManager = entitiyManagerProvider.get();
        
        TypedQuery<Book> q = entityManager.createQuery("SELECT x FROM Book x WHERE x.id IN (SELECT c.Book_id FROM Book_authorids c WHERE c.authorIds =:idd) ORDER BY x.id DESC ", Book.class);
        List<Book> books = q.setParameter("idd",newu.getcuser()).setFirstResult(0).setMaxResults(100).getResultList();            
            
        
        return books;
        
        
    }
    @UnitOfWork
    public Book getBook(Long id) {
        
        EntityManager entityManager = entitiyManagerProvider.get();
        
        TypedQuery<Book> q = entityManager.createQuery("SELECT x FROM Book x WHERE x.id = :idParam", Book.class);
        Book book = q.setParameter("idParam", id).getSingleResult();        
        
        return book;
        
        
    }
     
    @UnitOfWork
    public void deleteBook(Long id) {
        
        EntityManager entityManager = entitiyManagerProvider.get();
        
        TypedQuery<Book> q = entityManager.createQuery("SELECT x FROM Book x WHERE x.id = :idParam", Book.class);
        Book book = q.setParameter("idParam", id).getSingleResult();
        
        entityManager.getTransaction().begin();
        entityManager.remove(book);
        entityManager.getTransaction().commit();
        
        
    }
    
    @UnitOfWork
    public User currentuser(String username) {
        
        EntityManager entityManager = entitiyManagerProvider.get();
        
        TypedQuery<User> q = entityManager.createQuery("SELECT x FROM User x WHERE x.username = :nameParam", User.class);
        User user = q.setParameter("nameParam", username).getSingleResult();        
        
        return user;

    }
    
    // UPDATE

    @Transactional
    public boolean updateBook(Long id ,String username, BookDto bookDto) {
        
        EntityManager entityManager = entitiyManagerProvider.get();
        
        TypedQuery<User> q = entityManager.createQuery("SELECT x FROM User x WHERE username = :usernameParam", User.class);
        User user = q.setParameter("usernameParam", username).getSingleResult();
        
        if (user == null) {
            return false;
        }
        Query query = entityManager.createQuery("UPDATE Book  x SET x.book_title='"+ bookDto.book_title + "'  WHERE x.id ='" + id +"'");
      query.executeUpdate();
      Query query1 = entityManager.createQuery("UPDATE Book  x SET x.book_price='"+ bookDto.book_price + "'  WHERE x.id ='" + id +"'");
      query1.executeUpdate();
      
        
        
        return true;
        
    }
    
    @UnitOfWork
    public boolean isalreadyUser(String username, String fullname,String password) {
        
        if (username != null && fullname != null) {
            
            EntityManager entityManager = entityManagerProvider.get();
            
            TypedQuery<User> q = entityManager.createQuery("SELECT x FROM User x WHERE username = :usernameParam", User.class);
            User user = UserDao.getSingleResult(q.setParameter("usernameParam", username));

            if (user != null) {
                
                if (user.username.equals(username) && user.fullname.equals(fullname) || user.username.equals(username) || user.fullname.equals(fullname)) {
                	x = 1;
                    return true;
                }
                
            }
            

        }
        
        return false;
 
    }
 
}
    
    

