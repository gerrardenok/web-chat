package models;

import play.db.ebean.Model;
import javax.persistence.*;
import system.ChatRoomStatus;

import play.Logger;
import system.UserRoomRole;

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
public class Room extends Model {

    @Id
    Long id;

    @Column(nullable = false)
    String theme;

    @Column(nullable = false)
    ChatRoomStatus status;

    List<Message> messages =  new ArrayList<>();
    List<UserRoomRelationship> relationships = new ArrayList<>();

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @OneToMany(targetEntity = UserRoomRelationship.class, mappedBy = "room", cascade = CascadeType.ALL)
    public List<UserRoomRelationship> getRelationships() {
        return relationships;
    }

    @OneToMany(targetEntity = Message.class, mappedBy = "room", cascade = CascadeType.ALL)
    public List<Message> getMessages() {
        return messages;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public Room(String theme) {
        this.theme = theme;
        this.status = ChatRoomStatus.ACTIVE;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static final Finder<Long, Room> find = new Finder<Long, Room>(Long.class, Room.class);

    public static Room findById(final Long id) {
        return find.where().eq("id", id).findUnique();
    }

    public static Room findDefaultRoom() {
        return find.where().eq("status", ChatRoomStatus.DEFAULT).findUnique();
    }


    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static void CreateDefaultRoom() {
        Room defaultRoom = new Room("Default room");
        // check this room as default
        defaultRoom.status = ChatRoomStatus.DEFAULT;
        // find root user
        User root = User.findRoot();
        if(root != null) {
            // create new relationship
            UserRoomRelationship relationship = new UserRoomRelationship(root, defaultRoom, UserRoomRole.ADMIN);
            // add admin to room (root)
            defaultRoom.getRelationships().add(relationship);
            // save in DB
            defaultRoom.save();
            // send test message
            root.sendMessage(defaultRoom, "This is first message");
        } else {
            Logger.error("Not found root user");
        }
    }


    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public Long getId() {
        return this.id;
    }

    public String getTheme() {
        return this.theme;
    }

    public ChatRoomStatus getStatus() {
        return this.status;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public String toString() {
        return "Room{" +
                "theme:"+theme+
                ", status:"+status.name()
                +"}";
    }

}
