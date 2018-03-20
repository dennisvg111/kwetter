package dao;

import domain.Kweet;
import domain.User;

import javax.persistence.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.*;

public class JpaKweetTests {
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

    private Kweet addKweet() {
        User user = new User("username" + userDao.All().size(), "password", User.Role.USER);
        userDao.Create(user);

        Kweet kweet = new Kweet(user, "Hello testing world #Kwetter");
        return kweetDao.Create(kweet);
    }

    @Test
    public void CreateKweet() {
        transaction.begin();

        assertNotNull(addKweet());

        transaction.commit();
    }


    @Test
    public void ReadKweet() {
        transaction.begin();

        Kweet newKweet = addKweet();

        Kweet found = kweetDao.Read(newKweet.getId());
        assertNotNull(found);

        Kweet notFound = kweetDao.Read(100);
        assertNull(notFound);

        transaction.commit();
    }

    @Test
    public void UpdateKweet() {
        transaction.begin();

        Kweet newKweet = addKweet();

        Kweet kweet = kweetDao.Read(newKweet.getId());
        String text = "Updated text";
        kweet.setContent(text);
        Kweet updated = kweetDao.Update(kweet);
        assertNotNull(updated);
        assertEquals(text, updated.getContent());

        transaction.commit();
    }

    @Test
    public void AllKweets() {
        transaction.begin();

        addKweet();
        addKweet();

        List<Kweet> foundKweets = kweetDao.All();
        assertEquals(2, foundKweets.size());

        transaction.commit();
    }

    @Test
    public void FindKweets() {
        transaction.begin();

        addKweet();

        List<Kweet> kweetsFound = kweetDao.FindKweets("hello");
        assertEquals(1, kweetsFound.size());

        List<Kweet> kweetsNotFound = kweetDao.FindKweets("unknown");
        assertEquals(0, kweetsNotFound.size());

        transaction.commit();
    }

    @Test
    public void GetKweetsFromUser() {
        transaction.begin();

        Kweet newKweet = addKweet();

        List<Kweet> kweetsFound = kweetDao.GetKweetsFromUser(newKweet.getUser().getId());
        assertEquals(1, kweetsFound.size());

        List<Kweet> kweetsNotFound = kweetDao.GetKweetsFromUser(100);
        assertEquals(0, kweetsNotFound.size());

        transaction.commit();
    }
}
