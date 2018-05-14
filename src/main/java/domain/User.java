package domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dao.DaoIgnore;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Entity
@XmlRootElement
public class User implements Serializable {
    @Id
    @GeneratedValue
    private long id;

    @Column(unique=true)
    private String name;

    @JsonProperty(access = WRITE_ONLY)
    private String hashedPassword;
    private String bio;

    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(table = "user", name = "username", referencedColumnName = "name"),
            inverseJoinColumns = @JoinColumn(table = "role", name = "rolename", referencedColumnName = "name"))
    @LazyCollection(LazyCollectionOption.FALSE)
    @DaoIgnore
    private List<Role> roles = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.PERSIST, mappedBy = "following")
    @JsonIgnore
    private List<User> followers = new ArrayList<>();

    @ManyToMany
    @JsonIgnore
    private List<User> following = new ArrayList<>();

    private String location;
    private String website;
    private String profilePicture;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user")
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Kweet> kweets = new ArrayList<>();

    public User(String name, String hashedPassword, Role... roles) {
        this.name = name;
        this.hashedPassword = hashedPassword;
        this.roles = new ArrayList<>();
        for (Role role : roles)
        {
            this.roles.add(new Role(role.getName()));
        }
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

    public void RemoveFollowing(User otherUser) {
        for (User user : following)
        {
            if (user.getId() == otherUser.getId())
            {
                this.following.remove(user);
                user.followers.remove(this);
            }
        }
    }

    public boolean IsFollowing( User otherUser)
    {
        for (User user : following)
        {
            if (user.getId() == otherUser.getId())
            {
                return true;
            }
        }
        return false;
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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) { this.roles = roles; }

    public List<Kweet> getKweets() {
        return kweets;
    }

    public void setKweets(ArrayList<Kweet> kweets) {
        this.kweets = kweets;
    }



    @Transient
    public HateLink[] getLinks()
    {
        return new HateLink[] {
                new HateLink("/11/api/users/" + this.id, "self", "GET"),
                new HateLink("/11/api/users/" + this.id, "remove", "DELETE"),
                new HateLink("/11/api/users", "edit", "PUT"),
                new HateLink("/11/api/kweets/users/" + this.id, "kweets", "GET"),
                new HateLink("/11/api/kweets/feed/" + this.id, "feed", "GET"),
                new HateLink("/11/api/users/" + this.id + "/following", "following", "GET"),
                new HateLink("/11/api/users/" + this.id + "/followers", "followers", "GET")
        };
    }
}