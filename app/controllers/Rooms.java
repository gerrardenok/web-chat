package controllers;

import actions.GlobalContextParams;
import com.avaje.ebean.Transaction;
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
 * <p>
 * Контроллер реализующий действия пользователей в комнатах {@link Room}.
 * </p>
 *
 * @author Mikhail Vatalev(m.vatalev@euroats.com)
 * @version 1.0
 */
public class Rooms extends Application {
    /**
     * Метод отправки и сохранения сообщения в базе данных {@link Message}.
     * Метод обрабатывает данные с формы (Http POST) преобразует в объект модели и сохраняет
     * @return Сохранённое сообщение в JSON формате (признак успешной транзации)
     * @version 1.0
     */
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

    /**
     * Метод реализовывает получение лога сообщений из комнаты по заданному интервалу
     * @param id Уникальынй индификатор комнаты из которой пользователь запрашивает лог сообщений.
     * @param from Дата начата интервала в миллисикундах начиная с 01.01.1970.
     * @param to Дата конца интервала в миллисикундах начиная с 01.01.1970.
     * @return Коллекцию сообщений в JSON форматею.
     * @version 1.0
     */
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

    /**
     * Метод реазовывает получение всех пользователей из комнты.
     * @param room_id Уникальынй индификатор комнаты из которой запрашивается коллекция пользователей.
     * @return Коллекция пользователей в JSON формате.
     * @version 1.0
     */
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

    /**
     * Метод входа в админ панель комнаты (с проверками безопасности).
     * @param id Уникальынй индификатор комнаты
     * @return Html документ админ панели комнаты
     * @version 2.0
     */
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
