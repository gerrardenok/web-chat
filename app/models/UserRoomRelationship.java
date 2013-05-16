package models;

import play.db.ebean.Model;
import models.User;
import models.Room;
import start.RootUserSettings;
import system.UserRole;
import system.UserRoomRole;

import javax.persistence.*;
import java.util.List;

/**
 * <p>
 * Represents the relationship between user and the chat room entity.
 * </p>
 *
 * @author Mikhail Vatalev(m.vatalev@euroats.com)
 */
@Entity
@Table(name = "user_room_relationship")
public class UserRoomRelationship extends Model {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_email")
    public User user;

    @ManyToOne
    @JoinColumn(name="room_id")
    public Room room;

    private UserRoomRole role;

    // ~~~~~~~~~~~~~~~~~~~~~~~

    public UserRoomRelationship(User user, Room room) {
        this.user=user;
        this.room=room;
        this.role=UserRoomRole.USER;
    }

    public UserRoomRelationship(Room room, User user) {
        this.user=user;
        this.room=room;
        this.role=UserRoomRole.USER;
    }

    public UserRoomRelationship(User user, Room room, UserRoomRole role) {
        this.user=user;
        this.room=room;
        this.role=role;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static final Finder<String, UserRoomRelationship> find = new Finder<String, UserRoomRelationship>(String.class, UserRoomRelationship.class);

    public static List<UserRoomRelationship> findByEmail(final String email) {
        return find.where().eq("user.email", email).findList();
    }

    public static List<UserRoomRelationship> findByRoomId(final Long roomId) {
        return find.where().eq("room.id", roomId).findList();
    }

    public static List<UserRoomRelationship> findAdminsByRoomId(final Long roomId) {
        return find.where().eq("room.id", roomId).eq("role", UserRoomRole.ADMIN).findList();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~

    public UserRoomRole getRole() {
        return this.role;
    }

    public void setRole(UserRoomRole role) {
        this.role = role;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~

    public static void increaseToAdmin(UserRoomRelationship relationship) {
        relationship.role=UserRoomRole.ADMIN;
        relationship.update();
    }
}

