package data;

import play.data.validation.Constraints;

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
