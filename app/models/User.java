package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;
import javax.persistence.*;

import system.UserRole;
import java.util.List;

import data.UserDTO;

/**
 * <p>
 * Represents the User entity.
 * </p>
 *
 * @author Mikhail Vatalev(m.vatalev@euroats.com)
 */

@Entity
@Table(name = "users")
public class User extends Model {

    @Id
    public String email;

    @Column(nullable = false)
    public String name;

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

    public User(){}

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public User(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = UserRole.USER;
    }

    public User(UserDTO user) {
        this(user.email, user.name, user.password);
    }


    public User(String email, String name, String password, UserRole role) {
        this(email, name, password);
        this.role = role;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static final Finder<String, User> find = new Finder<String, User>(String.class, User.class);

    public static User findByEmail(final String email) {
        return find.where().eq("email", email).findUnique();
    }

    public static User findByEmailAndPassword(final String email, final String password) {
        return find.where().eq("email", email).eq("password", password).findUnique();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public boolean isPasswordChecked(String password) {
        return this.password.equals(password);
    }

    public String getUserRole() {
        return role.name();
    }
}
