package controllers;

import actions.GlobalContextParams;
import data.MessageDTO;
import data.UserRelationshipDTO;
import models.Message;
import models.Room;
import models.User;
import models.UserRoomRelationship;
import org.codehaus.jackson.JsonNode;
import static play.libs.Json.toJson;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.ISODateTimeFormat;
import play.Logger;
import play.data.Form;
import play.mvc.BodyParser;
import play.mvc.Result;
import system.Formatter;

import java.util.ArrayList;
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

    public static Result getMessages(Long id, Long from, Long to) {
        User user = GlobalContextParams.loggedUser();
        if (user == null) {
            return forbidden();
        }

        if (!Room.hasWithId(id)) {
            return badRequest();
        }

        DateTime dttf = new DateTime(from);
        DateTime dtto = new DateTime(to);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);

        List<Message> messages = Message.findByRoomAndInterval(id, dttf, dtto);
        return ok(objectMapper.valueToTree(messages));
    }

    public static Result getUsers(Long room_id) {
        User user = GlobalContextParams.loggedUser();
        if (user == null) {
            return forbidden();
        }
        Room room = Room.findById(room_id);
        if (room == null) {
            return badRequest();
        }
        List<UserRelationshipDTO> result = new ArrayList<>();
        for(UserRoomRelationship relationship: room.getRelationships()) {
            result.add(new UserRelationshipDTO(relationship));
        }
        return ok(toJson(result));
    }

    public static Result adminPanel(Long id) {
        User user = GlobalContextParams.loggedUser();
        if (user == null) {
            return forbidden();
        }
        Room room = Room.findById(id);
        if (room == null) {
            return badRequest();
        }
        return ok();
    }
}
