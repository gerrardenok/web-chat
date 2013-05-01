package data;

import play.data.validation.Constraints;

public class UserDTO {

    @Constraints.Required
    @Constraints.Email
    public String email;

    @Constraints.Required
    public String password;

    public String name;

    public UserDTO() {}

    // ~~~~~~~~~~~~~~~~~~~~~~~~

    public String toString() {
        return "UserDTO{" +
                "email:"+email+
                ", name:"+name+
                ", password:"+password
                +"}";
    }
}
