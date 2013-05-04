package data;

import play.data.validation.Constraints;

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
