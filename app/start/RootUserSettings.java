package start;

import play.Play;
import play.Configuration;

/**
 * <p>
 * Класс реализует получение настроек пользователя владельца приложения.
 * </p>
 *
 * @author Mikhail Vatalev(m.vatalev@euroats.com)
 * @version 1.0
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
