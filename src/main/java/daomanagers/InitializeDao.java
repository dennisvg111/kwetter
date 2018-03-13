package daomanagers;


import domain.Kweet;
import domain.User;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.Date;

@Singleton
@javax.ejb.Startup
public class InitializeDao {
    @Inject
    private UserManager userManager;
    @Inject
    private KweetManager kweetManager;

    @PostConstruct
    public void initData() {
        try {
            User normalUser = new User("user", "password", User.Role.USER);
            //possible add some kweets here as well

            userManager.AddUser(normalUser);
            Kweet kweet = new Kweet();
            kweet.setUser(normalUser);
            kweet.setDate(new Date());
            kweet.setContent("Hello Kwetter!");
            kweetManager.AddKweet(kweet);

            userManager.AddUser(new User("admin", "md5IsForLosers", User.Role.ADMINISTRATOR));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
