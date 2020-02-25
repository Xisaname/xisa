package life.majiang.community.community.model;

public class User {
    private Integer id;
    private String name;
    private String accountId;
    private Long gmtCreate;
    private String token;
    private Long gmtModified;

    public User(Integer id, String name, String accountId, Long gmtCreate, String token, Long gmtModified) {
        this.id = id;
        this.name = name;
        this.accountId = accountId;
        this.gmtCreate = gmtCreate;
        this.token = token;
        this.gmtModified = gmtModified;
    }

    public Long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Long getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Long gmtModified) {
        this.gmtModified = gmtModified;
    }
}
