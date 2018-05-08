package beans;

import daomanagers.UserManager;
import domain.Kweet;
import domain.Role;
import domain.User;
import org.primefaces.component.selectbooleancheckbox.SelectBooleanCheckbox;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
public class UsersBean implements Serializable {

    @Inject
    private UserManager userManager;

    @Inject
    private KweetsBean kweetsBean;

    private List<User> users;

    private User selectedUser;

    private List<Role> selectedRoles;

    @PostConstruct
    public void init() {
        this.users = userManager.GetUsers();
        this.selectedRoles = new ArrayList<>();
    }

    public void onSaveUser() {
        FacesMessage message;

        if (selectedUser == null) {
            message = new FacesMessage("Please select an user to update", "");
        } else if (selectedRoles.isEmpty()) {
            message = new FacesMessage("Please select any user group before updating", selectedUser.getName());
        } else {
            userManager.SetRoles(selectedUser, selectedRoles.toArray(new Role[selectedRoles.size()]));
            this.users = userManager.GetUsers();
            return;
        }

        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void onAddUser(String username, String password) {
        User user = new User(username, password, new Role("USER"));
        User createdUser = userManager.AddUser(user);

        if (createdUser != null) {
            this.users.add(createdUser);
        }

        FacesMessage message = new FacesMessage(createdUser == null ? "Username already in use" : "Added user", username);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void selectUser(SelectEvent event) {
        User user = ((User) event.getObject());
        this.setSelectedUser(user);
        kweetsBean.onUserRowSelect(user);
    }

    public void onUserGroupEdit(AjaxBehaviorEvent event) {
        String role = (String) event.getComponent().getAttributes().get("role");
        boolean value = ((SelectBooleanCheckbox) event.getSource()).isSelected();

        if (value) {
            addSelectedRole(role);
        } else {
            removeSelectedRole(role);
        }
    }

    public void onRemoveUser(User user) {
        users.remove(user);

        if (selectedUser != null && user.getName().equals(selectedUser.getName())) {
            kweetsBean.setKweets(new ArrayList<Kweet>());
        }

        userManager.RemoveUser(user.getId());
    }

    public String getUsername()
    {
        if (selectedUser == null) { return null; }
        return selectedUser.getName();
    }

    public void setUsername(String name)
    {
        if (selectedUser == null) { return; }
        selectedUser.setName(name);
        userManager.EditUser(selectedUser);
    }

    public void setPassword(String password)
    {
        if (selectedUser == null) { return; }
        selectedUser.setHashedPassword(password);
        userManager.EditUser(selectedUser);
    }

    public String getPassword() {
        if (selectedUser == null) { return null; }
        return selectedUser.getHashedPassword();
    }

    public boolean getUserRole() {
        if (selectedUser == null) { return false; }
        return selectedUser.getRoles().stream().anyMatch(r -> r.getName() == "USER");
    }

    public void setUserRole(boolean hasRole) {
        removeSelectedRole("USER");
        if (hasRole)
        {
            addSelectedRole("USER");
        }
    }

    public boolean getModeratorRole() {
        if (selectedUser == null) { return false; }
        return selectedUser.getRoles().stream().anyMatch(r -> r.getName() == "MODERATOR");
    }

    public void setModeratorRole(boolean hasRole) {
        removeSelectedRole("MODERATOR");
        if (hasRole)
        {
            addSelectedRole("MODERATOR");
        }
    }

    public boolean getAdministratorRole() {
        if (selectedUser == null) { return false; }
        return selectedUser.getRoles().stream().anyMatch(r -> r.getName() == "ADMINISTRATOR");
    }

    public void setAdministratorRole(boolean hasRole)
    {
        removeSelectedRole("ADMINISTRATOR");
        if (hasRole)
        {
            addSelectedRole("ADMINISTRATOR");
        }
    }


    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public void addSelectedRole(String roleName) {
        this.selectedRoles.add(new Role(roleName));
    }

    public void removeSelectedRole(String roleName) {
        for (int i = 0; i < this.selectedRoles.size(); i++)
        {
            Role role = this.selectedRoles.get(i);
            if (role.getName().equals(roleName)) {
                this.selectedRoles.remove(i);
                break;
            }
        }
    }
}
