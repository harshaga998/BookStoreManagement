package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity
public class Book_authorids {
	 @Id
	    @GeneratedValue(strategy=GenerationType.AUTO)
public long Book_id;
public long authorIds;


public Book_authorids() {}
}