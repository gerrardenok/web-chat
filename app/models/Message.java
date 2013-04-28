package models;

import org.joda.time.DateTime;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import javax.persistence.*;


@Entity
@Table(name = "messages")
public class Message extends Model{

    @Id
    Long id;

    @Column(nullable = false)
    public String message;

    @ManyToOne(fetch=FetchType.LAZY)
    User from;

    public DateTime send;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public Message(String message, User from) {
        this.from = from;
        this.message = message;
        this.send = DateTime.now();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static final Finder<Long, Message> find = new Finder<Long, Message>(Long.class, Message.class);

    public static Message findById(final Long id) {
        return find.where().eq("id", id).findUnique();
    }
}
