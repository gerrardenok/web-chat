package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;
import javax.persistence.*;

import system.UserRole;
import java.util.List;

@Entity
@Table(name = "users")
public abstract class User extends Model {

    @Id
    @Constraints.Email
    public String email;

    @Constraints.Required
    @Column(nullable = false)
    public String name;

    @Constraints.Required
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private UserRole role;

    @ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinTable(name="user_room",
            joinColumns={@JoinColumn(name="user_email", referencedColumnName="email")},
            inverseJoinColumns={@JoinColumn(name="room_id", referencedColumnName="id")})
    public List<ChatRoom> rooms;

    /*Confusing moment*/
    @OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    @OrderBy("id")
    public List<Message> messages;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public User(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = UserRole.USER;
    }

    public User(String email, String name, String password, UserRole role) {
        this(email, name, password);
        this.role = role;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static final Finder<String, User> find = new Finder<String, User>(String.class, User.class);

    public static User findByEmail(final Long id) {
        return find.where().eq("email", id).findUnique();
    }
}
