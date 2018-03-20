package dao;

import domain.User;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.*;

import java.util.List;

import static junit.framework.Assert.*;
import static org.junit.Assert.assertNotEquals;

public class JpaUserTests {
    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;
    private static IKweetDao kweetDao;
    private static IUserDao userDao;
    private EntityTransaction transaction;

    @BeforeClass
    public static void setUpBeforeClass() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpa-example");
        entityManager = entityManagerFactory.createEntityManager();
        kweetDao = new JpaKweetDao(entityManager);
        userDao = new JpaUserDao(entityManager);
    }

    @Before
    public void setUp() {
        transaction = entityManager.getTransaction();

        //cleanup old data in the database
        transaction.begin();
        Query cleanKweets = entityManager.createQuery("DELETE FROM Kweet");
        cleanKweets.executeUpdate();
        Query cleanUsers = entityManager.createQuery("DELETE FROM User");
        cleanUsers.executeUpdate();
        transaction.commit();
    }

    private User AddUser()
    {
        User user = new User("username" + userDao.All().size(), "password", User.Role.USER);
        return userDao.Create(user);
    }

    @Test
    public void CreateUser() {
        transaction.begin();

        User user = new User("username" + userDao.All().size(), "password", User.Role.USER);
        User created = userDao.Create(user);
        assertNotNull(created);

        transaction.commit();
    }

    @Test
    public void ReadUser() {
        transaction.begin();

        User newUser = AddUser();

        User found = userDao.Read(newUser.getId());
        assertNotNull(found);

        User notFound = userDao.Read(100);
        assertNull(notFound);

        transaction.commit();
    }

    @Test
    public void FindUser() {
        transaction.begin();

        userDao.Create(new User("username", "password", User.Role.USER));

        User found = userDao.FindUser("username");
        assertNotNull(found);

        User notFound = userDao.FindUser("unknown");
        assertNull(notFound);

        transaction.commit();
    }

    @Test
    public void UpdateUser() {
        transaction.begin();

        User newUser = AddUser();

        User user = userDao.Read(newUser.getId());
        String bio = "Updated bio";
        user.setBio(bio);
        User updated = userDao.Update(user);
        assertNotNull(updated);
        assertEquals(bio, updated.getBio());

        transaction.commit();
    }

    @Test
    public void AllUsers() {
        transaction.begin();

        AddUser();
        AddUser();

        List<User> foundUsers = userDao.All();
        assertEquals(2, foundUsers.size());

        transaction.commit();
    }

    @Test
    public void DeleteUser() {
        transaction.begin();

        userDao.Create(new User("username", "password", User.Role.USER));

        int initialLength = userDao.All().size();
        User user = userDao.FindUser("username");
        userDao.Delete(user.getId());

        int newLength = userDao.All().size();
        assertNotEquals(initialLength, newLength);

        transaction.commit();
    }
}
