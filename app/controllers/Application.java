package controllers;

import play.*;
import play.mvc.*;
import models.User;
import data.AuthUser;
import play.data.Form;
import actions.GlobalContextParams;
import security.UserSecurityHelper;

import views.html.*;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * Main controller
 * </p>
 *
 * @author Mikhail Vatalev(m.vatalev@euroats.com)
 */

@With(GlobalContextParams.class)
public class Application extends Controller {
    public static Result index() {
        // login form
        Form<AuthUser> loginForm = Form.form(AuthUser.class);

        // List of users
        List<User> users = new ArrayList<User>();

        // security validation
        if(UserSecurityHelper.isUserLogged()) {
            users = User.find.all();
        }

        return ok(views.html.pages.home.render(loginForm, users));
    }
  
}
