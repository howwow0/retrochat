package how.wow.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.stream.Collectors;

import static how.wow.config.utils.ConfigHelper.getRequiredProperty;

@NoArgsConstructor( access = AccessLevel.PRIVATE)
public final class CoreConfig {
    @Getter
    private static final String name;
    @Getter
    private static final String motdText;

    static {
        Properties properties = new Properties();
        try (InputStream inputStream = CoreConfig.class.getResourceAsStream("/core.properties");
             InputStream motdFile = CoreConfig.class.getResourceAsStream("/motd.txt")) {

            properties.load(inputStream);
            name = getRequiredProperty(properties, "core.name");
            motdText = motdFile != null ?
                    new BufferedReader(new InputStreamReader(motdFile, StandardCharsets.UTF_8))
                            .lines().collect(Collectors.joining("\n")) :
                    "Добро пожаловать!\n";

        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки конфига", e);
        }
    }
}