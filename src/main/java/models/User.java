package models;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity(name = User.PERSISTANCE_NAME)
@Table(name = User.PERSISTANCE_NAME )
@NamedQuery(name = "users.findByUsername", query = "from USER_TABLE where login = :username")
@NamedQuery(name = "users.findByToken", query = "from USER_TABLE where accessToken = :accessToken")
public class User {
    static final String PERSISTANCE_NAME = "USER_TABLE";
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "id_Sequence")
    @SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
    private Long id;

    @Column(name = "login", unique = true)
    private String login;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Entry> getMyEntries() {
        return myEntries;
    }

    public void setMyEntries(List<Entry> myEntries) {
        this.myEntries = myEntries;
    }

    @Column(name = "password")
    private String password;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String access_token) {
        this.accessToken = access_token;
    }

    public void generateAccessToken(){
        this.setAccessToken(UUID.randomUUID().toString());
    }

    @Column(name = "access_token")
    private String accessToken;

    @OneToMany(mappedBy="owner")
    private List<Entry> myEntries;
}
