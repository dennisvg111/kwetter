package beans;

import daomanagers.KweetManager;
import domain.Kweet;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.core.util.StringUtils;
import domain.User;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@SessionScoped
public class KweetsBean implements Serializable {
    @Inject
    private KweetManager kweetManager;

    private List<Kweet> kweets;
    private Kweet kweet;
    private String selectedText;

    @PostConstruct
    public void init() {
        this.kweets = kweetManager.getKweets();
    }

    public void onTextCellEdit(CellEditEvent event) {
        String oldUsername = event.getOldValue().toString();
        String newUsername = event.getNewValue().toString();

        if (!oldUsername.equals(newUsername)) {
            Kweet entity = (Kweet) ((DataTable) event.getComponent()).getRowData();
            editKweetText(entity.getId(), newUsername);
        }
    }

    public void onUserRowSelect(User user) {
        //this.kweets = kweetManager.getKweets(user.getUsername());
    }

    public void editKweetText(long id, String content) {
        Kweet kweet = kweetManager.getKweet(id);

        if (kweet != null && !StringUtils.isNullOrEmpty(content)) {
            kweet.setContent(content);
            kweetManager.EditKweet(kweet);
            FacesMessage message = new FacesMessage("Kweet text updated to", content);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void onRemoveKweet(Kweet kweet) {
        this.kweetManager.RemoveKweet(kweet.getId());
        this.kweets.remove(kweet);
    }

    public List<Kweet> getKweets() {
        return kweets;
    }

    public void setKweets(List<Kweet> kweets) {
        this.kweets = kweets;
    }

    public Kweet getSelectedKweet() {
        return kweet;
    }

    public void setSelectedKweet(Kweet selectedKweet) {
        this.kweet = selectedKweet;
    }

    public String getSelectedText() {
        return selectedText;
    }

    public void setSelectedText(String selectedText) {
        this.selectedText = selectedText;
    }
}
