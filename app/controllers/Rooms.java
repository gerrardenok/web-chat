package controllers;

import actions.GlobalContextParams;
import data.MessageDTO;
import models.Message;
import models.Room;
import models.User;
import org.codehaus.jackson.JsonNode;
import static play.libs.Json.toJson;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.ISODateTimeFormat;
import play.Logger;
import play.data.Form;
import play.mvc.BodyParser;
import play.mvc.Result;
import system.Formatter;

import java.util.List;

/**
 * @author Mikhail Vatalev(m.vatalev@euroats.com)
 */

public class Rooms extends Application {

    public static Result sendMessage() {
        // bind from request
        Form<MessageDTO> messageDTOForm = Form.form(MessageDTO.class).bindFromRequest();

        // form validation
        if (messageDTOForm.hasErrors()) {
            return badRequest();
        }

        MessageDTO messageDTO = messageDTOForm.get();
        Logger.info(messageDTO.toString());

        // save message
        User user = User.findByEmail(messageDTO.from);
        Room room = Room.findById(messageDTO.to);

        if((user == null) || (room == null)) {
            return badRequest();
        }

        // save in db
        Message message = new Message(user, room, messageDTO.message, messageDTO.date);
        room.getMessages().add(message);
        room.save();

        Logger.info(String.format("%s save in db", message));
        return ok();
    }

    public static Result getMessages(Long id, Long from, Long to) {
        User user = GlobalContextParams.loggedUser();
        if (user == null) {
            return forbidden();
        }

        if (Room.hasWithId(id)) {
            return badRequest();
        }

        DateTime dttf = new DateTime(from);
        DateTime dtto = new DateTime(to);

        List<Message> messages = Message.findByRoomAndInterval(id, dttf, dtto);
        return ok(toJson(messages));
    }
}
