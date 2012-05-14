package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;

@Entity
@NamedNativeQuery(name = "findPosts", query = "select p.*,u.* from post p, user u" +
		" where p.author_id=u.id order by p.postedAt desc", resultClass = Post.class)
public class Post extends Model {

	public String title;
	public Date postedAt;

	@Lob
	public String content;

	@ManyToOne
	public User author;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	public List<Comment> comments;

	public Post(User author, String title, String content) {
		this.comments = new ArrayList<Comment>();
		this.author = author;
		this.title = title;
		this.content = content;
		this.postedAt = new Date();
	}

	public Post addComment(String author, String content) {
		Comment newComment = new Comment(this, author, content).save();
		this.comments.add(newComment);
		this.save();
		return this;
	}

	public static List<Post> findPosts(){
		return em().createNamedQuery("findPosts").getResultList();
	}
}