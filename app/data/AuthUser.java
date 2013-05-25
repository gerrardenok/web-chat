package data;

import play.data.validation.Constraints;

/**
 * <p>Реализует модель данных для предачи информации о авторизированом (авторизирующемся пользователе)</p>
 * @author Mikhail Vatalev(m.vatalev@euroats.com)
 * @version 1.0
 */
public class AuthUser {

    @Constraints.Required
    @Constraints.Email
    public String email;

    @Constraints.Required
    public String password;

    public AuthUser() {}

    // ~~~~~~~~~~~~~~~~~~~~~~~~

    public String toString() {
        return "AuthUser(){" +
                "email:"+email+
                ", password:"+password
                +"}";
    }
}
