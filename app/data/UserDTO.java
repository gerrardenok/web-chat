package data;

import play.data.validation.Constraints;

public class UserDTO {

    @Constraints.Required
    @Constraints.Email
    public String email;

    @Constraints.Required
    private String password;

    public UserDTO() {}
}
