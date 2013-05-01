package controllers;

import play.*;
import play.mvc.*;
import models.User;
import data.UserDTO;
import play.data.Form;
import actions.GlobalContextParams;

import views.html.*;


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
        Form<UserDTO> loginForm = Form.form(UserDTO.class);
        return ok(views.html.pages.home.render(loginForm));
    }
  
}
