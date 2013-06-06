package controllers;

import actions.GlobalContextParams;
import com.avaje.ebean.annotation.Transactional;
import data.AuthUser;
import models.Room;
import models.UserRoomRelationship;
import play.data.Form;
import play.mvc.*;

import models.User;
import data.UserDTO;
import security.UserSecurityHelper;

import play.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Представляет действия совершаемые непосредственно пользователем. {@link models.User}
 * </p>
 *
 * @author Mikhail Vatalev(m.vatalev@euroats.com)
 * @version 1.0
 */

public class Users extends Application{

    /**
     * Метод предоставляет доступ к странце регистрации.
     * @return Html документ страница регистрации.
     * @version 1.0
     */
    public static Result signUp() {
        Form<UserDTO> registrationForm = Form.form(UserDTO.class);
        return ok(views.html.pages.sign_up.render(registrationForm));
    }

    /**
     * Метод реализует вход пользоваетля ыв систему с проверкой безопастности.
     * @return в случае прохождения проверок перенаправление на индексную страницу.
     * @version 1.0
     */
    public static Result signIn() {
        Form<AuthUser> authorizationForm = Form.form(AuthUser.class).bindFromRequest();

        // form validation
        if(authorizationForm.hasErrors()) {
            List<UserRoomRelationship> relationships_stub = new ArrayList<>();
            return badRequest(views.html.pages.home.render(authorizationForm, null, null));
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

    /**
     * Метод выхода пользователя из системы (Прямая очичистка сесси пользователя).
     * @return в случае прохождения проверок перенаправление на индексную страницу.
     * @version 1.0
     */
    public static Result signOut() {
        if(UserSecurityHelper.isUserLogged()) {
            session().clear();
            Logger.info("Logout completed");
        }
        return redirect(routes.Application.index());
    }

    /**
     * Метод создания нового пользователя в приложении и сохранение его в базе данных.
     * Метод получает данные с формы обрабатывает их валидирует и случае упеха сохранянет.
     * @return в случае прохождения проверок перенаправление на индексную страницу.
     * @version 1.0
     */
    @Transactional
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

        User user = new User(userDTO);
        user.save();
        Logger.info("Saving success: "+userDTO);

        // Add to default Room
        Room defaultRoom = Room.findDefaultRoom();
        if(defaultRoom != null) {
            user.joinToChatRoom(defaultRoom);
        }

        return redirect(routes.Application.index());
    }
}
