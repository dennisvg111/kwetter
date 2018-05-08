package dao;

import domain.Kweet;
import domain.Role;
import domain.User;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

public class CollectionKweetTests {

    private static IKweetDao kweetDao;
    private static IUserDao userDao;

    @BeforeClass
    public static void setUpBeforeClass() {
    }

    @Before
    public void setUp() {
        kweetDao = new CollectionKweetDao();
        userDao = new CollectionUserDao();
    }

    private Kweet addKweet() {
        User user = new User("username" + userDao.All().size(), "password", new Role("User"));
        user.setId(userDao.All().size());
        userDao.Create(user);

        Kweet kweet = new Kweet(user, "Hello testing world #Kwetter");
        return kweetDao.Create(kweet);
    }

    @Test
    public void CreateKweet() {
        assertNotNull(addKweet());
    }


    @Test
    public void ReadKweet() {
        Kweet newKweet = addKweet();

        Kweet found = kweetDao.Read(newKweet.getId());
        assertNotNull(found);

        Kweet notFound = kweetDao.Read(100);
        assertNull(notFound);
    }

    @Test
    public void UpdateKweet() {
        Kweet newKweet = addKweet();

        Kweet kweet = kweetDao.Read(newKweet.getId());
        String text = "Updated text";
        kweet.setContent(text);
        Kweet updated = kweetDao.Update(kweet);
        assertNotNull(updated);
        assertEquals(text, updated.getContent());
    }

    @Test
    public void AllKweets() {

        addKweet();
        addKweet();

        List<Kweet> foundKweets = kweetDao.All();
        assertEquals(2, foundKweets.size());

    }

    @Test
    public void FindKweets() {

        addKweet();

        List<Kweet> kweetsFound = kweetDao.FindKweets("hello");
        assertEquals(1, kweetsFound.size());

        List<Kweet> kweetsNotFound = kweetDao.FindKweets("unknown");
        assertEquals(0, kweetsNotFound.size());

    }

    @Test
    public void GetKweetsFromUser() {

        Kweet newKweet = addKweet();

        List<Kweet> kweetsFound = kweetDao.GetKweetsFromUser(newKweet.getUser().getId());
        assertEquals(1, kweetsFound.size());

        List<Kweet> kweetsNotFound = kweetDao.GetKweetsFromUser(100);
        assertEquals(0, kweetsNotFound.size());

    }
}
