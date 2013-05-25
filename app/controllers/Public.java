package controllers;

import actions.GlobalContextParams;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;

/**
 * <p>
 * Контроллер реализующий действия пользователей без проверок безопастности. {@link User}
 * </p>
 *
 * @author Mikhail Vatalev(m.vatalev@euroats.com)
 * @version 1.0
 */

public class Public extends Application {

    public static Result changeLanguage(String code) {
        changeLang(code);
        User loggerUser = GlobalContextParams.loggedUser();
        return redirect(routes.Application.index());
    }
}
