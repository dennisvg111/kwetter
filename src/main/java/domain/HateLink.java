package domain;

public class HateLink {
    private String link;
    private String relation;
    private String httpVerb;

    public HateLink(String link, String relation, String httpVerb) {
        this.link = link;
        this.relation = relation;
        this.httpVerb = httpVerb;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getHttpVerb() {
        return httpVerb;
    }

    public void setHttpVerb(String httpVerb) {
        this.httpVerb = httpVerb;
    }
}
