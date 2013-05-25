package actions;

import models.User;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

/**
 * <p>
 * Реализует методы приложения с привязкой к глобальному контексту.
 * </p>
 * @author Mikhail Vatalev(m.vatalev@euroats.com)
 * @version 1.0
 */

public class GlobalContextParams extends Action.Simple {

    public static final String LOGGED_USER_CTX_KEY = "loggedUser";

    /**
     *
     * @param ctx текуший Http контекст пользователя
     * @return Html документ который попадает в браузер
     * @throws Throwable разрешает всплытие ошибок времени выполнения
     * @version 1.0
     */
    public Result call(Http.Context ctx) throws Throwable {
        User user = null;
        String userEmail = ctx.session().get("user_email");
        if (userEmail != null) {
            user = User.findByEmail(userEmail);
        }
        if (user != null) {
            ctx.args.put(LOGGED_USER_CTX_KEY, user);
        }

        return delegate.call(ctx);
    }

    /**
     * Возвращяет объект пользователя который находится в системе.
     * @return объект пользователя который находится в системе.
     * @version 1.0
     */
    public static User loggedUser() {
        return (User) Http.Context.current().args.get(LOGGED_USER_CTX_KEY);
    }

}

