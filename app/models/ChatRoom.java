package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;
import javax.persistence.*;

import java.util.List;

@Entity
@Table(name = "rooms")
public class ChatRoom extends Model {

    @Id
    Long id;

    @Column(nullable = false)
    String theme;

    public List<User> users;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public ChatRoom(String theme) {
        this.theme = theme;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static final Finder<Long, ChatRoom> find = new Finder<Long, ChatRoom>(Long.class, ChatRoom.class);

    public static ChatRoom findById(final Long id) {
        return find.where().eq("id", id).findUnique();
    }

}
