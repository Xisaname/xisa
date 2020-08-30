package life.majiang.community.community.model;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String name;
    private String accountId;
    private Long gmtCreate;
    private String token;
    private Long gmtModified;
    private String bio;
    private String avatarUrl;
}
