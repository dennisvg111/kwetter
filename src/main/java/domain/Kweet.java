package domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

@Entity
@XmlRootElement
public class Kweet implements Serializable, Comparable<Kweet> {
    @GeneratedValue
    @Id
    private long id;
    private String content;
    private Date date;
    @ManyToOne
    private User user;

    public Kweet() {}

    public Kweet(User user, String content) {
        this.content = content;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Transient
    public HateLink[] getLinks()
    {
        return new HateLink[] {
                new HateLink("/11/api/kweets/" + this.id, "self", "GET"),
                new HateLink("/11/api/kweets/" + this.id + "/remove", "remove", "DELETE"),
                new HateLink("/11/api/kweets", "edit", "PUT"),
                new HateLink("/11/api/users/" + this.user.getId(), "user", "GET"),
        };
    }

    @Override
    public int compareTo(Kweet o) {
        return this.getDate().compareTo(o.getDate()) * -1;
    }
}