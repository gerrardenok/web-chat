package start;

import play.Play;
import play.Configuration;

/**
 * <p>
 * Contain the Default Settings of Root user in system
 * </p>
 *
 * @author Mikhail Vatalev(m.vatalev@euroats.com)
 */

public abstract class RootUserSettings {
    // global config attached {@link application.conf}
    private static final Configuration CONFIG = Play.application().configuration();
    // admin settings keys
    private static final String EMAIL_KEY ="root.user.email";
    private static final String NAME_KEY="root.user.name";
    private static final String PASSWORD_KEY="root.user.password";
    // default admin settings
    public static final String EMAIL = CONFIG.getString(EMAIL_KEY);
    public static final String NAME = CONFIG.getString(NAME_KEY);
    public static final String PASSWORD = CONFIG.getString(PASSWORD_KEY);
}
