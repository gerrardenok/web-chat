package security;

import models.User;
import data.UserDTO;
import actions.GlobalContextParams;

/**
 * <p>
 * Provide the Users security helper methods.
 * </p>
 *
 * @author Mikhail Vatalev(m.vatalev@euroats.com)
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
     * @param userDTO UserDTO data binding from login form
     */
    public static boolean isUserMayLogin(UserDTO userDTO) {
        return (User.findByEmailAndPassword(userDTO.email, userDTO.password) != null);
    }

    /**
     * Check is logged in system
     */
    public static boolean isUserLogged() {
        return (GlobalContextParams.loggedUser() != null);
    }

}
