public class User {
    private String username;
    private String password;
    private Boolean session;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.session = false;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getSession() {
        return this.session;
    }

    public void setSession(Boolean session) {
        this.session = session;
    }
}
