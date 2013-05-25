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
 * Представляет сущность Связи смежду пользователем и комнатой которая храниться в базе данных.
 * </p>
 *
 * @author Mikhail Vatalev(m.vatalev@euroats.com)
 * @version 1.0
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

    /**
     * Метод реализует поиск связей в базе по уникальному ключу команты
     * @param roomId уникальный индификатор комнаты
     * @return Коллекция связей
     * @version 1.0
     */
    public static List<UserRoomRelationship> findByRoomId(final Long roomId) {
        return find.where().eq("room.id", roomId).findList();
    }

    /**
     * Метод реализует поиск связей админстарторов в базе по уникальному ключу команты
     * @param roomId уникальный индификатор комнаты
     * @return Коллекция связей
     * @version 1.0
     */
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

    /**
     * Метод реализует повышение статуса рядового пользоваетля в комнате до администатора {@link system.UserRoomRole}
     * @param relationship связь для которой будет повышен статус
     * @version 2.0
     */
    public static void increaseToAdmin(UserRoomRelationship relationship) {
        relationship.role=UserRoomRole.ADMIN;
        relationship.update();
    }
}

