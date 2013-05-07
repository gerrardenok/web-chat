package controllers;

import data.AuthUser;
import org.apache.commons.lang3.StringUtils;
import play.data.Form;
import play.mvc.*;

import models.User;
import models.ChatRoom;
import data.UserDTO;
import security.UserSecurityHelper;

import play.Logger;

import java.util.ArrayList;
import java.util.List;

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
        Form<AuthUser> authorizationForm = Form.form(AuthUser.class).bindFromRequest();

        // form validation
        if(authorizationForm.hasErrors()) {
            List<User> users_stub = new ArrayList<>();
            return badRequest(views.html.pages.home.render(authorizationForm, users_stub));
        }

        AuthUser authUser = authorizationForm.get();
        // Logger.info("Form binding, user: " + userDTO);

        // User validation
        if(!UserSecurityHelper.isUserMayLogin(authUser)) {
            return forbidden();
        }

        // save user in session
        ctx().session().put("user_email", authUser.email);
        Logger.info("login success, user_email: " + authUser.email);

        return redirect(routes.Application.index());
    }

    public static Result signOut() {
        if(UserSecurityHelper.isUserLogged()) {
            session().clear();
            Logger.info("Logout completed");
        }
        return redirect(routes.Application.index());
    }

    public static Result create() {
        Form<UserDTO> userForm = Form.form(UserDTO.class).bindFromRequest();

        // form validation
        if(userForm.hasErrors()) {
            // stub
            return badRequest(views.html.pages.sign_up.render(userForm));
        }

        UserDTO userDTO = userForm.get();

        // User validation
        if(UserSecurityHelper.isUserExist(userDTO.email)) {
            return badRequest();
        }

        User user = save(userDTO);

        // Add to default Room
        ChatRoom defaultRoom = ChatRoom.findDefaultRoom();
        if(defaultRoom != null) {
            user.joinToChatRoom(defaultRoom);
        }

        return redirect(routes.Application.index());
    }

    protected static User save(UserDTO userDTO) {
        User user = new User(userDTO);
        user.save();
        Logger.info("Saving success: "+userDTO);
        return user;
    }
}
