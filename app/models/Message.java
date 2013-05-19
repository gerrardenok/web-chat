package models;

import data.MessageDTO;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import play.db.ebean.Model;
import system.Formatter;

import javax.persistence.*;
import java.util.List;


/**
 * <p>
 * Represents the message entity.
 * </p>
 *
 * @author Mikhail Vatalev(m.vatalev@euroats.com)
 */

@Entity
@Table(name = "messages")
public class Message extends Model{

    @JsonIgnore
    @Id
    @GeneratedValue
    Long id;

    @Column(nullable = false)
    public String message;

    @JsonIgnore
    @Column(nullable = false)
    Room room;

    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
    User from;

    @JsonIgnore
    @Column(nullable = false)
    public DateTime send;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="room_id")
    public Room getRoom() {
        return room;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public Message(String message, User from, Room to) {
        this.from = from;
        this.room = to;
        this.message = message;
        this.send = DateTime.now();
    }

    public Message(User from, Room to, String message, Long send) {
        this.from = from;
        this.room = to;
        this.message = message;
        this.send = new DateTime(send);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static final Finder<Long, Message> find = new Finder<Long, Message>(Long.class, Message.class);

    public static Message findById(final Long id) {
        return find.where().eq("id", id).findUnique();
    }

    public static List<Message> findByRoomAndInterval(final Long id, DateTime from, DateTime to) {
        return find.where().eq("room.id", id).between("send", from, to).findList();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // ~~~~~    JSON FORMAT  ~~~~~~~~
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @JsonIgnore
    public User getSender() {
        return from;
    }

    @JsonProperty("userName")
    public String getUserName() {
        return this.from.name;
    }

    /*
    @JsonProperty("userEmail")
    public String getUserEmail() {
        return this.from.email;
    }

    @JsonProperty("roomId")
    public Long getRoomId() {
        return this.room.getId();
    }
    */

    @JsonProperty("dateLong")
    public Long getDateTime() {
        return this.send.getMillis();
    }

    @JsonProperty("isMyMessage")
    public boolean isLoggedUserOwner() {
        User loggedUser = actions.GlobalContextParams.loggedUser();
        return (loggedUser != null) && (this.from.equals(loggedUser));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public String toString() {
        return String.format("Message{form: %s, to: %s, message: %s, date: %s}", this.from.email, this.room.getId(), this.message, this.send.toString());
    }
}
