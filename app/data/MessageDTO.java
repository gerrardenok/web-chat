package data;

import models.Room;
import org.joda.time.DateTime;
import play.data.validation.Constraints;

/**
 * <p>Реализует модель для предачи данных сообщений между клиентом и сервером {@link models.Message}</p>
 * @author Mikhail Vatalev(m.vatalev@euroats.com)
 * @version 1.0
 */
public class MessageDTO {
    @Constraints.Required
    @Constraints.Email
    public String from;

    @Constraints.Required
    public Long to;

    public String message;

    @Constraints.Required
    public Long date;

    public MessageDTO(){};

    public MessageDTO(String from, Long to, String message, Long date) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = date;
    }

    public MessageDTO(String from, Long to) {
        this.from = from;
        this.to = to;
    }

    public MessageDTO(String from) {
        this.from = from;
        this.to = Room.findDefaultRoom().getId();
        this.date = DateTime.now().getMillis();
        this.message="";
    }

    @Override
    public String toString(){
        return String.format("MessageDTO{ from: %s , to: %s , date: %s, message: %s }", this.from, this.to, this.date, this.message);
    }
}
