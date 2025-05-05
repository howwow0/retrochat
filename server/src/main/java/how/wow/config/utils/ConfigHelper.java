package how.wow.config.utils;

import java.util.Objects;
import java.util.Properties;

public class ConfigHelper {
    public static String getRequiredProperty(Properties props, String key) {
        return Objects.requireNonNull(props.getProperty(key), "Не найдено свойство: " + key);
    }
}
