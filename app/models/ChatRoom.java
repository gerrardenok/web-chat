package models;

import play.db.ebean.Model;
import javax.persistence.*;
import system.ChatRoomStatus;

import models.User;
import play.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Represents the chat room entity.
 * </p>
 *
 * @author Mikhail Vatalev(m.vatalev@euroats.com)
 */

@Entity
@Table(name = "rooms")
public class ChatRoom extends Model {

    @Id
    Long id;

    @Column(nullable = false)
    String theme;

    @Column(nullable = false)
    ChatRoomStatus status;

    public List<User> users;

    public User admin;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public ChatRoom(String theme) {
        this.theme = theme;
        this.status = ChatRoomStatus.ACTIVE;
        this.users = new ArrayList<>();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static final Finder<Long, ChatRoom> find = new Finder<Long, ChatRoom>(Long.class, ChatRoom.class);

    public static ChatRoom findById(final Long id) {
        return find.where().eq("id", id).findUnique();
    }

    public static ChatRoom findDefaultRoom() {
        return find.where().eq("status", ChatRoomStatus.DEFAULT).findUnique();
    }


    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static void CreateDefaultRoom() {
        ChatRoom defaultRoom = new ChatRoom("Default room");
        // check this room as default
        defaultRoom.status = ChatRoomStatus.DEFAULT;
        // add admin (root)
        defaultRoom.admin = User.findRoot();
        // save in DB
        defaultRoom.save();
    }


    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public String toString() {
        return "ChatRoom{" +
                "theme:"+theme+
                ", status:"+status.name()
                +"}";
    }

}
