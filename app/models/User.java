package models;

import play.Logger;
import play.db.ebean.Model;
import javax.persistence.*;

import system.UserRole;
import start.RootUserSettings;

import java.util.ArrayList;
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

    public List<UserRoomRelationship> relationships = new ArrayList<>();

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @OneToMany(targetEntity = UserRoomRelationship.class, mappedBy = "user", cascade = CascadeType.ALL)
    public List<UserRoomRelationship> getRelationships() {
        return this.relationships;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public User(){}

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

    public static List<User> findAdmins() {
        return find.where().eq("role", UserRole.SYSTEM_ADMIN).findList();
    }

    public static User findRoot() {
        return find.where().eq("email", RootUserSettings.EMAIL).findUnique();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public String getUserRole() {
        return role.name();
    }

    public List<Room> getRooms() {
        List<Room> rooms = new ArrayList<>();
        for(UserRoomRelationship relationship : relationships) {
            rooms.add(relationship.room);
        }
        return rooms;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public boolean isPasswordChecked(String password) {
        return this.password.equals(password);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public void joinToChatRoom(Room room) {
        // create new relationship
        UserRoomRelationship relationship = new UserRoomRelationship(this, room);
        //User join to room
        this.getRelationships().add(relationship);
        this.update();

        Logger.info(String.format("%s join to room %s ", this, room));
    }

    public void sendMessage(Room to, String message) {
        to.getMessages().add(new Message(message, this, to));
        to.save();
        Logger.info(String.format("%s send message to %s", this, to));
    }

    public void sendMessage(Room to, Message message) {
        to.getMessages().add(message);
        to.save();
        Logger.info(String.format("%s send message to %s", this, to));
    }

    public static User createAdmin(String email, String name, String password) {
        // create user
        User admin = new User();
        admin.email = email;
        admin.name = name;
        admin.password = password;
        admin.role = UserRole.SYSTEM_ADMIN;

        // saving in DB
        admin.save();
        return admin;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public String toString() {
        return "User{" +
                "email:"+email+
                ", name:"+name+
                ", status:"+role.name()
                +"}";
    }


}
