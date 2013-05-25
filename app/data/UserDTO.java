package data;

import play.data.validation.Constraints;


/**
 * <p>Реализует модель для предачи данных пользователей между клиентом и сервером {@link models.User}</p>
 * @author Mikhail Vatalev(m.vatalev@euroats.com)
 * @version 1.0
 */
public class UserDTO {

    @Constraints.Required
    @Constraints.Email
    public String email;

    @Constraints.Required
    public String password;

    @Constraints.Required
    public String name;

    public UserDTO() {
        // add default value for correct working of form validation
        name = "";
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~

    public String toString() {
        return "UserDTO{" +
                "email:"+email+
                ", name:"+name+
                ", password:"+password
                +"}";
    }
}
