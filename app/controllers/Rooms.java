package controllers;

import actions.GlobalContextParams;
import data.MessageDTO;
import models.Message;
import models.Room;
import models.User;
import org.codehaus.jackson.JsonNode;
import static play.libs.Json.toJson;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
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

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);

        Logger.info(String.format("%s save in db", message));
        return ok(objectMapper.valueToTree(message));
    }

    public static Result getMessages(Long id, String from, String to) {
        User user = GlobalContextParams.loggedUser();
        if (user == null) {
            return forbidden();
        }

        if (!Room.hasWithId(id)) {
            return badRequest();
        }

        DateTime dttf = DateTime.parse(from, DateTimeFormat.forPattern(Formatter.DATE_TIME_JODA_FORMATTER));
        DateTime dtto = DateTime.parse(to, DateTimeFormat.forPattern(Formatter.DATE_TIME_JODA_FORMATTER));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);

        List<Message> messages = Message.findByRoomAndInterval(id, dttf, dtto);
        return ok(objectMapper.valueToTree(messages));
    }
}
