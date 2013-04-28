package controllers;

import play.*;
import play.mvc.*;
import views.html.*;

/**
 * <p>
 * Represents the user's action. {@link models.User}
 * </p>
 *
 * @author Mikhail Vatalev(m.vatalev@euroats.com)
 */

public class Users extends Controller{

    public static Result signUp() {
        return ok();
    }

    public static Result signIn() {
        return ok();
    }

    public static Result signOut() {
        return ok();
    }

    public static Result save() {
        return ok();
    }
}
