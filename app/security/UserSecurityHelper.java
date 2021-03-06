package security;

import data.AuthUser;
import models.User;
import data.UserDTO;
import actions.GlobalContextParams;

/**
 * <p>
 * Класс реализует методы проверки безопастности для пользователей.
 * </p>
 *
 * @author Mikhail Vatalev(m.vatalev@euroats.com)
 * @version 1.0
 */
public class UserSecurityHelper {

    /**
     * Check is user exist in database
     * @param email String email of user
     */
    public static boolean isUserExist(String email) {
        return (User.findByEmail(email) != null);
    }

    /**
     * Check is user may to login in system
     * @param authUser AuthUser data binding from login form
     */
    public static boolean isUserMayLogin(AuthUser authUser) {
        return (User.findByEmailAndPassword(authUser.email, authUser.password) != null);
    }

    /**
     * Check is logged in system
     */
    public static boolean isUserLogged() {
        return (GlobalContextParams.loggedUser() != null);
    }

}
