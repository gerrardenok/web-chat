import controllers.routes;
import models.Room;
import models.User;
import org.joda.time.DateTimeZone;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.Play;
import play.mvc.Controller;
import play.mvc.Http.RequestHeader;

import start.RootUserSettings;

/**
 * <p>
 * Глобальный класс который обрабатывает базовые операции приложения.
 * </p>
 *
 * @author Mikhail Vatalev(m.vatalev@euroats.com)
 * @version 1.0
 */
public class Global extends GlobalSettings {

    /**
     * Действия выполняемые перед стартом приложения
     * @param app запускаемое приложение
     */
    @Override
    public void beforeStart(Application app) {}

    /**
     * Действия выполняемые на старте приложения.
     * Проверяем наличие владельца системы если нет то создаём.
     * Проверяем если наличие общей комнаты если нет то создаём.
     * @param app запускаемое приложение
     */
    @Override
    public void onStart(Application app) {
        // set default server timezone
        DateTimeZone.setDefault(DateTimeZone.UTC);

        // Create admin if not exist
        if (User.findRoot() == null) {
            User.createAdmin(RootUserSettings.EMAIL, RootUserSettings.NAME, RootUserSettings.PASSWORD);
        }
        // Create default room if not exist
        if(Room.findDefaultRoom() == null) {
            Logger.info("Create Default Room for all users");
            Room.CreateDefaultRoom();
        } else {
            Logger.info("Default Room is already exist");
        }
    }

    /**
     * При ошибке вызова метода по http протоколу выводим для пользователя сообщение об ощибке
     * @param requestHeader контекст запроса
     */
    @Override
    public play.mvc.Result onHandlerNotFound(RequestHeader requestHeader) {
        Logger.error("Not found: " + requestHeader.path());
        return Controller.redirect(routes.Application.not_found());
    }

    /**
     * При ошибке работы метода по http протоколу пользовательское выводим сообщение об ощибке
     * @param requestHeader контекст запроса
     * @param throwable контекст ошибки
     */
    @Override
    public play.mvc.Result onError(RequestHeader requestHeader, Throwable throwable) {
        if (Play.isDev()) {
            return super.onError(requestHeader, throwable);
        } else {
            return Controller.redirect(routes.Application.internal_server_error());
        }
    }
}
