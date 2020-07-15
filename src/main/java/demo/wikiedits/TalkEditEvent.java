package demo.wikiedits;

import java.io.Serializable;

public class TalkEditEvent implements Serializable {

    private String id;
    private String title;
    private String user;
    private String diffUrl;
    private String summary;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDiffUrl() {
        return diffUrl;
    }

    public void setDiffUrl(String diffUrl) {
        this.diffUrl = diffUrl;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return "TalkEditEvent{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", user='" + user + '\'' +
                ", diffUrl='" + diffUrl + '\'' +
                ", summary='" + summary + '\'' +
                '}';
    }
}
