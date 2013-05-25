package data;

import actions.GlobalContextParams;
import models.User;
import models.UserRoomRelationship;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import system.UserRoomRole;

/**
 * <p>Реализует модель для предачи данных об связях между пользоветем и комнатой чата {@link models.UserRoomRelationship}</p>
 * @author Mikhail Vatalev(m.vatalev@euroats.com)
 * @version 1.0
 */
public class UserRelationshipDTO {
    @JsonIgnore
    public User user;

    public String userName;
    private String userEmail;
    public String role;

    @JsonIgnore
    public String userStatus;
    @JsonIgnore
    public UserRoomRole roomRole;

    public UserRelationshipDTO(UserRoomRelationship relationship) {
        this.userName = relationship.user.name;
        this.userEmail= relationship.user.email;
        this.roomRole = relationship.getRole();
        this.role = relationship.getRole().name();
    }

    @JsonProperty("isMyUser")
    public boolean isLoggedUser() {
        User loggerUser = GlobalContextParams.loggedUser();
        return (loggerUser != null) && loggerUser.email.equals(this.userEmail);
    }

    @JsonProperty("isAdmin")
    public boolean isAdmin() {
        return this.roomRole.equals(UserRoomRole.ADMIN);
    }
}
