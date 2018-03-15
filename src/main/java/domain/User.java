package domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@Entity
@XmlRootElement
public class User implements Serializable {
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String hashedPassword;
    private String bio;

    @ManyToMany(cascade = CascadeType.PERSIST, mappedBy = "following")
    @JsonIgnore
    private List<User> followers = new ArrayList<>();

    @ManyToMany
    @JsonIgnore
    private List<User> following = new ArrayList<>();

    private String location;
    private String website;
    private String profilePicture;
    private Role role;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user")
    private List<Kweet> kweets = new ArrayList<>();

    public enum Role {
        USER,
        ADMINISTRATOR
    }

    public User(String name, String hashedPassword, Role role) {
        this.name = name;
        this.hashedPassword = hashedPassword;
        this.role = role;
    }

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<User> followers) {
        this.followers = followers;
    }

    public void addFollowing(User otherUser)
    {
        if (!this.following.contains(otherUser))
        {
            this.following.add(otherUser);
            otherUser.followers.add(this);
        }
    }

    public List<User> getFollowing() {
        return following;
    }

    public void setFollowing(ArrayList<User> following) {
        this.following = following;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Kweet> getKweets() {
        return kweets;
    }

    public void setKweets(ArrayList<Kweet> kweets) {
        this.kweets = kweets;
    }
}