package actions;

import models.User;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

/**
 * <p>
 * Provides methods for global app params.
 * </p>
 *
 * @author Mikhail Vatalev(m.vatalev@euroats.com)
 */

public class GlobalContextParams extends Action.Simple {

    public static final String LOGGED_USER_CTX_KEY = "loggedUser";

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
     * Returns the instance of logged user.
     * @return the instance of logged user.
     */
    public static User loggedUser() {
        return (User) Http.Context.current().args.get(LOGGED_USER_CTX_KEY);
    }

}

