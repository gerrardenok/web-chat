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

    public Message(User from, Room to, String message, String send) {
        this.from = from;
        this.room = to;
        this.message = message;
        this.send = DateTime.parse(send, DateTimeFormat.forPattern(Formatter.DATE_TIME_JODA_FORMATTER));
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
    @JsonIgnore
    public User getSender() {
        return from;
    }

    @JsonProperty("user_name")
    public String getUserName() {
        return this.from.name;
    }

    @JsonProperty("user_email")
    public String getUserEmail() {
        return this.from.email;
    }

    @JsonProperty("room_id")
    public Long getRoomId() {
        return this.room.getId();
    }
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public String toString() {
        return String.format("Message{form: %s, to: %s, message: %s, date: %s}", this.from.email, this.room.getId(), this.message, this.send.toString());
    }
}
