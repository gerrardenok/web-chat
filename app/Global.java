import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.Play;
import play.data.format.Formatters;
import play.mvc.Controller;
import play.mvc.Http.RequestHeader;
import java.util.TimeZone;


public class Global extends GlobalSettings {

    @Override
    public void beforeStart(Application app) {}

    @Override
    public void onStart(Application app) {}

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
