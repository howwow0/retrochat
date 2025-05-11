package how.wow.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static how.wow.config.utils.ConfigHelper.getRequiredProperty;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NetworkProperties {
    @Getter
    private static final Integer port;

    @Getter
    private static final Integer maxUsers;

    @Getter
    private static final String host;


    static {
        Properties properties = new Properties();
        try (InputStream inputStream = CoreConfig.class.getResourceAsStream("/network.properties")) {
            properties.load(inputStream);
            port = Integer.parseInt(getRequiredProperty(properties, "network.port"));
            maxUsers = Integer.parseInt(getRequiredProperty(properties, "network.max_users"));
            host = getRequiredProperty(properties, "network.host");
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки конфига", e);
        }
    }
}
