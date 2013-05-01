package controllers;

import org.apache.commons.lang3.StringUtils;
import play.data.Form;
import play.mvc.*;

import models.User;
import data.UserDTO;
import security.UserSecurityHelper;

import play.Logger;

/**
 * <p>
 * Represents the user's action. {@link models.User}
 * </p>
 *
 * @author Mikhail Vatalev(m.vatalev@euroats.com)
 */

public class Users extends Application{

    public static Result signUp() {
        Form<UserDTO> registrationForm = Form.form(UserDTO.class);
        return ok(views.html.pages.sign_up.render(registrationForm));
    }

    public static Result signIn() {
        Form<UserDTO> authorizationForm = Form.form(UserDTO.class).bindFromRequest();

        // form validation
        if(authorizationForm.hasErrors()) {
            return badRequest();
        }

        UserDTO userDTO = authorizationForm.get();
        // Logger.info("Form binding, user: " + userDTO);

        // User validation
        if(!UserSecurityHelper.isUserMayLogin(userDTO)) {
            return forbidden();
        }

        // save user in session
        ctx().session().put("user_email", userDTO.email);
        Logger.info("login success, user_email: " + userDTO.email);

        return redirect(routes.Application.index());
    }

    public static Result signOut() {
        if(UserSecurityHelper.isUserLogged()) {
            session().clear();
            Logger.info("Logout completed");
        }
        return redirect(routes.Application.index());
    }

    public static Result save() {
        Form<UserDTO> userForm = Form.form(UserDTO.class).bindFromRequest();

        // form validation
        if(userForm.hasErrors()) {
            return badRequest();
        }

        UserDTO userDTO = userForm.get();

        // User validation
        if(UserSecurityHelper.isUserExist(userDTO.email)) {
            return badRequest();
        }

        User user = new User(userDTO);
        user.save();
        Logger.info("Saving success: "+userForm.get());

        return redirect(routes.Application.index());
    }
}
