package daomanagers;


import dao.JPA;
import domain.Kweet;
import domain.Role;
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

    @Inject
    private RoleManager roleManager;

    @PostConstruct
    public void initData() {
        try {
            System.out.println("---------------------------------------------------------\r\nInitializing dao");
            System.out.println("---------------------------------------------------------\r\nCreating roles");
            Role normalRole = new Role("USER");
            Role adminRole = new Role("ADMINISTRATOR");
            Role modRole = new Role("MODERATOR");
            normalRole = roleManager.AddRole(normalRole);
            adminRole = roleManager.AddRole(adminRole);
            modRole = roleManager.AddRole(modRole);

            System.out.println("---------------------------------------------------------\r\nCreating user");

            User normalUser = new User("user", "password", normalRole);
            //possible add some kweets here as well
            User user = userManager.AddUser(normalUser);

            System.out.println("---------------------------------------------------------\r\nPosting kweets");
            Kweet kweet = new Kweet();
            kweet.setUser(user);
            kweet.setContent("Hello Kwetter!");
            kweetManager.AddKweet(kweet);

            kweet = new Kweet();
            kweet.setUser(user);
            kweet.setContent("Goodbye Kwetter!");
            kweetManager.AddKweet(kweet);

            System.out.println("---------------------------------------------------------\r\nCreating admin");

            User adminUser = new User("admin", "md5IsForLosers", new Role("ADMINISTRATOR"), new Role("MODERATOR"));
            User admin = userManager.AddUser(adminUser);

            System.out.println("---------------------------------------------------------\r\nFollow user");
            //userManager.Follow(admin.getId(), user.getId());

            System.out.println("---------------------------------------------------------\r\nFinished initialization");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
