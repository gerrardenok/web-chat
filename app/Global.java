import models.Room;
import models.User;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.Play;
import play.mvc.Controller;
import play.mvc.Http.RequestHeader;

import start.RootUserSettings;


public class Global extends GlobalSettings {

    @Override
    public void beforeStart(Application app) {}

    @Override
    public void onStart(Application app) {
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

    @Override
    public play.mvc.Result onHandlerNotFound(RequestHeader requestHeader) {
        Logger.error("Not found: " + requestHeader.path());
        return Controller.notFound(views.html.error.error404.render());
    }

    @Override
    public play.mvc.Result onError(RequestHeader requestHeader, Throwable throwable) {
        if (Play.isDev()) {
            return super.onError(requestHeader, throwable);
        } else {
            return Controller.internalServerError(views.html.error.errorUnknown.render());
        }
    }
}
